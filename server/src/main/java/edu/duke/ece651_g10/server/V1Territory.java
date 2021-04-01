package edu.duke.ece651_g10.server;

import java.util.*;

/**
 * This class represents one territory of the map.
 * Maintained by Guancheng Fu.
 */
public class V1Territory implements Territory {


    @Override
    public void setUnitNumber(int unit) {
        ownedUnits = new LinkedList<>();
        for (int i = 0; i < unit; i ++) {
            ownedUnits.add(new V1Unit());
        }
    }


    @Override
    public void decreaseUnit(int unit) {
        for (int i = 0; i < unit; i ++) {
            ownedUnits.remove(0);
        }
    }


    @Override
    public void increaseUnit(int unit) {
        for (int i = 0; i < unit; i ++) {
            ownedUnits.add(new V1Unit());
        }
    }


    private String name;
    private Player owner;
    private HashMap<Player, Integer> units;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
    private List<Unit> ownedUnits;

    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    public V1Territory(String name) {
        this.name = name;
        neighbours = new HashSet<>();
        units = new HashMap<>();
        ownedUnits = new LinkedList<>();
    }


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int getNumUnit() {
        return ownedUnits.size();
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













