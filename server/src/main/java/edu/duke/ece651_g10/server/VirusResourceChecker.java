package edu.duke.ece651_g10.server;

public class VirusResourceChecker extends RuleChecker<OneTerritoryOrder>{

    public VirusResourceChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < 200) {
            return "The player does not have enough technology resource.";
        }
        return null;
    }
}
