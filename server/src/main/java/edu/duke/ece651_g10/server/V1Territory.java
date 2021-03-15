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

    //TODO: Implement
    @Override
    public void setUnitNumber(int unit) {

    }

    //TODO: Implement
    @Override
    public void decreaseUnit(int unit) {

    }

    //TODO: Implement
    @Override
    public void increaseUnit(int unit) {

    }


    private String name;
    private Player owner;
    private HashMap<Player, Army> units;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
    private Army ownedUnits;

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
        return 0;
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













