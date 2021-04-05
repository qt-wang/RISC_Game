package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the player has enough food to execute the
 * move order
 */
public class MoveFoodChecker extends RuleChecker<TerritoryToTerritoryOrder> {
  /**
   * The constructor of the the MoveFoodChecker
   *
   * @param next The next RuleChecker
   */
  public MoveFoodChecker(RuleChecker<TerritoryToTerritoryOrder> next) {
    super(next);
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
  protected String checkMyRule(TerritoryToTerritoryOrder order, GameMap gameMap) {
    ShortestPath shortestPath = new ShortestPath(gameMap);
    if (shortestPath.minCost(order.getSourceTerritory(), order.getTargetTerritory()) * order.getNumUnit() > gameMap.getOwnership()
        .get(order.getSourceTerritory()).getFoodResourceTotal()) {
      return "The player does not have enough food resource to execute the move order";
    }
    return null;
  }
}








