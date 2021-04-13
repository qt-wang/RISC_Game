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

    int totalPlayers;

    int territoriesPerPlayer;
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
        this.totalPlayers = initialGroups.size();
        if (totalPlayers == 0) {
            territoriesPerPlayer = 0;
        } else {
            this.territoriesPerPlayer = initialGroups.get(1).size();
        }
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
    public void addUnitToEachTerritory() {
        for (Territory t: territories) {
            t.increaseUnit(1, 0);
        }
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

    @Override
    public int getTotalPlayers() {
        return totalPlayers;
    }

    @Override
    public int getNumberOfTerritoriesPerPlayer() {
        return territoriesPerPlayer;
    }

    @Override
    public void updatePlayerResource() {
        for (Territory t: territories) {
            Player p = t.getOwner();
            p.setFoodResourceTotal(p.getFoodResourceTotal() + t.getFoodResourceGenerationRate());
            p.setTechnologyResourceTotal(p.getTechnologyResourceTotal() + t.getTechnologyResourceGenerationRate());
        }
    }

    @Override
    public void decreaseCloakLastTime() {
        for (Territory t: territories) {
            t.decreaseCloakLastTime();
        }
    }

    //TODO:Implement
    /**
     * Check if the territory is the neighbor land to player p.
     * @param p    The checked player.
     * @param territory   The checked territory.
     * @return True if the territory is the neighbor land of player p.
     */
    boolean isNeighborToTerritory(Player p, Territory territory) {
        for (Territory t: getTerritoriesForPlayer(p)) {
            if (t.getNeighbours().contains(territory)) {
                return true;
            }
        }
        return false;
    }

    //TODO:Implement, test and think again.
    @Override
    public boolean visibleToPlayer(Player p, Territory territory) {
        if (territory.getOwner() == p) {
            return true;
        }
        if (territory.spyInTerritory(p)) {
            return true;
        }
        if (isNeighborToTerritory(p, territory)) {
            if (territory.isHidden()) {
                if (territory.getOldView(p) != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            if (territory.getOldView(p) != null) {
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public Set<Territory> getTerritoriesNotBelongToPlayer(Player p) {
        Set<Territory> result = new HashSet<>();
        for (Territory t: territories) {
            if (t.getOwner()!=p) {
                result.add(t);
            }
        }
        return result;
    }
}












