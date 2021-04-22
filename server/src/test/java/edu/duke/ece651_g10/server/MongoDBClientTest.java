package edu.duke.ece651_g10.server;

import java.util.ArrayList;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MongoDBClientTest {

  @Test
  public void test_add_game2DB() {
    Server server = mock(Server.class);
    V2GameFactory gameFactory = new V2GameFactory(null);
    Game game = gameFactory.createTestGame(2);
    game.addPlayerFromDb(new Player(null, null));
    game.addPlayerFromDb(new Player(null, null));
    game.assignInitialTerritories();
    MongoDBClient mongoClient = new MongoDBClient(
        "mongodb+srv://g10:ece651@cluster0.jjos5.mongodb.net/ece651_risk?retryWrites=true&w=majority");
    MongoDBClient.addGame2DB(game);
    mongoClient.reconstructGameFromDatabase();
  }
}
