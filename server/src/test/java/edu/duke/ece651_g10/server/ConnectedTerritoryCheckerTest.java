package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class ConnectedTerritoryCheckerTest {
  @Test
  public void test_check_my_rule() {
    // Mock
    GameMap mockGameMap = mock(GameMap.class);
    Order mockOrder1 = mock(Order.class);
    // Order mockOrder2 = mock(Order.class);
    Player mockPlayer1 = mock(Player.class);
    Player mockPlayer2 = mock(Player.class);

    HashMap<Territory, Player> ownership = new HashMap<Territory, Player>();
    Territory mockTerritory1 = mock(Territory.class);
    Territory mockTerritory2 = mock(Territory.class);
    Territory mockTerritory3 = mock(Territory.class);
    Territory mockTerritory4 = mock(Territory.class);
    Territory mockTerritory5 = mock(Territory.class);
    ownership.put(mockTerritory1, mockPlayer1);
    ownership.put(mockTerritory2, mockPlayer2);
    ownership.put(mockTerritory3, mockPlayer1);
    ownership.put(mockTerritory4, mockPlayer1);
    ownership.put(mockTerritory5, mockPlayer1);

    HashSet<Territory> mockTerritorySet1 = new HashSet<Territory>();
    mockTerritorySet1.add(mockTerritory2);
    mockTerritorySet1.add(mockTerritory3);
    when(mockTerritory1.getNeighbours()).thenReturn(mockTerritorySet1);

    HashSet<Territory> mockTerritorySet2 = new HashSet<Territory>();
    mockTerritorySet2.add(mockTerritory1);
    mockTerritorySet2.add(mockTerritory3);
    mockTerritorySet2.add(mockTerritory4);
    when(mockTerritory2.getNeighbours()).thenReturn(mockTerritorySet2);

    HashSet<Territory> mockTerritorySet3 = new HashSet<Territory>();
    mockTerritorySet3.add(mockTerritory1);
    mockTerritorySet3.add(mockTerritory2);
    mockTerritorySet3.add(mockTerritory4);
    when(mockTerritory3.getNeighbours()).thenReturn(mockTerritorySet3);

    HashSet<Territory> mockTerritorySet4 = new HashSet<Territory>();
    mockTerritorySet4.add(mockTerritory2);
    mockTerritorySet4.add(mockTerritory3);
    mockTerritorySet4.add(mockTerritory5);
    when(mockTerritory4.getNeighbours()).thenReturn(mockTerritorySet4);

    HashSet<Territory> mockTerritorySet5 = new HashSet<Territory>();
    mockTerritorySet5.add(mockTerritory4);
    when(mockTerritory5.getNeighbours()).thenReturn(mockTerritorySet5);

    when(mockOrder1.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockOrder1.getTargetTerritory()).thenReturn(mockTerritory5);
    // when(mockOrder2.getSourceTerritory()).thenReturn(mockTerritory1);
    when(mockGameMap.getOwnership()).thenReturn(ownership);
    when(mockPlayer1.getPlayerID()).thenReturn(1);
    when(mockPlayer2.getPlayerID()).thenReturn(2);
    when(mockOrder1.getPlayerID()).thenReturn(1);
    // when(mockOrder2.getPlayerID()).thenReturn(2);

    RuleChecker checker = new ConnectedTerritoryChecker(null);
    String res = "The source territory and target territory are not connected.";
    assertEquals(null, checker.checkMyRule(mockOrder1, mockGameMap));

    ownership.put(mockTerritory2, mockPlayer1);
    ownership.put(mockTerritory4, mockPlayer2);
    assertEquals(res, checker.checkMyRule(mockOrder1, mockGameMap));
  }

}











