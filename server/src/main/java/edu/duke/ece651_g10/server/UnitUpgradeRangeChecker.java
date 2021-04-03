package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer's units are in upgrade range
 */
public class UnitUpgradeRangeChecker extends RuleChecker<OneTerritoryOrder> {
/**
   * The constructor of the UnitUpgradeRangeChecker
   * 
   * @param next The next RuleChecker
   */
  public UnitUpgradeRangeChecker(RuleChecker<OneTerritoryOrder> next) {
    super(next);
  }

  /**
   * Check whether the order is following the rule defined in the following
   * method.
   * 
   * @param order   the order frome the player
   * @param gameMap the game map of the game
   * @return if the rule is not violated, return null. Otherwise, return the
   *         reason causing the invalid placement
   */
  @Override
  protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
    if (!order.getUnitUpgradeTable().containsKey(order.getLevel())) {
      return "The player's upgrade level is not in the unit upgrade range.";
    }
    return null;
  }
}









