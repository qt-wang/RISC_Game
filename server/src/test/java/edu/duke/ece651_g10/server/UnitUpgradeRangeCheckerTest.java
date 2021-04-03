package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class UnitUpgradeRangeCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    OneTerritoryOrder mockOrder1 = mock(OneTerritoryOrder.class);
    OneTerritoryOrder mockOrder2 = mock(OneTerritoryOrder.class);
    HashMap<Integer, Integer> unitUpgradeTable = new HashMap<Integer, Integer>();
    unitUpgradeTable.put(0, 3);
    unitUpgradeTable.put(1, 8);
    unitUpgradeTable.put(2, 19);
    unitUpgradeTable.put(3, 25);
    unitUpgradeTable.put(4, 35);
    unitUpgradeTable.put(5, 50);
    when(mockOrder1.getLevel()).thenReturn(0);
    when(mockOrder2.getLevel()).thenReturn(6);
    when(mockOrder1.getUnitUpgradeTable()).thenReturn(unitUpgradeTable);
    when(mockOrder2.getUnitUpgradeTable()).thenReturn(unitUpgradeTable);
    
    RuleChecker<OneTerritoryOrder> checker = new UnitUpgradeRangeChecker(null);
    String res = "The player's upgrade level is not in the unit upgrade range.";
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
  }

}





