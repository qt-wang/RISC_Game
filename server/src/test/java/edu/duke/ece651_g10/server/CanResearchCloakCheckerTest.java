package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CanResearchCloakCheckerTest {
  @Test
  public void test_checker() {
                FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player1 = new Player(null, null);
      CanResearchCloakChecker c = new CanResearchCloakChecker(null);
      ResearchCloakOrder o = new ResearchCloakOrder(player1.getPlayerID(), gMap, player1);
        c.checkMyRule(o, gMap);
  }

}









