package edu.duke.ece651_g10.server;

import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class FixedGameMapFactoryTest {

    @Test
    public void test_get_territories() {
        HashMap<String, Territory> territoryHashMap = new FixedGameMapFactory().createTerritories(4);
        assertEquals(4, territoryHashMap.size());
        assertEquals(true, territoryHashMap.containsKey("A"));
        assertEquals(true, territoryHashMap.containsKey("D"));
        assertEquals(false, territoryHashMap.containsKey("E"));
    }

    @Test
    public void test_create_groups() {
        HashMap<String, Territory> territoryHashMap = new FixedGameMapFactory().createTerritories(4);
        HashMap<Integer, HashSet<Territory>> groups = new FixedGameMapFactory().createGroups(4, territoryHashMap);

        HashSet<Territory> group = groups.get(1);
        assertEquals(true, group.contains(territoryHashMap.get("A")));
        assertEquals(true, group.contains(territoryHashMap.get("B")));
        assertEquals(true, group.contains(territoryHashMap.get("C")));

        group = groups.get(4);
        assertEquals(true, group.contains(territoryHashMap.get("G")));
        assertEquals(true, group.contains(territoryHashMap.get("H")));
        assertEquals(true, group.contains(territoryHashMap.get("I")));
    }

    @Test
    public void test_five_people_map() {
        GameMapFactory factory = new FixedGameMapFactory();
        GameMap map = factory.createGameMap(5,3);
    }
}