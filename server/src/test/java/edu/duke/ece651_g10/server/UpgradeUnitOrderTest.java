package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class UpgradeUnitOrderTest {
  @Test
  public void test_upgradeUnit() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    Player player = new Player(null, null);
  }

}
