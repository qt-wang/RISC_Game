package edu.duke.ece651_g10.server;

public class SpyMoveTerritoryChecker extends RuleChecker<TerritoryToTerritoryOrder>{

    public SpyMoveTerritoryChecker(RuleChecker<TerritoryToTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(TerritoryToTerritoryOrder order, GameMap gameMap) {
        if(order.getSourceTerritory().getOwner().getPlayerID() != order.getPlayerID()) {
            if (!order.getSourceTerritory().getNeighbours().contains(order.getSourceTerritory())) {
                return "If the spy is in an enemy territory, the spy can only move to a target territory next to the source territory.";
            }
        }
        return null;
    }
}
