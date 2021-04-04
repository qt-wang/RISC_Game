package edu.duke.ece651_g10.server;

import java.util.HashMap;

public class shortestPath {
    GameMap gMap;
    Territory source;
    Territory target;
    HashMap<Territory, Integer> distanceTable;

    public shortestPath(GameMap gMap){
        this.gMap = gMap;

    }

    /*public int minCost(Territory source, Territory target){

    }*/
}
