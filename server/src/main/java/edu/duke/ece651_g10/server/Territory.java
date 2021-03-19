package edu.duke.ece651_g10.server;

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
     * @param unit
     */
    public void setUnitNumber(int unit);

    /**
     * Increase the owner's soldier number by unit.
     *
     * @param unit The number of units to increase.
     */
    public void increaseUnit(int unit);


    /**
     * Decrease the owner's soldier number by unit.
     *
     * @param unit The number of units to decrease.
     */
    public void decreaseUnit(int unit);

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
}
