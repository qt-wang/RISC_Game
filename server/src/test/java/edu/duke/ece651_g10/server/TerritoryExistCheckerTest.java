package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class TerritoryExistCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    GameMap mockGameMap = mock(GameMap.class);
    TerritoryToTerritoryOrder mockOrder1 = mock(TerritoryToTerritoryOrder.class);
    Player mockPlayer1 = mock(Player.class);

    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);

    when(mockOrder1.getSourceTerritory()).thenReturn(null);
    when(mockOrder1.getTargetTerritory()).thenReturn(null);

    String resSource = "The source territory does not exist.";
    String resTarget = "The target territory does not exist.";
    RuleChecker checker = new TerritoryExistChecker(null);

    assertEquals(resSource, checker.checkMyRule(mockOrder1, mockGameMap));
    ownership.put(mockTerritory1, mockPlayer1);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    assertEquals(resTarget, checker.checkMyRule(mockOrder1, mockGameMap));
    ownership.put(mockTerritory2, mockPlayer1);
        when(mockOrder1.getTargetTerritory()).thenReturn(mockTerritory2);
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}











