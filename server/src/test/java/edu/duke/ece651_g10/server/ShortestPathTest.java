package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ShortestPathTest {
  @Test
  public void test_minCost() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    ShortestPath sp = new ShortestPath(gMap);
    assertEquals(23, sp.minCost(gMap.getTerritory("Narnia"), gMap.getTerritory("Oz")));
    assertEquals(11, sp.minCost(gMap.getTerritory("Elantris"), gMap.getTerritory("Roshar")));
  }

  @Test
  public void test_anotherMap(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createTwoPeopleMap();
    Player player = new Player(null, null);
    ShortestPath sp = new ShortestPath(gMap);
    gMap.getTerritory("A").setUnitNumber(10);
    gMap.getTerritory("B").setUnitNumber(10);
    gMap.getTerritory("C").setUnitNumber(10);
    gMap.getTerritory("D").setUnitNumber(10);
    gMap.getTerritory("E").setUnitNumber(10);
    gMap.getTerritory("F").setUnitNumber(10);
    gMap.getTerritory("G").setUnitNumber(10);
    gMap.getTerritory("H").setUnitNumber(10);
    gMap.getTerritory("A").setSize(1);
    gMap.getTerritory("B").setSize(2);
    gMap.getTerritory("C").setSize(3);
    gMap.getTerritory("D").setSize(4);
    gMap.getTerritory("E").setSize(5);
    gMap.getTerritory("F").setSize(6);
    gMap.getTerritory("G").setSize(7);
    gMap.getTerritory("H").setSize(8);
    assertEquals(5, sp.minCost(gMap.getTerritory("C"), gMap.getTerritory("B")));
    assertEquals(9, sp.minCost(gMap.getTerritory("G"), gMap.getTerritory("B")));
  }

}
