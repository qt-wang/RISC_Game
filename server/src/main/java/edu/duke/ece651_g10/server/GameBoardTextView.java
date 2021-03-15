package edu.duke.ece651_g10.server;


import java.util.Set;

/**
 * This class is used to display the map (with information) to all the players.
 * Maintained by Guancheng Fu.
 */
public class GameBoardTextView implements GameBoardView{

    private GameMap gameMap;

    //TODO:Build the constructor.
    public GameBoardTextView(GameMap gameMap) {
        this.gameMap = gameMap;
    }

    @Override
    public String territoryForUser(Player player) {
        Set<Territory> territories = gameMap.getTerritoriesForPlayer(player);
        StringBuilder result = new StringBuilder();
        for (Territory t : territories) {
            StringBuilder temp = new StringBuilder();
            int numberOfUnits = t.getNumUnit();
            temp.append(numberOfUnits);
            temp.append(" units in ");
            temp.append(t.getName());
            temp.append(" (next to: ");
            int i = 0;
            for (Territory neighbour: t.getNeighbours()) {
                temp.append(neighbour.getName());
                if (i != t.getNeighbours().size() - 1) {
                    temp.append(", ");
                } else {
                    temp.append(")\n");
                }
                i += 1;
            }
            result.append(temp.toString());
        }
        return result.toString();
    }
}
