package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;

public class MoveFoodCheckerTest {
  @Test
  public void test_check_my_rule() {
    Player mockPlayer = mock(Player.class);
    when(mockPlayer.getFoodResourceTotal()).thenReturn(55);
    FixedGameMapFactory factory = new FixedGameMapFactory();
    GameMap gameMap = factory.createGameMap(3, 3);
    gameMap.getTerritory("Narnia").setOwner(mockPlayer);
    gameMap.getTerritory("Oz").setOwner(mockPlayer);
    gameMap.getTerritory("Elantris").setOwner(mockPlayer);
    gameMap.getTerritory("Roshar").setOwner(mockPlayer);
    gameMap.getTerritory("Midkemia").setOwner(mockPlayer);
    gameMap.getTerritory("Scadrial").setOwner(mockPlayer);
    gameMap.getTerritory("Gondor").setOwner(mockPlayer);
    gameMap.getTerritory("Mordor").setOwner(mockPlayer);
    gameMap.getTerritory("Hogwarts").setOwner(mockPlayer);
    TerritoryToTerritoryOrder order1 = new MoveOrder(0, "Narnia", "Oz", 5, gameMap, mockPlayer);
    TerritoryToTerritoryOrder order2 = new MoveOrder(0, "Elantris", "Roshar", 5, gameMap, mockPlayer);
    RuleChecker<TerritoryToTerritoryOrder> checker = new MoveFoodChecker(null);
    assertEquals(null, checker.checkMyRule(order2, gameMap));
    String res = "The player does not have enough food resource to execute the move order";
    assertEquals(res, checker.checkMyRule(order1, gameMap));
  }

}











