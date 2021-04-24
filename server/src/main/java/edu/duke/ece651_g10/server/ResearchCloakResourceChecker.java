package edu.duke.ece651_g10.server;

public class ResearchCloakResourceChecker extends RuleChecker<ZeroTerritoryOrder>{

    public ResearchCloakResourceChecker(RuleChecker<ZeroTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(ZeroTerritoryOrder order, GameMap gameMap) {
        if (order.getPlayer().getTechnologyResourceTotal() < 100) {
            return "The player does not have enough technology to research cloak.";
        }
        return null;
    }
}
