package edu.duke.ece651_g10.client.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameInfo {
    List<String> playerInfo;
    String sub;
    boolean canUpgrade;
    HashMap<String,List<String>> territoryInfos;
    public HashMap<Integer, List<String>> territoryOwnerShip = new HashMap<>();
    // Have 3 player status A L E
    String playerStatus;
    private HashMap<Integer,String> playerColor;

    public String getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerInfoLose(JSONObject obj) {
        try {
            List<String> info = new LinkedList<>();
            int playerId = obj.getInt("playerId");
            info.add("Player " + playerId + ":");
            info.add("You lost the game.");
            playerInfo = info;
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public GameInfo(JSONObject received) {
        //JSONObject
        //received = generateTestJSON();
        //System.out.println(received);
        sub = received.getString("sub");
        canUpgrade = received.getBoolean("canUpgrade");
        playerStatus = received.getString("playerStatus");
        int playerNumber = received.getInt("playerNumber");
        setPlayerColor();
        setTerritoryInfos(received);
        if (playerStatus.equals("L")) {
            setPlayerInfoLose(received);
        } else {
            setPlayerInfo(received);
        }
    }

    private void setPlayerColor() {
        playerColor = new HashMap<>();
        playerColor.put(1,"red");
        playerColor.put(2,"yellow");
        playerColor.put(3,"green");
        playerColor.put(4,"blue");
        playerColor.put(5,"purple");
    }

    public String getPlayerColor(int playerId){
        return playerColor.getOrDefault(playerId,null);
    }

    public void setPlayerInfo(JSONObject obj){
        try{
            List<String> info = new LinkedList<>();
            int playerId = obj.getInt("playerId");
            info.add("Player "+playerId+":");

            StringBuilder sb = new StringBuilder("Owns territory: ");
            for(String str : territoryOwnerShip.get(playerId)){
                String toAppend = str+" ";
                sb.append(toAppend);
            }
            info.add(sb.toString());
            //info.add("Territory color: "+colorStrategy.get(playerId));
            int technologyLevel = obj.getInt("technologyLevel");
            int foodResource = obj.getInt("foodResource");
            int technologyResource = obj.getInt("technologyResource");
            //boolean canUpgrade = obj.getBoolean("canUpgrade");
            if (canUpgrade) {
                info.add("Can upgrade tech: yes");
            } else {
                info.add("Can upgrade tech: no");
            }
            info.add("Tech lvl: "+technologyLevel);
            info.add("Tech resource: "+technologyResource);
            info.add("Food resource: "+foodResource);
            playerInfo = info;
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    public String getSub(){
        return sub;
    }
    public boolean getCanUpgrade(){
        return canUpgrade;
    }

    public void setTerritoryInfos(JSONObject obj){
        try{
            HashMap<String,List<String>> infos = new HashMap<>();
            HashMap<Integer, List<String>> ownerShip = new HashMap<>();
            JSONObject tInfos = obj.getJSONObject("TerritoriesInformation");
            for(String key:tInfos.keySet()){
                JSONObject singleT = tInfos.getJSONObject(key);
                JSONObject armies = singleT.getJSONObject("armies");
                int ownerId = singleT.getInt("owner");
                List<String> currOwners = ownerShip.getOrDefault(ownerId,new LinkedList<String>());
                currOwners.add(key);
                ownerShip.put(ownerId, currOwners);
                List<String> terrInfo = new LinkedList<>();
                terrInfo.add("Territory name: "+key);
                terrInfo.add("Owner: " + ownerId);
                int foodRate = singleT.getInt("foodResourceGenerationRate");
                int techRate = singleT.getInt("technologyResourceGenerationRate");
                int size = singleT.getInt("territorySize");
                terrInfo.add("Food increase rate: "+foodRate);
                terrInfo.add("Size: " + size);
                terrInfo.add("Tech resource increase rate: "+techRate);
                terrInfo.add("Number of units:");
                for(int i=0;i<7;i++){
                    String level = ""+(char)('0'+i);
                    int num = armies.getInt(level);
                    terrInfo.add("  Lv"+level+": "+num);
                }
                infos.put(key,terrInfo);
            }
            territoryInfos = infos;
            territoryOwnerShip = ownerShip;
            showOwnerShip();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }
    public List<String> getTerritoryInfo(String tName){
        return territoryInfos.getOrDefault(tName,null);
    }

    public List<String> getPlayerInfo(){
        return playerInfo;
    }

    public void showOwnerShip(){
        for(Integer i : territoryOwnerShip.keySet()){
            System.out.println("PlayerID: "+i);
            for(String name:territoryOwnerShip.get(i)){
                System.out.println("Territory: "+name);
            }
        }
    }
}
