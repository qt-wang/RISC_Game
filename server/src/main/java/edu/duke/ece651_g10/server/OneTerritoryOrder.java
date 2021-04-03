package edu.duke.ece651_g10.server;

public abstract class OneTerritoryOrder extends Order {
  protected Territory source;
  protected int unitNum;
  protected GameMap gMap;
  protected Player player;
  public OneTerritoryOrder(int playerID, String source, int unitNum, GameMap gMap, Player player) {
        super(playerID);
        this.gMap = gMap;
        this.source = gMap.getTerritory(source);
        this.unitNum = unitNum;
        this.player = player;
  }

  public abstract Territory getSourceTerritory();
  public abstract void execute();
  public abstract int getNumUnit();

}








