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


    private final String name;
    private Player owner;
    private HashMap<Player, Integer> units;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
    private List<Unit> ownedUnits;


    // Version 2 variables.
    private int size;
    private int foodResourceGenerationRate;
    private int technologyResourceGenerationRate;


    @Override
    public void setOwner(Player player) {
        this.owner = player;
    }

    public V1Territory(String name) {
        this.name = name;
        neighbours = new HashSet<>();
        units = new HashMap<>();
        ownedUnits = new LinkedList<>();
        size = 0;
        foodResourceGenerationRate = 0;
        technologyResourceGenerationRate = 0;
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

    @Override
    public int getFoodResourceGenerationRate() {
        return foodResourceGenerationRate;
    }

    @Override
    public int getTechnologyResourceGenerationRate() {
        return technologyResourceGenerationRate;
    }

    @Override
    public void setFoodResourceGenerationRate(int rate) {
        this.foodResourceGenerationRate = rate;
    }

    @Override
    public void setTechnologyResourceGenerationRate(int rate) {
        this.technologyResourceGenerationRate = rate;
    }

    @Override
    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public int getSize() {
        return this.size;
    }
}













