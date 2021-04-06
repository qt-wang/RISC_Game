package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class V1OrderProcessorTest {
  @Disabled
  public void test_acceptOrder() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    AttackOrder aOrder = new AttackOrder(1, "Elantris", "Narnia", 1, gMap, player);
    AttackOrder aOrder2 = new AttackOrder(1, "Elantris", "Scadrial", 1, gMap, player);
    V1OrderProcessor op = new V1OrderProcessor();
    op.acceptOrder(mOrder);
    op.acceptOrder(aOrder);
    op.acceptOrder(aOrder2);
    op.executeEndTurnOrders();
    assertEquals(3, gMap.getTerritory("Roshar").getNumUnit());
    assertEquals(0, gMap.getTerritory("Elantris").getNumUnit());

  }

}











