package edu.duke.ece651_g10.server;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class MongoDBClientTest {
  @Disabled
  @Test
  public void test_add_game2DB() {
    Game game = mock(Game.class);
    when(game.getGameId()).thenReturn(1);
    when(game.getGameEnd()).thenReturn(false);
    MongoDBClient mongoClient = new MongoDBClient(
        "mongodb+srv://g10:ece651@cluster0.jjos5.mongodb.net/ece651_risk?retryWrites=true&w=majority");
    mongoClient.addGame2DB(game);
  }
}
