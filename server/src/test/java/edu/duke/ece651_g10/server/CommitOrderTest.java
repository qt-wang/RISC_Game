package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class CommitOrderTest {
  @Test
  public void test_commitOrder() {
    CommitOrder commit = new CommitOrder(1);
    assertEquals(0, commit.getNumUnit());
    assertEquals(0, commit.getPlayerID());
    assertEquals(null, commit.getSourceTerritory());
    assertEquals(null, commit.getTargetTerritory());

  }

}









