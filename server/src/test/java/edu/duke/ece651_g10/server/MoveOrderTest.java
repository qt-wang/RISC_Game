package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.junit.jupiter.api.Test;
public class MoveOrderTest {
  @Test
  public void test_MoveOrderExecute() {
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals(2, gMap.getTerritory("Elantris").getNumUnit());
  }

  @Test
  public void test_getSourceTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals("Elantris", mOrder.getSourceTerritory().getName());
  }

  @Test
  public void test_getTargetTerritory(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals("Roshar", mOrder.getTargetTerritory().getName());
  }

  @Test
  public void test_getNumUnit(){
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gMap = factory.createGameMap(3);
    gMap.getTerritory("Elantris").increaseUnit(5);
    Player p = mock(Player.class);
    MoveOrder mOrder = new MoveOrder(1, "Elantris", "Roshar", 3, gMap, p);
    mOrder.execute();
    assertEquals(3, mOrder.getNumUnit());
  }
}











