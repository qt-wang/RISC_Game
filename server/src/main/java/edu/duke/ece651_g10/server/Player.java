package edu.duke.ece651_g10.server;

import edu.duke.ece651_g10.shared.JSONCommunicator;

import java.net.Socket;

/**
 * The Player class records the information of a player within the game.
 * Maintained by Guancheng Fu
 */
public class Player {

    // The next available playerId.
    static int availableId;

    private JSONCommunicator jCommunicate;

    /**
     * Version 2 fields. foodResourceTotal: The total food resource owned by the
     * player. technologyResourceTotal: The total technology resource owned by the
     * player. technologyLevel: The technology level of the current player.
     * upgradeInThisTurn: Represents whether a player can upgrade its technology
     * level in this turn.
     */
    private int foodResourceTotal;

    private int technologyResourceTotal;

    private int technologyLevel;

    private boolean canUpgradeInThisTurn;

    // This variable represents whether the player is in current game.
    // If the player log out, then the inGame variable is set to false.
    private volatile boolean inGame;

    WaitGroup waitGroup;

    static {
        availableId = 1;
    }


    private boolean isLost;

    // This should record the socket related to this user.
    private int socketNumber;

    private Socket connectedSocket;

    private int playerId;

    /**
     * Constructor to be used when resume the game from the db.
     * TODO: Whether we should record the canUpgradeInThisTurn -- maybe we start from a new turn?
     *
     * @param playerId                The player id.
     * @param foodResourceTotal       The food resource owned by the player.
     * @param technologyResourceTotal The technology resource owned by the player.
     * @param technologyLevel         The technology level of the player.
     * @param canUpgradeInThisTurn    Whether the user can upgrade in this turn.
     * @param isLost                  Whether the player is lost or not.
     */
    Player(int playerId, int foodResourceTotal, int technologyResourceTotal, int technologyLevel, boolean canUpgradeInThisTurn, boolean isLost) {
        this.foodResourceTotal = foodResourceTotal;
        this.technologyResourceTotal = technologyResourceTotal;
        this.technologyLevel = technologyLevel;
        this.canUpgradeInThisTurn = canUpgradeInThisTurn;
        this.inGame = false;
        this.waitGroup = null;
        this.isLost = isLost;
        this.playerId = playerId;
    }

    public int getPlayerID() {
        return playerId;
    }

    public Socket getSocket() {
        return this.connectedSocket;
    }

    public int getSocketNumber() {
        return this.socketNumber;
    }

    public boolean getIsLost() {
        return isLost;
    }

    public void setIsLost() {
        this.isLost = true;
    }

    public JSONCommunicator getJCommunicator() {
        return this.jCommunicate;
    }


    public void setWaitGroup(WaitGroup waitGroup) {
        this.waitGroup = waitGroup;
    }

    //Added by yt136

    /**
     * Get whether the player can upgrade in this turn
     *
     * @return The boolean whether the player can upgrade in this trun
     */
    public boolean getCanUpgradeInThisTurn() {
        return this.canUpgradeInThisTurn;
    }

    public void setCanUpgradeInThisTurn(boolean canUpgradeInThisTurn) {
        this.canUpgradeInThisTurn = canUpgradeInThisTurn;
    }

    /**
     * Get the food resource total.
     *
     * @return The player's total food resource.
     */
    public int getFoodResourceTotal() {
        return this.foodResourceTotal;
    }

    /**
     * Set the player's food resource.
     *
     * @param foodResource The player's new food resource.
     */
    public void setFoodResourceTotal(int foodResource) {
        this.foodResourceTotal = foodResource;
    }

    /**
     * Get the player's current total technology units.
     *
     * @return the player's current technology units.
     */
    public int getTechnologyResourceTotal() {
        return this.technologyResourceTotal;
    }

    /**
     * Set the player's current total technology resource.
     *
     * @param resourceTotal The player's total resource.
     */
    public void setTechnologyResourceTotal(int resourceTotal) {
        this.technologyResourceTotal = resourceTotal;
    }

    /**
     * Get the player's current technology level.
     *
     * @return The player's current technology level.
     */
    public int getTechnologyLevel() {
        return this.technologyLevel;
    }

    /**
     * Increment the player's technology level by 1.
     */
    public void incrementTechnologyLevel() {
        this.technologyLevel += 1;
    }

    /**
     * Change the player's technology level by 1.
     */
    public void setTechnologyLevel(int level) {
        this.technologyLevel = level;
    }

    /**
     * Mark the player as join the game.
     */
    public void joinGame() {
        this.inGame = true;
    }

    /**
     * Check whether the player is in this game.
     *
     * @return True if the player is in this game. False if the player leaves the
     * game.
     */
    public boolean getInGameStatus() {
        return inGame;
    }

    /**
     * Mark the player as leave the game.
     */
    public void leaveGame() {
        this.inGame = false;
    }

    // public BufferedWriter getBufferedWriter() {
    // return this.bw;
    // }

    /**
     * Construct a Player class.
     *
     * @param socket The socket which can be used to read/write to it.
     */
    Player(Socket socket, JSONCommunicator jc) {
        synchronized (Player.class) {
            this.playerId = availableId++;
        }
        this.connectedSocket = socket;
        this.jCommunicate = jc;
        this.foodResourceTotal = 0;
        this.technologyResourceTotal = 0;
        this.technologyLevel = 1;
        this.canUpgradeInThisTurn = true;
        this.inGame = false;
        this.virusMaxLevel = 1; //added by qw99
        this.vaccineMaxLevel = 1; //added by qw99
        this.vaccineLevel = 0; //added by qw99
        this.canBombInThisGame = true; //added by qw99
        this.canVaccine = true; //added by qw99
        this.canVirus = true; //added by qw99
    }

    /**
     * Set the jCommunicate for the player.
     *
     * @param jc The jCommunicator for this player.
     */
    public void setJCommunicate(JSONCommunicator jc) {
        this.jCommunicate = jc;
    }

    /**
     * Set the availableId in this class.
     * Only used when resume the player from the database.
     *
     * @param availableId The id to be set for availableId.
     */
    public static void setAvailableId(int availableId) {
        Player.availableId = availableId;
    }

  //Added by qw99
  private int virusMaxLevel;
  private int vaccineLevel;
  private int vaccineMaxLevel;
  private boolean canBombInThisGame;
  private boolean canVaccine;
  private boolean canVirus;
  public int getVirusMaxLevel(){
    return virusMaxLevel;
  }
  public int getVaccineLevel(){
    return vaccineLevel;
  }
  public void incrementVirusMaxLevel(){
    this.virusMaxLevel += 1;
  }
  public void setVaccineLevel(int vaccineLevel){
    this.vaccineLevel = vaccineLevel;
  }
  public int getVaccineMaxLevel(){
    return vaccineMaxLevel;
  }
  public void incrementVaccineMaxLevel(){
    this.vaccineMaxLevel += 1;
  }
}
