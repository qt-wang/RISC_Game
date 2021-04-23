package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class ResearchCloakOrderTest {
  @Test
  public void test_researchCloak() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    Player p = new Player(null, null);
    ResearchCloakOrder rOrder = new ResearchCloakOrder(p.getPlayerID(), gMap, p);
    rOrder.execute();
  }

}


