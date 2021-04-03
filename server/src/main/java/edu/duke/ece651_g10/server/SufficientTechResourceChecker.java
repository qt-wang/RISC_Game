package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer has enough technology resource to
 * upgrade the maximum technology
 */
public class SufficientTechResourceChecker extends RuleChecker<ZeroTerritoryOrder> {
  /**
   * The constructor of the SufficientTechResourceChecker
   * 
   * @param next The next RuleChecker
   */
  public SufficientTechResourceChecker(RuleChecker<ZeroTerritoryOrder> next) {
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
    if (order.getMaxTechLevelTable().get(order.getPlayer().getTechnologyLevel()) > order.getPlayer()
        .getTechnologyResourceTotal()) {
      return "The player does not have enough technology to upgrade technology.";
    }
    return null;
  }
}
