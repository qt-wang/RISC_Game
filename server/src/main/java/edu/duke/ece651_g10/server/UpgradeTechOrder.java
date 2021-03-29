package edu.duke.ece651_g10.server;

public class UpgradeTechOrder extends Order {
  private GameMap gMap;

  public UpgradeTechOrder(int playerID, GameMap gMap){
    super(playerID);
    this.gMap = gMap;
  }

  public void execute(){
    //gMap.upgradeTech(playerID, gMap);
  }
}













