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

    @Override
    public HashMap<Integer, HashSet<Territory>> getInitialGroups() {
        return initialGroups;
    }


    @Override
    public HashMap<Territory, Player> getOwnership() {
        HashMap<Territory, Player> result = new HashMap<>();
        for (Territory t: territories) {
            result.put(t, t.getOwner());
        }
        return result;
    }

    @Override
    public Territory getTerritory(String name) {
        for (Territory t: territories) {
            if (t.getName().equals(name)) {
                return t;
            }
        }
        return null;
    }


    @Override
    public Player allBelongsToSamePlayer() {
        Player player = null;
        for (Territory t: territories) {
            if (player == null) {
                player = t.getOwner();
            } else {
                if (player != t.getOwner()) {
                    return null;
                }
            }
        }
        return player;
    }

    @Override
    public Set<Territory> getTerritoriesForPlayer(Player p) {
        Set<Territory> result = new HashSet<>();
        for (Territory t: territories) {
            if (t.getOwner()==p) {
                result.add(t);
            }
        }
        return result;
    }
}












