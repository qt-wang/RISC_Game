package edu.duke.ece651_g10.server;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class SelfTerritoryCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    GameMap mockGameMap = mock(GameMap.class);
    Order mockOrder1 = mock(Order.class);
    Order mockOrder2 = mock(Order.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);
    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);
    ownership.put(mockTerritory1, mockPlayer1);
    ownership.put(mockTerritory2, mockPlayer2);
    when(mockOrder1.getTargetTerritory()).thenReturn(mockTerritory1);
    when(mockOrder2.getTargetTerritory()).thenReturn(mockTerritory2);
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    when(mockPlayer1.getPlayerID()).thenReturn(1);
    when(mockPlayer2.getPlayerID()).thenReturn(2);
    when(mockOrder1.getPlayerID()).thenReturn(1);
    when(mockOrder2.getPlayerID()).thenReturn(3);

    String res = "The target territory does not belong to the player.";
    RuleChecker checker = new SelfTerritoryChecker(null);
    assertEquals(res, checker.checkMyRule(mockOrder2, mockGameMap));
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}











