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
     *
     * @return A HashMap contains the ownership of the territories.
     */
    public HashMap<Territory, Player> getOwnership();

    /**
     * Check if all the territories in the map all belong to the same player.
     *
     * @return The player if all the territory belong to the same player.
     * @return null Otherwise.
     */
    public Player allBelongsToSamePlayer();

    /**
     * @param name The territory's name
     * @return null If the territory is not existed.
     */
    public Territory getTerritory(String name);

    /**
     * Add one unit to each territory.
     */
    public void addUnitToEachTerritory();

    /**
     * Give the player, return a set of territories belong to this player.
     *
     * @param p The player for looking up.
     * @return A set of territories belong to this user.
     */
    public Set<Territory> getTerritoriesForPlayer(Player p);


    /**
     * Get all the territories that do not belong to player p.
     * @param p  The player that is used to test.
     * @return   All the territories that do not belong to player p.
     */
    public Set<Territory> getTerritoriesNotBelongToPlayer(Player p);

    /**
     * Get the number of total players allowed in this game map.
     * @return
     */
    public int getTotalPlayers();

    public int getNumberOfTerritoriesPerPlayer();


    /**
     * Update each player's resource based on the ownership.
     */
    public void updatePlayerResource();

}













