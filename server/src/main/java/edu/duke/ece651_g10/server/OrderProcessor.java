package edu.duke.ece651_g10.server;

public interface OrderProcessor {
    /**
     * Accept the order from the players.
     * This method should probably be invoked in the server class.
     * This method should also merge the orders if needed.
     * @param order The order to be accepted.
     */
    public void acceptOrder(Order order);

    /**
     * Execute those orders that need to be handled at the end of turn.
     * For version 1, just need to execute the attack order.
     */
    public void executeEndTurnOrders();
}
