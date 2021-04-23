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
    private final static String INVISIBLE_COLOR = "grey";
    List<String> playerInfo;
    int currPlayerID;
    String sub;
    boolean canUpgrade;
    HashMap<String,List<String>> territoryInfos;
    public HashMap<Integer, List<String>> territoryOwnerShip = new HashMap<>();
    // Have 3 player status A L E
    String playerStatus;
    private HashMap<Integer,String> playerColor;
    public HashMap<String,String> territoryColor;


    public String getPlayerStatus() {
        return playerStatus;
    }

    public String getMyOwnColor(){
        return playerColor.get(currPlayerID);
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
        currPlayerID = received.getInt("playerId");
        setTerritoryInfos(received);
        if (playerStatus.equals("L")) {
            setPlayerInfoLose(received);
        } else {
            setPlayerInfo(received);
        }
    }

    private void setPlayerColor(JSONObject colors) {
        playerColor = new HashMap<>();
        for(String key:colors.keySet()){
            int playerId = Integer.parseInt(key);
            String color = colors.getString(key);
            playerColor.put(playerId,color);
        }
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
            int technologyLevel = obj.getInt("technologyLevel");
            int foodResource = obj.getInt("foodResource");
            int technologyResource = obj.getInt("technologyResource");
            int vaccineLvl = obj.getInt("vaccineLevel");
            int vaccineMaxLvl = obj.getInt("vaccineMaxLevel");
            int virusMaxLvl = obj.getInt("virusMaxLevel");
            String upgradable = canUpgrade ? "yes" : "no";
            info.add("Tech Lv: "+technologyLevel);
            info.add("Tech resource: "+technologyResource);
            info.add("Food resource: "+foodResource);
            String researchedCloak = obj.getBoolean("researchedCloak") ? "yes" : "no";
            info.add("Can upgrade tech: "+upgradable);
            info.add("Cloak ability: "+researchedCloak);
            info.add("Current vaccine Lv: "+vaccineLvl);
            info.add("Max available vaccine Lv: "+vaccineMaxLvl);
            info.add("Max usable virus Lv: "+virusMaxLvl);
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
            HashMap<String,String> terrColors = new HashMap<>();
            JSONObject colors = obj.getJSONObject("colorStrategy");
            setPlayerColor(colors);
            JSONObject tInfos = obj.getJSONObject("TerritoriesInformation");
            for(String key:tInfos.keySet()){
                JSONObject singleT = tInfos.getJSONObject(key);
                JSONObject armies = singleT.getJSONObject("armies");
                JSONObject spyInfo = singleT.getJSONObject("spyInfo");
                int spyNum;
                if (spyInfo.has(Integer.toString(currPlayerID))) {
                    spyNum = spyInfo.getInt(Integer.toString(currPlayerID));
                } else {
                    spyNum = 0;
                }
                boolean visible = singleT.getBoolean("visible"),isNew = singleT.getBoolean("isNew");
                int ownerId = singleT.getInt("owner");
                List<String> currOwners = ownerShip.getOrDefault(ownerId,new LinkedList<String>());
                currOwners.add(key);
                ownerShip.put(ownerId, currOwners);
                List<String> terrInfo = new LinkedList<>();
                if(spyNum==1){
                    terrInfo.add("You have a spy on this territory\n");
                }else if(spyNum>1){
                    terrInfo.add("You have "+spyNum+" spies on this territory\n");
                }
                terrInfo.add("Territory name: "+key);
                if(visible){
                    terrColors.put(key,getPlayerColor(ownerId));
                    terrInfo.add("Owner: " + ownerId);
                    int foodRate = singleT.getInt("foodResourceGenerationRate");
                    int techRate = singleT.getInt("technologyResourceGenerationRate");
                    int size = singleT.getInt("territorySize");
                    terrInfo.add("Size: " + size);
                    terrInfo.add("Food increase rate: "+foodRate);
                    terrInfo.add("Tech resource increase rate: "+techRate);
                    terrInfo.add("Number of units:");
                    for(int i=0;i<7;i++){
                        String level = ""+(char)('0'+i);
                        int num = armies.getInt(level);
                        terrInfo.add("  Lv"+level+": "+num);
                    }
                }else{
                    terrColors.put(key,INVISIBLE_COLOR);
                }
                infos.put(key,terrInfo);
            }
            territoryInfos = infos;
            territoryOwnerShip = ownerShip;
            territoryColor = terrColors;
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
