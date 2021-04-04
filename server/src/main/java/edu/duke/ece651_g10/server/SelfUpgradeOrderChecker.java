package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer's units upgrade is upgrading its
 * own units
 */
public class SelfUpgradeOrderChecker extends RuleChecker<OneTerritoryOrder> {
  /**
   * The constructor of the SelfUpgradeOrderChecker
   * 
   * @param next The next RuleChecker
   */
  public SelfUpgradeOrderChecker(RuleChecker<OneTerritoryOrder> next) {
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
    if (gameMap.getOwnership().get(order.getSourceTerritory()).getPlayerID() != order.getPlayerID()) {
      return "The player is not upgrading his or her own units.";
    }
    return null;
  }
}
