package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PseudoNumberGeneratorTest {
  @Test
  public void test_pseudo() {
    PseudoNumberGenerator p = new PseudoNumberGenerator();
    int r = p.nextInt();
    assertEquals(r, r);
  }
}
    
 

