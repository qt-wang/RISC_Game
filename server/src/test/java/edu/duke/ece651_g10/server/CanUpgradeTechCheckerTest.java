package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class CanUpgradeTechCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    ZeroTerritoryOrder mockOrder1 = mock(ZeroTerritoryOrder.class);
    ZeroTerritoryOrder mockOrder2 = mock(ZeroTerritoryOrder.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);
    when(mockPlayer1.getCanUpgradeInThisTurn()).thenReturn(true);
    when(mockPlayer2.getCanUpgradeInThisTurn()).thenReturn(false);
    when(mockOrder1.getPlayer()).thenReturn(mockPlayer1);
    when(mockOrder2.getPlayer()).thenReturn(mockPlayer2);

    RuleChecker<ZeroTerritoryOrder> checker = new CanUpgradeTechChecker(null);
    String res ="The player can not upgrade in this turn.";
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
  }

}




