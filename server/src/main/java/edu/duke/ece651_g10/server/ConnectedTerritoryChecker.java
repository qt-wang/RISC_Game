package edu.duke.ece651_g10.server;

import java.util.HashSet;

/**
 * The RuleChecker to check whether two territories connected with each other
 */
public class ConnectedTerritoryChecker extends RuleChecker<Order> {
  final HashSet<Territory> waypoints;

  /**
   * The constructor of the the ConnectedTerritoryChecker
   *
   * @param next The next RuleChecker
   */
  public ConnectedTerritoryChecker(RuleChecker<Order> next) {
    super(next);
    this.waypoints = new HashSet<Territory>();
  }

  /**
   * To search whether the source territory and target territory are connected in
   * the player's territories
   *
   * @param current the current Territory
   * @param target  the target Territory
   * @param selfID  the Player's ID
   * @param gameMap the game map
   * @return if the source and target is connected, then return true, otherwise
   *         return false.
   */
  private boolean searchConnection(Territory current, Territory target, int selfID, GameMap gameMap) {
    if (current.getNeighbours().contains(target)) {
      return true;
    } else {
      waypoints.add(current);
      for (Territory t : current.getNeighbours()) {
        // Also needs to check whether the player own the neighbours
        if (gameMap.getOwnership().get(t).getPlayerID() == selfID && !waypoints.contains(t)) {
          if (searchConnection(t, target, selfID, gameMap)) {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Check whether the order is following the rule defined in the following
   * method.
   * 
   * @param order   the order frome the player
   * @param gameMap the game map of the game
   * @return if the rule is not violated, return null. Otherwise, return the
   *         reason casuing the invalid placement
   */
  @Override
  protected String checkMyRule(Order order, GameMap gameMap) {
    if (!searchConnection(order.getSourceTerritory(), order.getTargetTerritory(), order.getPlayerID(), gameMap)) {
      waypoints.clear();
      return "The source territory and target territory are not connected.";
    }
    waypoints.clear();
    return null;
  }
}







