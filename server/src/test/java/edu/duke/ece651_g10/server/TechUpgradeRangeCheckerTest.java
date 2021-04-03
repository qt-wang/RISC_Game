package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class TechUpgradeRangeCheckerTest {
  @Test
  public void test_check_may_rule() {
    // Mock
    ZeroTerritoryOrder mockOrder1 = mock(ZeroTerritoryOrder.class);
    ZeroTerritoryOrder mockOrder2 = mock(ZeroTerritoryOrder.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);
    HashMap<Integer, Integer> maxTechLevel = new HashMap<Integer, Integer>();
    maxTechLevel.put(1, 50);
    maxTechLevel.put(2, 75);
    maxTechLevel.put(3, 125);
    maxTechLevel.put(4, 200);
    maxTechLevel.put(5, 300);
    when(mockPlayer1.getTechnologyLevel()).thenReturn(5);
    when(mockPlayer2.getTechnologyLevel()).thenReturn(6);
    when(mockOrder1.getPlayer()).thenReturn(mockPlayer1);
    when(mockOrder2.getPlayer()).thenReturn(mockPlayer2);
    when(mockOrder1.getMaxTechLevel()).thenReturn(maxTechLevel);
    when(mockOrder2.getMaxTechLevel()).thenReturn(maxTechLevel);

    RuleChecker<ZeroTerritoryOrder> checker = new TechUpgradeRangeChecker(null);
    String res = "The player current technology level is not in upgrade range";
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
  }

}
