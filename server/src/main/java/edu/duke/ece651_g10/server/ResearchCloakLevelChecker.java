package edu.duke.ece651_g10.server;

public class ResearchCloakLevelChecker extends RuleChecker<ZeroTerritoryOrder>{

    public ResearchCloakLevelChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyLevel() < 3) {
            return "The player's technology level is lower than 3.";
        }
        return null;
    }
}
