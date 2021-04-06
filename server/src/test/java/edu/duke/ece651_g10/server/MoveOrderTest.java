package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
public class MoveOrderTest {
  @Test
  public void test_MoveOrderExecute() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").getArmyWithLevel(0).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(1).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(2).increaseUnits(2);
    gMap.getTerritory("Elantris").getArmyWithLevel(3).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(4).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(5).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(6).increaseUnits(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    /* assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(0).getArmyUnits());
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(1).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Elantris").getArmyWithLevel(2).getArmyUnits());
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(3).getArmyUnits());
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(4).getArmyUnits());
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(5).getArmyUnits());
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(6).getArmyUnits());

    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(0).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(1).getArmyUnits());
    assertEquals(2, gMap.getTerritory("Roshar").getArmyWithLevel(2).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(3).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(4).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(5).getArmyUnits());
    assertEquals(0, gMap.getTerritory("Roshar").getArmyWithLevel(6).getArmyUnits());*/
    }

  @Test
  public void test_getSourceTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    //gMap.getTerritory("Elantris").increaseUnit(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(0).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(1).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(2).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(3).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(4).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(5).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(6).increaseUnits(5);
     assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(6).getArmyUnits());
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals("Elantris", mOrder.getSourceTerritory().getName());
  }

  @Test
  public void test_getTargetTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    //gMap.getTerritory("Elantris").increaseUnit(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(0).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(1).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(2).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(3).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(4).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(5).increaseUnits(5);
    gMap.getTerritory("Elantris").getArmyWithLevel(6).increaseUnits(5);
    assertEquals(5, gMap.getTerritory("Elantris").getArmyWithLevel(6).getArmyUnits());
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals("Roshar", mOrder.getTargetTerritory().getName());
  }

  @Disabled
  public void test_getNumUnit(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals(3, mOrder.getNumUnit());
    }
}













