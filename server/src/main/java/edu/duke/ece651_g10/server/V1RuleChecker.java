package edu.duke.ece651_g10.server;

public class V1RuleChecker extends RuleChecker{

    private RuleChecker moveRuleChecker;
    private RuleChecker attackRuleChecker;

    public V1RuleChecker(RuleChecker moveRuleChecker, RuleChecker attackRuleChecker) {
        super(null);
        this.moveRuleChecker = moveRuleChecker;
        this.attackRuleChecker = attackRuleChecker;
    }

    @Override
    protected String checkMyRule(Order order, GameMap gameMap) {
        //return super.checkOrder(order, gameMap);
        if (order instanceof MoveOrder) {
            return moveRuleChecker.checkOrder(order, gameMap);
        } else if (order instanceof AttackOrder) {
            return attackRuleChecker.checkOrder(order, gameMap);
        }
        return "Invalid order type\n";
    }
}
