package edu.duke.ece651_g10.server;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Territory {
    private String name;
    private Player owner;
    private HashMap<Player, List<Unit>> units;
    private HashSet<Territory> neighbours;
    // This units are all owned by the owner of the territory.
    private List<Unit> ownedUnits;
}
