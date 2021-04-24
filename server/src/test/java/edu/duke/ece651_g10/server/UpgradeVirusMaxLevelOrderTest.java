package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UpgradeVirusMaxLevelOrderTest {
    @Test
    public void test_upgradeMaxVirusLevel(){
        FixedGameMapFactory factory = new FixedGameMapFactory();
        GameMap gMap = factory.createGameMap(3);
        Player player = new Player(null, null);
        player.setTechnologyResourceTotal(1000);
        assertEquals(1, player.getVirusMaxLevel());
        UpgradeVirusMaxLevelOrder uOrder = new UpgradeVirusMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder.execute();
        assertEquals(2, player.getVirusMaxLevel());
        assertEquals(970, player.getTechnologyResourceTotal());
        UpgradeVirusMaxLevelOrder uOrder2 = new UpgradeVirusMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder2.execute();
        assertEquals(3, player.getVirusMaxLevel());
        assertEquals(910, player.getTechnologyResourceTotal());
        UpgradeVirusMaxLevelOrder uOrder3 = new UpgradeVirusMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder3.execute();
        assertEquals(4, player.getVirusMaxLevel());
        assertEquals(800, player.getTechnologyResourceTotal());
        UpgradeVirusMaxLevelOrder uOrder4 = new UpgradeVirusMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder4.execute();
        assertEquals(5, player.getVirusMaxLevel());
        assertEquals(580, player.getTechnologyResourceTotal());
        UpgradeVirusMaxLevelOrder uOrder5 = new UpgradeVirusMaxLevelOrder(player.getPlayerID(), gMap, player);
        uOrder5.execute();
        assertEquals(6, player.getVirusMaxLevel());
        assertEquals(250, player.getTechnologyResourceTotal());
    }
}