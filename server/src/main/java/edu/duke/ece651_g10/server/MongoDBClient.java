package edu.duke.ece651_g10.server;

import static com.mongodb.client.model.Filters.eq;
import static com.mongodb.client.model.Filters.gte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;
import org.json.JSONObject;

/**
 * The class to update the game and user information into database
 */
public class MongoDBClient {
  final String connectionString;

  /**
   * The constructor of the MongoDBClient
   */
  public MongoDBClient(String connectionString) {
    this.connectionString = connectionString;
  }

  /**
   * Add the Game into the database
   * 
   * @param game The game to add
   */
  public void addGame2DB(Game game) {
    try (MongoClient mongoClient = MongoClients.create(connectionString)) {
      MongoDatabase riskDB = mongoClient.getDatabase("ece651_risk");
      MongoCollection<Document> gameCollection = riskDB.getCollection("games");
      Bson filter = eq("game_id", game.getGameId());
      gameCollection.deleteMany(filter);
      Document gameDoc = new Document("_id", new ObjectId());
      gameDoc.append("game_id", game.getGameId()).append("end_game", game.getGameEnd())
          .append("begin_game", game.getGameBegins()).append("num_players", game.getNumPlayers())
          .append("territories", generateTerritoryList(game.getGameMap()))
          .append("players", generatePlayerList(game.getAllPlayers()));

      gameCollection.insertOne(gameDoc);
    }
  }

  /**
   * Generate the list of territories
   *
   * @param gameMap The gameMap to describe the game
   * @return return the list of Document describe the territories
   */
  private List<Document> generateTerritoryList(GameMap gameMap) {
    List<Document> territoriesDoc = new ArrayList<Document>();
    HashSet<Territory> territories = gameMap.getAllTerritory();
    for (Territory t : territories) {
      territoriesDoc.add(generateTerritoryDoc(t));
    }
    return territoriesDoc;
  }

  /**
   * Generate one terrritory documentation
   *
   * @param terriroty The territory that be transfered to documentation
   * @return the territory document
   */
  private Document generateTerritoryDoc(Territory territory) {
    Document territoryDoc = new Document("name", territory.getName());
    territoryDoc.append("size", territory.getSize()).append("food_rate", territory.getFoodResourceGenerationRate())
        .append("tech_rate", territory.getTechnologyResourceGenerationRate())
        .append("owner", territory.getOwner().getPlayerID()).append("army", generateArmyDoc(territory))
        .append("owned_spies", generateSpyDocList(territory.getOwnedSpy()))
        .append("enemy_spies", generateSpyDocList(territory.getEnemySpy()))
        .append("old_views", generateOldViewDoc(territory.getAllOldView()));
    return territoryDoc;
  }

  /**
   * Generate one territory army documentation
   *
   * @param territory The territory in the game
   * @return return the Document descirbe the army
   */
  private Document generateArmyDoc(Territory territory) {
    Document armyDoc = new Document();
    for (int i = 0; i <= 6; i++) {
      armyDoc.append(String.valueOf(i), territory.getUnitNumber(i));
    }
    return armyDoc;
  }

  /**
   * Generate one territory spies documentation
   *
   * @param spies The set of spies
   * @return the list of documentation store the spies information
   */
  private List<Document> generateSpyDocList(Set<Spy> spies) {
    List<Document> spiesDoc = new ArrayList<Document>();
    for (Spy s : spies) {
      Document spyDoc = new Document("owner_id", s.getOwner().getPlayerID());
      spiesDoc.add(spyDoc);
    }
    return spiesDoc;
  }

  /**
   * Generate one territory old views documentation
   *
   * @param the hash map for the old views for different player
   * @return the list of document for the old views
   */
  private List<Document> generateOldViewDoc(HashMap<Player, JSONObject> oldViews) {
    List<Document> oldViewsDoc = new ArrayList<Document>();
    for (Map.Entry<Player, JSONObject> entry : oldViews.entrySet()) {
      Document viewDoc = new Document("player_id", entry.getKey().getPlayerID()).append("view",
          Document.parse(entry.getValue().toString()));
      oldViewsDoc.add(viewDoc);
    }
    return oldViewsDoc;
  }

  /**
   * Generate the player list
   *
   * @param players The all players in the game
   * @return the list of all players
   */
  private List<Document> generatePlayerList(HashMap<Integer, Player> players) {
    List<Document> playersDoc = new ArrayList<Document>();
    for (Player p : players.values()) {
      playersDoc.add(generatePlayerDoc(p));
    }
    return playersDoc;
  }

  /**
   * Generate the player documentation
   *
   * @param player The player object will be transferred to documentation
   * @return the player documentation
   */
  private Document generatePlayerDoc(Player player) {
    Document playerDoc = new Document("id", player.getPlayerID());
    playerDoc.append("food", player.getFoodResourceTotal()).append("tech", player.getTechnologyResourceTotal())
        .append("tech_level", player.getTechnologyLevel()).append("can_upgrade", player.getCanUpgradeInThisTurn())
        .append("lost", player.getIsLost());
    return playerDoc;
  }

  /**
   * Reconstruct the game from the database
   *
   * @return the arraylist of the game objects
   */
  public ArrayList<Game> reconstructGameFromDatabase(Server runServer) {
    try (MongoClient mongoClient = MongoClients.create(connectionString)) {
      MongoDatabase riskDB = mongoClient.getDatabase("ece651_risk");
      MongoCollection<Document> gameCollection = riskDB.getCollection("games");
      ArrayList<Game> gameList = new ArrayList<Game>();
      List<Document> gameListDoc = gameCollection.find(gte("game_id", -1)).into(new ArrayList<>());
      for (Document d : gameListDoc) {
        if ((boolean) (d.get("end_game")) == true) {
          continue;
        }
        V2GameFactory gameFactory = new V2GameFactory(runServer);
        Game game = gameFactory.createTestGame((int) d.get("num_players"));
        game = reconstructPlayers(game, d);
        game = reconstructTerritories(game, d);
        game.setGameBegins(d.getBoolean("begin_game"));
        gameList.add(game);
      }
      return gameList;
    }
  }

  /**
   * Reconstruct the players
   *
   * @param game The game needs to reconstruct the players
   * @param doc  the document that store the whole game info in the database
   *             collection
   * @return the players reconstructed game
   */
  private Game reconstructPlayers(Game game, Document doc) {
    List<Document> playerList = (List<Document>) doc.get("players");
    for (Document p : playerList) {
      // TODO: Set up socket and jCommunicater
      Player player = new Player(null, null);
      player.setCanUpgradeInThisTurn(p.getBoolean("can_upgrade"));
      player.setFoodResourceTotal(p.getInteger("food"));
      player.setTechnologyResourceTotal(p.getInteger("tech"));
      player.setTechnologyLevel(p.getInteger("tech_level"));
      if (p.getBoolean("lost") == true) {
        player.setIsLost();
      }
      // player.setPlayerId(p.getplayerId);
      game.addPlayer(player);
    }
    return game;
  }

  /**
   * Reconstruct the territories
   *
   * @param game The game needs to reconstruct the territories
   * @param doc  the document that store the whole game info in the database
   *             collection
   * @return the territories reconstructed game
   */
  private Game reconstructTerritories(Game game, Document doc) {
    List<Document> territoryList = (List<Document>) doc.get("territories");
    GameMap gameMap = game.getGameMap();
    for (Document t : territoryList) {
      Territory territory = gameMap.getTerritory(t.getString("name"));
      territory.setFoodResourceGenerationRate(t.getInteger("food_rate"));
      territory.setTechnologyResourceGenerationRate(t.getInteger("tech_rate"));
      territory.setSize(t.getInteger("size"));
      territory.setOwner(game.getAllPlayers().get(t.getInteger("owner")));
      Document army = (Document) t.get("army");
      territory.setUnitNumber(0);
      for (int i = 0; i <= 6; i++) {
        territory.increaseUnit(army.getInteger(String.valueOf(i)), i);
      }
      List<Document> ownSpiesList = (List<Document>) t.get("owned_spies");
      for (Document s : ownSpiesList) {
        territory.addOwnedSpy(new SpyUnit(game.getAllPlayers().get(s.get("owner_id")), territory));
      }
      List<Document> enemySpiesList = (List<Document>) t.get("enemy_spies");
      for (Document s : enemySpiesList) {
        territory.addEnemySpy(new SpyUnit(game.getAllPlayers().get(s.get("owner_id")), territory));
      }
      List<Document> oldViewList = (List<Document>) t.get("old_views");
      for (Document o : oldViewList) {
        territory.setPlayerView(game.getAllPlayers().get(o.get("player_id")), new JSONObject(o.get("view", Document.class).toJson()));
      }
    }
    return game;
  }
}







