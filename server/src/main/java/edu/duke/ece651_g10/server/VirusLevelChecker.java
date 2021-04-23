package edu.duke.ece651_g10.server;

public class VirusLevelChecker extends RuleChecker<OneTerritoryOrder>{

    public VirusLevelChecker(RuleChecker<OneTerritoryOrder> next) {
        super(next);
        }

    @Override
    protected String checkMyRule(OneTerritoryOrder order, GameMap gameMap) {
        if (order.getLevel() > order.getPlayer().getVirusMaxLevel()) {
            return "This virus order's level cannot be higher than virus max level";
        }
        return null;
    }
}
