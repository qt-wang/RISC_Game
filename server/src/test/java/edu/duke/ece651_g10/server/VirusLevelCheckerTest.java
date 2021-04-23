package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class VirusLevelCheckerTest {
  @Test
  public void test_virusLevelChecker() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    Player p = new Player(null, null);
    VirusLevelChecker vc = new VirusLevelChecker(null);
    VirusOrder vOrder = new VirusOrder(p.getPlayerID(), gMap, p, "Elantris", 0);
    vc.checkMyRule(vOrder, gMap);
  }

}











