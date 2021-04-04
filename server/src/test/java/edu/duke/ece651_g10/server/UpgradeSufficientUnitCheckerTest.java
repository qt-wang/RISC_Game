package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class UpgradeSufficientUnitCheckerTest {
  @Test
  public void test_check_my_rule() {
    OneTerritoryOrder mockOrder1 = mock(OneTerritoryOrder.class);
    OneTerritoryOrder mockOrder2 = mock(OneTerritoryOrder.class);
    Territory mockTerritory1 = mock(Territory.class);
    Army mockArmy = mock(Army.class);
    when(mockTerritory1.getArmyWithLevel(1)).thenReturn(mockArmy);
    when(mockArmy.getArmyUnits()).thenReturn(3);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder1.getNumUnit()).thenReturn(3);
    when(mockOrder2.getNumUnit()).thenReturn(4);
    when(mockOrder1.getLevel()).thenReturn(1);
    when(mockOrder2.getLevel()).thenReturn(1);

    String res = "The player does not have enough units to upgrade in the current level";
    RuleChecker<OneTerritoryOrder> checker = new UpgradeSufficientUnitChecker(null);
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
  }

}










