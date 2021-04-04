package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class TechResourceUnitCheckerTest {
  @Test
  public void test_check_my_rule() {
    GameMap mockGameMap = mock(GameMap.class);
    OneTerritoryOrder mockOrder1 = mock(OneTerritoryOrder.class);
    OneTerritoryOrder mockOrder2 = mock(OneTerritoryOrder.class);
    HashMap<Integer, Integer> unitUpgradeTable = new HashMap<Integer, Integer>();
    unitUpgradeTable.put(0, 3);
    unitUpgradeTable.put(1, 8);
    unitUpgradeTable.put(2, 19);
    unitUpgradeTable.put(3, 25);
    unitUpgradeTable.put(4, 35);
    unitUpgradeTable.put(5, 50);
    when(mockOrder1.getUnitUpgradeTable()).thenReturn(unitUpgradeTable);
    when(mockOrder2.getUnitUpgradeTable()).thenReturn(unitUpgradeTable);
    Territory mockTerritory1 = mock(Territory.class);
    Player mockPlayer1 = mock(Player.class);
    when(mockPlayer1.getTechnologyResourceTotal()).thenReturn(24);
    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    ownership.put(mockTerritory1, mockPlayer1);
    ownership.put(mockTerritory1, mockPlayer1);
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder1.getNumUnit()).thenReturn(3);
    when(mockOrder2.getNumUnit()).thenReturn(4);
    when(mockOrder1.getLevel()).thenReturn(1);
    when(mockOrder2.getLevel()).thenReturn(1);

    String res = "The player does not have enough technology resources to upgrade the units";
    RuleChecker<OneTerritoryOrder> checker = new TechResourceUnitChecker(null);
    assertEquals(res, checker.checkMyRule(mockOrder2, mockGameMap));
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}
