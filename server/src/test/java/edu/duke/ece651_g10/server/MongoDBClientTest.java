package edu.duke.ece651_g10.server;

import java.io.IOException;

import org.junit.jupiter.api.Test;

public class MongoDBClientTest {

  @Test
  public void test_add_game2DB() throws IOException {
    V2GameFactory gameFactory = new V2GameFactory(null);
    Game game = gameFactory.createTestGame(2);
    game.addPlayerFromDb(new Player(null, null));
    game.addPlayerFromDb(new Player(null, null));
    game.assignInitialTerritories();
    Server server = new Server(1234, new V2ServerPasswordGenerator());
    MongoDBClient mongoClient = new MongoDBClient(
        "mongodb+srv://g10:ece651@cluster0.jjos5.mongodb.net/ece651_risk?retryWrites=true&w=majority");
    MongoDBClient.addGame2DB(game);
    MongoDBClient.reconstructGameFromDatabase();
    MongoDBClient.addServer2DB(server);
    MongoDBClient.reconstructServerFromDatabase();
  }
}











