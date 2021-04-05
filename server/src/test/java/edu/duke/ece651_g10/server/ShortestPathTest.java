package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ShortestPathTest {
  @Test
  public void test_minCost() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    Player player = new Player(null, null);
    ShortestPath sp = new ShortestPath(gMap);
    assertEquals(23, sp.minCost(gMap.getTerritory("Narnia"), gMap.getTerritory("Oz")));
    assertEquals(11, sp.minCost(gMap.getTerritory("Elantris"), gMap.getTerritory("Roshar")));
  }

}
