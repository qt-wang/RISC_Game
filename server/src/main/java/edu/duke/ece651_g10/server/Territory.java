package edu.duke.ece651_g10.server;

import org.json.JSONObject;

import java.util.Set;

/**
 * The Territory interface.
 */
public interface Territory {
    /**
     * Return the number of units owned in the territory (by the owner).
     *
     * @return The number of units currently in the territory.
     */
    public int getNumUnit();

    /**
     * Add neighbour as its neighbours.
     *
     * @param neighbour The territory to be added as current territory's neighbour.
     *                  If neighbour is already its neighbour, do nothing.
     */
    public void addNeighbour(Territory neighbour);

    /**
     * Set the territory's unit number to unit.
     *
     * @param unit
     */
    public void setUnitNumber(int unit);

    /**
     * Increase the owner's soldier number by unit.
     *
     * @param unit The number of units to increase.
     * @param level The level of the unit.
     */
    public void increaseUnit(int unit, int level);


    /**
     * Decrease the owner's soldier number by unit.
     *
     * @param unit The number of units to decrease.
     * @param level The level to decrease.
     */
    public void decreaseUnit(int unit, int level);

    /**
     * Get all the neighbours of the territory.
     *
     * @return A Set contains all the neighbour territories.
     */
    public Set<Territory> getNeighbours();

    /**
     * Set the owner of the territory to player.
     *
     * @param player The player who will own this land.
     */
    public void setOwner(Player player);

    /**
     * Get the name of the territory
     *
     * @return The name of the territory.
     */
    public String getName();


    /**
     * Get the owner of the territory.
     *
     * @return The owner of the territory.
     */
    public Player getOwner();

    /**
     * Get the territory's food resource generation rate.
     *
     * @return territory's food resource generation rate.
     */
    public int getFoodResourceGenerationRate();

    /**
     * Get the territory's technology resource generation rate.
     *
     * @return territory's technology resource generation rate.
     */
    public int getTechnologyResourceGenerationRate();


    /**
     * Set the territories food resource generation rate.
     *
     * @param rate The rate to generate food resource.
     */
    public void setFoodResourceGenerationRate(int rate);

    /**
     * Set the territory's technology resource generation rate.
     *
     * @param rate The rate to generate technology resource.
     */
    public void setTechnologyResourceGenerationRate(int rate);

    /**
     * Set the size of the current territory.
     */
    public void setSize(int size);

    /**
     * Get the size of current territory.
     *
     * @return The size of current territory.
     */
    public int getSize();


    /**
     * Get how many units in specific level.
     * @param level  The level for units.  level in the range [0, 6]
     * @return The number of units in this level.
     */
    public int getUnitNumber(int level);


    /**
     * Get the army from territory with level specified by "level".
     * @param level The level of army
     * @return  The army object.
     */
    public Army getArmyWithLevel(int level);

    public JSONObject presentTerritoryInformation();
}
