package edu.duke.ece651_g10.server;

/**
 * This interface represents the special unit Spy.
 */
public interface Spy {

    /**
     * Get the owner of the spy.
     *
     * @return The owner of the spy.
     */
    public Player getOwner();

    /**
     * Get the current position of the spy.
     *
     * @return The position (territory) of the current spy.
     */
    public Territory getCurrentPosition();


    /**
     * Move to a new position.
     * Warning: This function does not check whether the territory is valid to move.
     *
     * @param territory The target territory.
     */
    public void moveTo(Territory territory);
}
