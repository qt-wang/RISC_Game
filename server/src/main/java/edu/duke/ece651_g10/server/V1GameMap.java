package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * An implementation of the game map. Maintained by Guancheng Fu.
 */
public class V1GameMap implements GameMap {

    HashSet<Territory> territories;

    HashMap<Integer, HashSet<Territory>> initialGroups;

    /**
     * Generate a Version 1 game map.
     *
     * @param territories   All the territories within the map board.
     * @param initialGroups Initial groups which can be used to assigned to each player.
     *                      The size of the hashmap should equals to the number of players.
     */
    public V1GameMap(HashSet<Territory> territories, HashMap<Integer, HashSet<Territory>> initialGroups) {
        this.territories = territories;
        this.initialGroups = initialGroups;
    }

    //TODO: Implement
    @Override
    public HashMap<Integer, HashSet<Territory>> getInitialGroups() {
        return initialGroups;
    }


    // TODO: Implement
    @Override
    public HashMap<Territory, Player> getOwnership() {
        return null;
    }

    //TODO: Implement
    @Override
    public Territory getTerritory(String name) {
        return null;
    }

    //TODO: Implement
    @Override
    public Set<Territory> getTerritoriesForPlayer(Player p) {
        return null;
    }
}












