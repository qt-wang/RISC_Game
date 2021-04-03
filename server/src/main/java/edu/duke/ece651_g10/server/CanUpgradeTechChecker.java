package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer can upgrade maximum technology in
 * this turn
 */
public class CanUpgradeTechChecker extends RuleChecker<ZeroTerritoryOrder> {
  /**
   * The constructor of the CanUpgradeTechChecker
   * 
   * @param next The next RuleChecker
   */
  public CanUpgradeTechChecker(RuleChecker<ZeroTerritoryOrder> next) {
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
    if (!order.getPlayer().getCanUpgradeInThisTurn()) {
      return "The player can not upgrade in this turn.";
    }
    return null;
  }
}
