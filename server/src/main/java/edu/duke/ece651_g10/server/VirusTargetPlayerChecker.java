package edu.duke.ece651_g10.server;

public class VirusTargetPlayerChecker extends RuleChecker<OneTerritoryOrder>{

    public VirusTargetPlayerChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (!order.getPlayer().equals(order.getSourceTerritory().getOwner())) {
            return "The target player cannot be the order starter";
        }
        return null;
    }
}
