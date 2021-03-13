package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;

public class SufficientUnitCheckerTest {
  @Test
  public void test_check_my_rule() {
    Order mockOrder = mock(Order.class);
    RuleChecker checker = new SufficientUnitChecker(null);
    when(mockOrder.getNumUnit()).thenReturn(1);
    when(mockOrder.getSourceTerritory().getNumUnit()).thenReturn(4);
    String res = checker.checkMyRule(mockOrder, null);
  }

}











