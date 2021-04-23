package edu.duke.ece651_g10.server;

public class MoveSpyEnoughSpies extends RuleChecker<TerritoryToTerritoryOrder>{

    public MoveSpyEnoughSpies(RuleChecker<TerritoryToTerritoryOrder> next) {
        super(next);
    }

    @Override
    protected String checkMyRule(TerritoryToTerritoryOrder order, GameMap gameMap) {
        if (order.getSourceTerritory().getSpyNumForPlayer(order.getPlayer()) < order.getNumUnit()) {
            return "The territory does not have enough spies to move";
        }
        return null;
    }
}
