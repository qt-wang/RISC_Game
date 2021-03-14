package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class SufficientUnitCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    Order mockOrder1 = mock(Order.class);
    Order mockOrder2 = mock(Order.class);
    Territory mockTerritory = mock(Territory.class);
    when(mockOrder1.getNumUnit()).thenReturn(3);
    when(mockOrder2.getNumUnit()).thenReturn(5);
    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory);
    when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory);
    when(mockTerritory.getNumUnit()).thenReturn(4);

    RuleChecker checker = new SufficientUnitChecker(null);
    String res = "The territory does not have enough units.";
    assertEquals(null, checker.checkMyRule(mockOrder1, null));
    assertEquals(res, checker.checkMyRule(mockOrder2, null));
  }

}











