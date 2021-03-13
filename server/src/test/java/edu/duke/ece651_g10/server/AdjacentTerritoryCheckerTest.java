package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class AdjacentTerritoryCheckerTest {
  @Test
  public void test_check_order() {
    // Mock
    Order mockOrder1 = mock(Order.class);
    Order mockOrder2 = mock(Order.class);
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);
    Territory mockTerritory3 = mock(Territory.class);
    HashSet<Territory> mockTerritorySet = new HashSet<Territory>();
    mockTerritorySet.add(mockTerritory1);
    mockTerritorySet.add(mockTerritory2);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory2);
    when(mockOrder1.getTargetTerritory()).thenReturn(mockTerritory2);
    when(mockOrder2.getTargetTerritory()).thenReturn(mockTerritory3);
    when(mockTerritory1.getNeighbours()).thenReturn(mockTerritorySet);
    when(mockTerritory2.getNeighbours()).thenReturn(mockTerritorySet);

    when(mockOrder1.getNumUnit()).thenReturn(3);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockTerritory1.getNumUnit()).thenReturn(4);

    RuleChecker checker1 = new SufficientUnitChecker(null);
    RuleChecker checker2 = new AdjacentTerritoryChecker(checker1);
    String res = "The territories are not adjacent to each other.";
    assertEquals(res, checker2.checkOrder(mockOrder2, null));
    assertEquals(null, checker2.checkOrder(mockOrder1, null));
  }

}












