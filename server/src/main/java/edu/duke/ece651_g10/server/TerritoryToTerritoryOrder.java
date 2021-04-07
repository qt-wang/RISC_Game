package edu.duke.ece651_g10.server;

public abstract class TerritoryToTerritoryOrder extends Order{
    protected Territory source;
    protected Territory dest;
    protected int unitNum;
    protected GameMap gMap;
    protected Player player;
    int level;

    public TerritoryToTerritoryOrder(int playerID, String source, String dest, int unitNum, GameMap gMap, Player p, int level) {
        super(playerID);
        this.gMap = gMap;
        this.source = gMap.getTerritory(source);
        this.dest = gMap.getTerritory(dest);
        this.unitNum = unitNum;
        this.player = p;
        this.level = level;
    }

  public abstract Territory getSourceTerritory();

  public abstract int getNumUnit();

  public abstract Territory getTargetTerritory();

  public abstract void execute();

  public abstract void addUnits(int number);

  public abstract int getLevel();

}








