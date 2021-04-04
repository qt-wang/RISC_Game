package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the palyer's has sufficient units in certain
 * level to upgrade his or her units
 */
public class UpgradeSufficientUnitChecker extends RuleChecker<OneTerritoryOrder> {
  /**
   * The constructor of the UpgradeSufficientUnitChecker
   * 
   * @param next The next RuleChecker
   */
  public UpgradeSufficientUnitChecker(RuleChecker<OneTerritoryOrder> next) {
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
    if (order.getNumUnit() > order.getSourceTerritory().getArmyWithLevel(order.getLevel()).getArmyUnits()) {
      return "The player does not have enough units to upgrade in the current level";
    }
    return null;
  }
}
