package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;

/**
 * Map indicates the play board of the game.
 * Maintained by Guancheng Fu
 */
public interface GameMap {
    /**
     * If there are 3 players, this should return:
     * 3 groups of territories for each player to select.
     * @return A HashMap to represent three groups of territories to be assigned.
     */
    HashMap<Integer, HashSet<Territory>> getInitialGroups();
}
