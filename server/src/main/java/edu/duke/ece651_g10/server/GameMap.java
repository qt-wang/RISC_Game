package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Map indicates the play board of the game. Maintained by Guancheng Fu
 */
public interface GameMap {


    /**
     * If there are 3 players, this should return:
     * 3 groups of territories for each player to select.
     *
     * @return A HashMap to represent three groups of territories to be assigned.
     */
    HashMap<Integer, HashSet<Territory>> getInitialGroups();

    /**
     * For each territory, record its ownership within the HashMap.
     * @return A HashMap contains the ownership of the territories.
     */
    public HashMap<Territory, Player> getOwnership();

    /**
     *
     * @param name The territory's name
     * @return The territory with this name.
     * @return null If the territory is not existed.
     */
    public Territory getTerritory(String name);

    /**
     * Give the player, return a set of territories belong to this player.
     * @param p The player for looking up.
     * @return A set of territories belong to this user.
     */
    public Set<Territory> getTerritoriesForPlayer(Player p);
}













