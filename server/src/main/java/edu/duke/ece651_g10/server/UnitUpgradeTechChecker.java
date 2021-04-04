package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer's has corresponded technology
 * level to upgrade his or her units
 */
public class UnitUpgradeTechChecker extends RuleChecker<OneTerritoryOrder> {
  /**
   * The constructor of the UnitUpgradeTechChecker
   * 
   * @param next The next RuleChecker
   */
  public UnitUpgradeTechChecker(RuleChecker<OneTerritoryOrder> next) {
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
    if (gameMap.getOwnership().get(order.getSourceTerritory()).getTechnologyLevel() <= order.getLevel()) {
      return "The player's technology level is not high enough to upgrade the units";
    }
    return null;
  }
}
