package edu.duke.ece651_g10.server;

public class V1RuleChecker extends RuleChecker{

    private RuleChecker moveRuleChecker;
    private RuleChecker attackRuleChecker;

    @Override
    public String checkOrder(Order order, GameMap gameMap) {
        //return super.checkOrder(order, gameMap);
        if (order instanceof MoveOrder) {
            return moveRuleChecker.checkMyRule(order, gameMap);
        } else if (order instanceof AttackOrder) {
            return attackRuleChecker.checkMyRule(order, gameMap);
        }
        return "Invalid order type\n";
    }

    public V1RuleChecker(RuleChecker moveRuleChecker, RuleChecker attackRuleChecker) {
        super(null);
        this.moveRuleChecker = moveRuleChecker;
        this.attackRuleChecker = attackRuleChecker;
    }

    @Override
    protected String checkMyRule(Order order, GameMap gameMap) {
        return null;
    }
}
