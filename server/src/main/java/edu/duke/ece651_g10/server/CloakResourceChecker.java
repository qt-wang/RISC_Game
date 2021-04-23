package edu.duke.ece651_g10.server;

public class CloakResourceChecker extends RuleChecker<OneTerritoryOrder>{

    public CloakResourceChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < 20) {
            return "The player does not have enough technology resource to cloak.";
        }
        return null;
    }
}
