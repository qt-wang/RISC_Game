package edu.duke.ece651_g10.server;

/**
 * The RuleChekcer to check whether the source Territory and target Territory
 * are adjacent to each other
 */
public class AdjacentTerritoryChecker extends RuleChecker {

  /**
   * The constructor of the the AdjacentTerritoryChecker
   *
   * @param next The next RuleChecker
   */
  public AdjacentTerritoryChecker(RuleChecker next) {
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
  protected String checkMyRule(Order order, GameMap gameMap) {
    if (!order.getSourceTerritory().getNeighbours().contains(order.getTargetTerritory())) {
      return "The territories are not adjacent to each other.";
    }
    return null;
  }
}











