package edu.duke.ece651_g10.server;

public class UpgradeSpyResourceChecker extends RuleChecker<OneTerritoryOrder>{

    public UpgradeSpyResourceChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < 20) {
            return "This territory does not have enough technology resource.";
        }
        return null;
    }
}
