package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;

public class V1OrderProcessorTest {
  @Disabled
  public void test_acceptOrder() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    gMap.getTerritory("Roshar").increaseUnit(4, 6);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    AttackOrder aOrder = new AttackOrder(1, "Elantris", "Narnia", 1, gMap, player);
    AttackOrder aOrder2 = new AttackOrder(1, "Elantris", "Scadrial", 1, gMap, player);
    V1OrderProcessor op = new V1OrderProcessor();
    op.acceptOrder(mOrder);
    op.acceptOrder(aOrder);
    op.acceptOrder(aOrder2);
    op.executeEndTurnOrders();
    assertEquals(7, gMap.getTerritory("Roshar").getNumUnit());
    assertEquals(4, gMap.getTerritory("Roshar").getArmyWithLevel(6).getArmyUnits());
    assertEquals(3, gMap.getTerritory("Roshar").getArmyWithLevel(0).getArmyUnits());
    assertEquals(1, gMap.getTerritory("Narnia").getNumUnit());
    assertEquals(1, gMap.getTerritory("Scadrial").getNumUnit());
    assertEquals(0, gMap.getTerritory("Elantris").getNumUnit());

  }

  @Test
  public void test_attackLevel(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    gMap.getTerritory("Elantris").increaseUnit(2, 3);
    AttackOrder aOrder = new AttackOrder(1, "Elantris", "Narnia", 7, gMap, player);
    ArrayList<Integer> arr = new ArrayList<>(Arrays.asList(5, 0, 0, 2, 0, 0, 0));
    assertEquals(5, aOrder.getSourceTerritory().getArmyWithLevel(0).getArmyUnits());
    assertEquals(2, aOrder.getSourceTerritory().getArmyWithLevel(3).getArmyUnits());
    assertEquals(arr, aOrder.getAttackLevel());
  }

  @Test
  public void test_merge(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player = new Player(null, null);
    gMap.getTerritory("Elantris").increaseUnit(5, 0);
    gMap.getTerritory("Elantris").increaseUnit(2, 3);
    gMap.getTerritory("Roshar").increaseUnit(6, 1);
    gMap.getTerritory("Roshar").increaseUnit(3, 4);
    AttackOrder aOrder1 = new AttackOrder(1, "Elantris", "Scadrial", 3, gMap, player);
    AttackOrder aOrder2 = new AttackOrder(1, "Roshar", "Scadrial", 3, gMap, player);
    V1OrderProcessor op = new V1OrderProcessor();
    op.acceptOrder(aOrder1);
    op.acceptOrder(aOrder2);
    op.executeEndTurnOrders();
    assertEquals(6, gMap.getTerritory("Scadrial").getNumUnit());
  }

  @Test
  public void test_1v0(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    Player player1 = new Player(null, null);
    Player player2 = new Player(null, null);
    gMap.getTerritory("Elantris").setOwner(player1);
    gMap.getTerritory("Narnia").setOwner(player2);
    gMap.getTerritory("Elantris").increaseUnit(1, 0);
    gMap.getTerritory("Narnia").increaseUnit(1, 6);
    AttackOrder aOrder1 = new AttackOrder(1, "Elantris", "Narnia", 1, gMap, player1);
    AttackOrder aOrder2 = new AttackOrder(1, "Narnia", "Elantris", 1, gMap, player2);
    V1OrderProcessor op = new V1OrderProcessor();
    op.acceptOrder(aOrder1);
    op.acceptOrder(aOrder2);
    op.executeEndTurnOrders();
    assertEquals(player2.getPlayerID(), gMap.getTerritory("Elantris").getOwner().getPlayerID());
    assertEquals(player1.getPlayerID(), gMap.getTerritory("Narnia").getOwner().getPlayerID());
  }
}











