package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the technology upgrade is in the range
 */
public class TechUpgradeRangeChecker extends RuleChecker<ZeroTerritoryOrder> {
  /**
   * The constructor of the TechUpgradeRangeChecker
   * 
   * @param next The next RuleChecker
   */
  public TechUpgradeRangeChecker(RuleChecker<ZeroTerritoryOrder> next) {
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
  protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
    if (!order.getMaxTechLevel().containsKey(order.getPlayer().getTechnologyLevel())) {
      return "The player current technology level is not in upgrade range";
    }
    return null;
  }
}
