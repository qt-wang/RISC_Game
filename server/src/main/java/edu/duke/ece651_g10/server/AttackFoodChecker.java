package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether the player has enough food to execute the
 * attack order
 */
public class AttackFoodChecker extends RuleChecker<TerritoryToTerritoryOrder> {
  /**
   * The constructor of the AttckFoodChecker
   *
   * @param next The next RuleChecker
   */
  public AttackFoodChecker(RuleChecker<TerritoryToTerritoryOrder> next) {
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
    if (gameMap.getOwnership().get(order.getSourceTerritory()).getFoodResourceTotal() < 1) {
      return "The player does not have enough food to execute the attack order.";
    }
    return null;
  }

}








