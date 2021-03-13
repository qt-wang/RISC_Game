package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * This class represents one territory of the map.
 * Maintained by Guancheng Fu.
 */
public class Territory {
    private String name;
    private Player owner;
    private HashMap<Player, Army> units;
    private HashSet<Territory> neighbours;

    // This units are all owned by the owner of the territory.
    private Army ownedUnits;

  public int getNumUnit(){
    return 0;
  }

}













