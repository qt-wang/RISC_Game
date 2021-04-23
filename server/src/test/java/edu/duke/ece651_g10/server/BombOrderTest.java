package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class BombOrderTest {
    @Test
    public void test_bomb(){
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player mockPlayer1 = mock(Player.class);
        Player mockPlayer2 = mock(Player.class);
        gMap.getTerritory("Elantris").setOwner(mockPlayer1);
        gMap.getTerritory("Narnia").setOwner(mockPlayer2);
        gMap.getTerritory("Elantris").increaseUnit(3, 4);
        gMap.getTerritory("Narnia").increaseUnit(5, 0);
        gMap.getTerritory("Narnia").increaseUnit(6, 3);
        assertEquals(5, gMap.getTerritory("Narnia").getArmyWithLevel(0).getArmyUnits());
        assertEquals(6, gMap.getTerritory("Narnia").getArmyWithLevel(3).getArmyUnits());
        BombOrder bOrder = new BombOrder(mockPlayer1.getPlayerID(), "Narnia", gMap, mockPlayer1);
        assertEquals(mockPlayer2.getPlayerID(), gMap.getTerritory("Narnia").getOwner().getPlayerID());
        assertEquals(0,  bOrder.getNumUnit());
        bOrder.execute();
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(0).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(1).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(2).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(3).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(4).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(5).getArmyUnits());
        assertEquals(0, gMap.getTerritory("Narnia").getArmyWithLevel(6).getArmyUnits());
        assertEquals(mockPlayer1.getPlayerID(), gMap.getTerritory("Narnia").getOwner().getPlayerID());
        assertEquals(gMap.getTerritory("Narnia"), bOrder.getSourceTerritory());
        assertEquals(0, bOrder.getLevel());
        bOrder.addUnits(1);
    }
}












