package edu.duke.ece651_g10.server;

/**
 * A simple wait group, which has the following features:
 * It has a inner count, which is set to the number of players initially.
 * If the player choose to commit, then call the decrease function.
 * If the player choose to logout, then call the increase function.
 * If the player choose to login, then call the decrease function.
 * The entire class should be thread-safe.
 */
public class WaitGroup {

    int count;


    /**
     * Construct a WaitGroup object.
     * @param count The initial value given to count.
     */
    public WaitGroup(int count) {
        this.count = count;
    }


    /**
     * Increase the inner count.
     */
    public synchronized void increase() {
        this.count += 1;
    }

    /**
     * Decrease the inner count.
     */
    public synchronized void decrease() {
        this.count -= 1;
    }

    /**
     * check if all the players are ready.
     * If the inner count is 0, which indicates that all the player is ready.
     * If the inner count is not 0, then the players can free to logout.
     * @return  True if all the players are ready.
     */
    public synchronized boolean getState() {
        return this.count == 0;
    }
}
