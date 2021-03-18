package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This class represents one territory of the map.
 * Maintained by Guancheng Fu.
 */
public class V1Territory implements Territory {


    @Override
    public void setUnitNumber(int unit) {
        this.ownedUnits = unit;
    }


    @Override
    public void decreaseUnit(int unit) {
        this.ownedUnits -= unit;
    }


    @Override
    public void increaseUnit(int unit) {
        this.ownedUnits += unit;
    }


    private String name;
    private Player owner;
    private HashMap<Player, Integer> units;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
    private int ownedUnits;

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    //TODO:Change this later.
    public V1Territory(String name) {
        this.name = name;
        neighbours = new HashSet<>();
        units = new HashMap<>();
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumUnit() {
        return ownedUnits;
    }

    @Override
    public Set<Territory> getNeighbours() {
        return neighbours;
    }

    @Override
    public void addNeighbour(Territory neighbour) {
        if (!neighbours.contains(neighbour)) {
            neighbours.add(neighbour);
        }
    }

    @Override
    public Player getOwner(){
        return owner;
    }
}













