package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Test;

public class UpgradeUnitOrderTest {
  @Test
  public void test_upgradeUnit() {
     FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    Player p = mock(Player.class);
    UpgradeUnitOrder order = new UpgradeUnitOrder(1, "Elantris", 2, gMap, 0, p);
    assertEquals(gMap.getTerritory("Elantris"), order.getSourceTerritory());
    assertEquals(2, order.getNumUnit());
    assertEquals(0, order.getLevel());
    order.execute();
    assertEquals(3, order.getSourceTerritory().getArmyWithLevel(0).getArmyUnits());
    assertEquals(2, order.getSourceTerritory().getArmyWithLevel(1).getArmyUnits());
  }

}
