package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class UnitUpgradeTechCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    GameMap mockGameMap = mock(GameMap.class);
    OneTerritoryOrder mockOrder1 = mock(OneTerritoryOrder.class);
    OneTerritoryOrder mockOrder2 = mock(OneTerritoryOrder.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);
    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);
    ownership.put(mockTerritory1, mockPlayer1);
    ownership.put(mockTerritory2, mockPlayer2);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory2);
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    when(mockPlayer1.getTechnologyLevel()).thenReturn(1);
    when(mockPlayer2.getTechnologyLevel()).thenReturn(4);
    when(mockOrder1.getLevel()).thenReturn(0);
    when(mockOrder2.getLevel()).thenReturn(4);

    String res = "The player's technology level is not high enough to upgrade the units";
    RuleChecker<OneTerritoryOrder> checker = new UnitUpgradeTechChecker(null);
    assertEquals(res, checker.checkMyRule(mockOrder2, mockGameMap));
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}
