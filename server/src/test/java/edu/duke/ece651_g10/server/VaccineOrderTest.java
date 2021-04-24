package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VaccineOrderTest {
    @Test
    public void test_vaccine(){
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player = new Player(null, null);
        player.setTechnologyResourceTotal(1000);
        assertEquals(0, player.getVaccineLevel());
        VaccineOrder vOrder = new VaccineOrder(player.getPlayerID(), gMap, player, 1);
        vOrder.execute();
        assertEquals(1, player.getVaccineLevel());
    }

}