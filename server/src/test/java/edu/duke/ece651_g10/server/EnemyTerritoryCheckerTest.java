package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class EnemyTerritoryCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    GameMap mockGameMap = mock(GameMap.class);
    TerritoryToTerritoryOrder mockOrder1 = mock(TerritoryToTerritoryOrder.class);
    TerritoryToTerritoryOrder mockOrder2 = mock(TerritoryToTerritoryOrder.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);
    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);
    ownership.put(mockTerritory1, mockPlayer1);
    ownership.put(mockTerritory2, mockPlayer2);
    when(mockOrder1.getTargetTerritory()).thenReturn(mockTerritory2);
    when(mockOrder2.getTargetTerritory()).thenReturn(mockTerritory2);
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    when(mockPlayer1.getPlayerID()).thenReturn(1);
    when(mockPlayer2.getPlayerID()).thenReturn(2);
    when(mockOrder1.getPlayerID()).thenReturn(1);
    when(mockOrder2.getPlayerID()).thenReturn(2);

    String res = "The target territory does not belong to the enemy.";
    RuleChecker<TerritoryToTerritoryOrder> checker = new EnemyTerritoryChecker(null);
    assertEquals(res, checker.checkMyRule(mockOrder2, mockGameMap));
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}
