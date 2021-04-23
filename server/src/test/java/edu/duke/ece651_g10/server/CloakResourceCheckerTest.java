package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CloakResourceCheckerTest {
  @Test
  public void test_cloackResourceChecker() {
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player1 = new Player(null, null);
      CloakResourceChecker c = new CloakResourceChecker(null);
      CloakOrder o = new CloakOrder(player1.getPlayerID(), "Narnia", gMap, player1);
        c.checkMyRule(o, gMap);
  }

}









