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

    assertEquals(0, player.getFoodResourceTotal());
    assertEquals(0, player.getTechnologyResourceTotal());
    assertEquals(0, player.getTechnologyLevel());

    player.setFoodResourceTotal(100);
    assertEquals(100, player.getFoodResourceTotal());

    player.setTechnologyResourceTotal(120);
    assertEquals(120, player.getTechnologyResourceTotal());

    player.incrementTechnologyLevel();
    assertEquals(1, player.getTechnologyLevel());
  }

}










