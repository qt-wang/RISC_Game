package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class AttackOrderTest {
  @Test
  public void test_execute() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
   
    gMap.getTerritory("Elantris").increaseUnit(1);
    AttackOrder mOrder = new AttackOrder(1, "Elantris", "Narnia", 1, gMap, player);
    assertEquals(1, mOrder.getPlayerID());
    assertEquals(1, mOrder.getNumUnit());
    mOrder.execute();
    assertEquals(1, gMap.getTerritory("Narnia").getNumUnit());
    AttackOrder mOrder2 = new AttackOrder(1, "Narnia", "Elantris", 1, gMap, player);
    mOrder2.execute();
    assertEquals(1, gMap.getTerritory("Elantris").getNumUnit());

    gMap.getTerritory("Scadrial").increaseUnit(2);
    AttackOrder mOrder3 = new AttackOrder(1, "Scadrial", "Elantris", 1, gMap, player);
    mOrder3.addUnits(1);
    assertEquals(2, mOrder3.getNumUnit());
    
    
    /* AttackOrder mOrder2 = new AttackOrder(1, "Elantris", "Narnia", 1, gMap, player);
    mOrder2.execute();
    assertEquals(gMap.getTerritory("Elantris").getNumUnit(), gMap.getTerritory("Elantris").getNumUnit());*/
  }

  @Test
  public void test_getSourceTerritory() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5);
    AttackOrder mOrder = new AttackOrder(1, "Elantris", "Narnia", 3, gMap, player);
    mOrder.execute();
    assertEquals("Elantris", mOrder.getSourceTerritory().getName());
  }

   @Test
  public void test_getTargetTerritory() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5);
    AttackOrder mOrder = new AttackOrder(1, "Elantris", "Narnia", 3, gMap, player);
    mOrder.execute();
    assertEquals("Narnia", mOrder.getTargetTerritory().getName());
  }

}








