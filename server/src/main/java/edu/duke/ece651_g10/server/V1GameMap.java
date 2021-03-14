package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;

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
}












