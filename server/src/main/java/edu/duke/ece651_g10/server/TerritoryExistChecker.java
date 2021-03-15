package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the source Territory and target Territory
 * are adjacent to each other
 */
public class TerritoryExistChecker extends RuleChecker {
/**
   * The constructor of the the TerritoryExistChekcer
   *
   * @param next The next RuleChecker
   */
  public TerritoryExistChecker(RuleChecker next) {
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
    if (!gameMap.getOwnership().containsKey(order.getSourceTerritory())) {
      return "The source territory does not exist.";
    } else if (!gameMap.getOwnership().containsKey(order.getTargetTerritory())) {
      return "The target territory does not exist.";
    }
    return null;
  }
}











