package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CloakOrderTest {
  @Test
  public void test_cloak() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    Player p = new Player(null, null);
    CloakOrder cOrder = new CloakOrder(p.getPlayerID(), "Elantris", gMap, p);
    cOrder.getSourceTerritory();
    cOrder.getNumUnit();
    cOrder.getLevel();
    cOrder.execute();
  }

}











