package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {
  @Test
  public void test_player() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    Player player = new Player(null, null);
    assertEquals(player.getSocket(), player.getSocket());
    assertEquals(player.getSocketNumber(), player.getSocketNumber());
    assertEquals(player.getPlayerID(), player.getPlayerID());
    assertEquals(player.getIsLost(), player.getIsLost());
    assertEquals(player.getJCommunicator(), player.getJCommunicator());
    player.setIsLost();
    assertEquals(true, player.getIsLost());
  }

}










