package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class MoveOrderTest {
  @Test
  public void test_MoveOrderExecute() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap);
    mOrder.execute();
    assertEquals(2, gMap.getTerritory("Elantris").getNumUnit());
  }

  @Test
  public void test_getSourceTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap);
    mOrder.execute();
    assertEquals("Elantris", mOrder.getSourceTerritory().getName());
  }

  @Test
  public void test_getTargetTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap);
    mOrder.execute();
    assertEquals("Roshar", mOrder.getTargetTerritory().getName());
  }

  @Test
  public void test_getNumUnit(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3, 3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap);
    mOrder.execute();
    assertEquals(3, mOrder.getNumUnit());
  }
}











