package edu.duke.ece651_g10.server;

import java.util.*;
import java.lang.Math;

public class ShortestPath {
    GameMap gMap;

    public ShortestPath(GameMap gMap){
        this.gMap = gMap;
    }

    /**
     * This function calculates the minimum food cost from source territory to target territory
     * when doing move order.
     * @param source this is the source territory
     * @param target this is the target territory
     * @return the minimum food cost from source territory to target territory.
     */
    public int minCost(Territory source, Territory target){
        Player player = source.getOwner();
        HashMap<String, Integer> distanceTable = new HashMap<>();
        HashMap<String, Territory> territoryTable = new HashMap<>();
        Vector<String> visited = new Vector<>();
        Vector<String> unvisited = new Vector<>();
        for(Territory t : gMap.getTerritoriesForPlayer(player)){
            distanceTable.put(t.getName(), Integer.MAX_VALUE);
        }
        distanceTable.put(source.getName(), source.getSize());
        for(Territory t : gMap.getTerritoriesForPlayer(player)){
            unvisited.add(t.getName());
        }
        for(Territory t : gMap.getTerritoriesForPlayer(player)){
            territoryTable.put(t.getName(), t);
        }
        for(Territory t : gMap.getTerritoriesForPlayer(player)){
            territoryTable.put(t.getName(), t);
        }
        int size1 = visited.size();
        visited.add(source.getName());
        int size2 = visited.size();

        while((size2 - size1) != 0) {
            size1 = visited.size();
            String t = visited.lastElement();
            Set<Territory> neigh = territoryTable.get(t).getNeighbours();
            Territory minTerritory = source;
            int shortest = Integer.MAX_VALUE;
            for (Territory n : neigh) {
                if (!visited.contains(n.getName())) {
                    if (shortest > distanceTable.get(t) + n.getSize()) {
                        shortest = distanceTable.get(t) + n.getSize();
                        minTerritory = n;
                    }
                    int minDistance = Math.min(distanceTable.get(n.getName()), distanceTable.get(t) + n.getSize());
                    distanceTable.put(n.getName(), minDistance);
                }
            }
            if(!visited.contains(minTerritory.getName())) {
                visited.add(minTerritory.getName());
            }
            size2 = visited.size();
        }
        return distanceTable.get(target.getName());
    }
}
