package edu.duke.ece651_g10.server;

public class SufficientUnitToSpyChecker extends RuleChecker<OneTerritoryOrder>{

    public SufficientUnitToSpyChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getNumUnit() > order.getSourceTerritory().getNumUnit()) {
            return "This territory does not have enough units";
        }
        return null;
    }
}
