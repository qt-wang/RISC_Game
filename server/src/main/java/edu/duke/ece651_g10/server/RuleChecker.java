package edu.duke.ece651_g10.server;

/**
 * The abstract class to check whether the order follows the rules in the RISK game
 */
public abstract class RuleChecker {
  private final RuleChecker next;

  /**
   * The RuleChecker constructor
   *
   * @param next the next Rulechecker linked with current RuleChecker
   */
  public RuleChecker(RuleChecker next) {
    this.next = next;
  }

  /**
   * Check whether the Order is following the rules in the game
   *
   * @param  order   the order frome the player
   * @param  gameMap the game map of the game
   * @return if the rule is not violated, return null. 
   *         Otherwise, return the reason casuing the invalid placement
   */
  public String checkOrder(Order order, GameMap gameMap) {
    if (checkMyRule(order, gameMap) != null) {
      return checkMyRule(order, gameMap);
    }

    if (next != null) {
      return next.checkOrder(order, gameMap);
    }

    return null;
  }

  /**
   * Check whether the order is following the rule defined 
   * in the following method.
   * @param  order   the order frome the player
   * @param  gameMap the game map of the game
   * @return if the rule is not violated, return null. 
   *         Otherwise, return the reason casuing the invalid placement
   */
  protected abstract String checkMyRule(Order order, GameMap gameMap);
}
