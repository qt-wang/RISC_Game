package edu.duke.ece651_g10.server;

/**
 * The RuleChecker to check whether is has sufficient unit
 */
public class SufficientUnitChecker extends RuleChecker {

  /**
   * The constructor of the the SufficientUnitChecker
   *
   * @param next The next RuleChecker
   */
  public SufficientUnitChecker(RuleChecker next) {
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
    return null;
  }
}












