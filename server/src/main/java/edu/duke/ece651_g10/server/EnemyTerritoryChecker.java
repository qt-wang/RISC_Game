package edu.duke.ece651_g10.server;

/**
 * The RuleChekcer to check whether the target Territory belongs to the enemy
 */
public class EnemyTerritoryChecker extends RuleChecker<Order> {
  /**
   * The constructor of the the EnemyTerritoryChecker
   *
   * @param next The next RuleChecker
   */
  public EnemyTerritoryChecker(RuleChecker<Order> next) {
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
    if (gameMap.getOwnership().get(order.getTargetTerritory()).getPlayerID() == order.getPlayerID()) {
      return "The target territory does not belong to the enemy.";
    }
    return null;
  }

}







