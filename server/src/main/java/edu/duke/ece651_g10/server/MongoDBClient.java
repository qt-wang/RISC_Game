package edu.duke.ece651_g10.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import org.bson.Document;
import org.bson.types.ObjectId;

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
      Document gameDoc = new Document("_id", new ObjectId());
      gameDoc.append("game_id", game.getGameId()).append("end_game", game.getGameEnd())
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
        .append("owener", territory.getOwner().getPlayerID()).append("army", generateArmyDoc(territory));
    // TODO:append neighbours
    return territoryDoc;
  }

  /**
   * Generate one territory army documentation
   *
   * @param territory The territory in the game
   * @return return the list of Document descirbe the army
   */
  private List<Document> generateArmyDoc(Territory territory) {
    List<Document> armyDoc = new ArrayList<Document>();
    for (int i = 0; i <= 6; i++) {
      armyDoc.add(new Document(String.valueOf(i), territory.getUnitNumber(i)));
    }
    return armyDoc;
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
        .append("tech_level", player.getTechnologyLevel()).append("can_upgrade", player.getCanUpgradeInThisTurn());
    return playerDoc;
  }

  

}
