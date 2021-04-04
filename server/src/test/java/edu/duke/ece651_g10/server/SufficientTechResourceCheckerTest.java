package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class SufficientTechResourceCheckerTest {
  @Test
  public void test_check_my_rule() {
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
    when(mockPlayer2.getTechnologyLevel()).thenReturn(5);
    when(mockPlayer1.getTechnologyResourceTotal()).thenReturn(300);
    when(mockPlayer2.getTechnologyResourceTotal()).thenReturn(299);
    when(mockOrder1.getPlayer()).thenReturn(mockPlayer1);
    when(mockOrder2.getPlayer()).thenReturn(mockPlayer2);
    when(mockOrder1.getMaxTechLevelTable()).thenReturn(maxTechLevel);
    when(mockOrder2.getMaxTechLevelTable()).thenReturn(maxTechLevel);

    RuleChecker<ZeroTerritoryOrder> checker = new SufficientTechResourceChecker(null);
    String res = "The player does not have enough technology to upgrade technology.";
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
  }

}
