package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class VirusOrderTest {
    @Test
    public void test_virusAttack(){
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player1 = new Player(null, null);
        Player player2 = new Player(null, null);
        player1.setTechnologyResourceTotal(1000);
        player2.setTechnologyResourceTotal(500);
        player1.setFoodResourceTotal(2000);
        player2.setFoodResourceTotal(1500);
        gMap.getTerritory("Elantris").setOwner(player1);
        gMap.getTerritory("Narnia").setOwner(player2);
        VirusOrder vOrder = new VirusOrder(player1.getPlayerID(), gMap, player1, "Narnia", 1 );
        vOrder.execute();
        assertEquals(400, player2.getTechnologyResourceTotal());
        assertEquals(1300, player2.getFoodResourceTotal());
        assertEquals(900, player1.getTechnologyResourceTotal());
        assertEquals(2000, player1.getFoodResourceTotal());

        player1.setTechnologyResourceTotal(1000);
        player2.setTechnologyResourceTotal(50);
        player1.setFoodResourceTotal(2000);
        player2.setFoodResourceTotal(15);
        VirusOrder vOrder2 = new VirusOrder(player1.getPlayerID(), gMap, player1, "Narnia", 1 );
        vOrder2.execute();
        assertEquals(0, player2.getTechnologyResourceTotal());
        assertEquals(0, player2.getFoodResourceTotal());
        assertEquals(900, player1.getTechnologyResourceTotal());
        assertEquals(2000, player1.getFoodResourceTotal());
    }
}