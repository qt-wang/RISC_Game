package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeVaccineMaxLevelOrderTest {
    @Test
    public void test_upgradeVaccine(){
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player = new Player(null, null);
        player.setTechnologyResourceTotal(1000);
        assertEquals(1, player.getVaccineMaxLevel());
        UpgradeVaccineMaxLevelOrder uOrder1 = new UpgradeVaccineMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder1.execute();
        assertEquals(2, player.getVaccineMaxLevel());
        assertEquals(940, player.getTechnologyResourceTotal());
        UpgradeVaccineMaxLevelOrder uOrder2 = new UpgradeVaccineMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder2.execute();
        assertEquals(3, player.getVaccineMaxLevel());
        assertEquals(840, player.getTechnologyResourceTotal());
        UpgradeVaccineMaxLevelOrder uOrder3 = new UpgradeVaccineMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder3.execute();
        assertEquals(4, player.getVaccineMaxLevel());
        assertEquals(670, player.getTechnologyResourceTotal());
        UpgradeVaccineMaxLevelOrder uOrder4 = new UpgradeVaccineMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder4.execute();
        assertEquals(5, player.getVaccineMaxLevel());
        assertEquals(420, player.getTechnologyResourceTotal());
        UpgradeVaccineMaxLevelOrder uOrder5 = new UpgradeVaccineMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder5.execute();
        assertEquals(6, player.getVaccineMaxLevel());
        assertEquals(80, player.getTechnologyResourceTotal());
    }
}