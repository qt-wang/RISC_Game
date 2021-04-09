package edu.duke.ece651_g10.client.model;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameInfo {
    //the list of string to show in the listview of playerInfo
    List<String> playerInfo;
    //current phase of game
    //could be "Placement","Attack","GameEnd"
    String sub;
    //true if the player can upgrade tech level, false if not
    boolean canUpgrade;
    //the key is the terr's name, value is a list of string to show in the listview of terrInfo
    HashMap<String,List<String>> territoryInfos;
    //key is all possible player id, value is the list of the terrs' names owned by that player
    HashMap<Integer, List<String>> territoryOwnerShip = new HashMap<>();
    // Have 3 player status A L E
    String playerStatus;
    //HashMap<Integer, String> colorStrategy = new HashMap<>();

    /**
     * construct the model by the JSONObject received
     * set territory's infos and player infos
     * @param received the JSONObject received by the client
     */
    public GameInfo(JSONObject received) {
        //JSONObject
        //received = generateTestJSON();
        //System.out.println(received);
        sub = received.getString("sub");
        canUpgrade = received.getBoolean("canUpgrade");
        playerStatus = received.getString("playerStatus");
        //int playerNumber = received.getInt("playerNumber");
        //setColorStrategy(playerNumber);
        setTerritoryInfos(received);
        if (playerStatus.equals("L")) {
            setPlayerInfoLose(received);
        } else {
            setPlayerInfo(received);
        }
    }

    /**
     * get the player's status
     * "A" or "L" or "E"
     * @return
     */
    public String getPlayerStatus() {
        return playerStatus;
    }

    /**
     * set the info when the player loses
     * @param obj the received jsonObject
     */
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
    /**
     * set the info when the player is alive
     * @param obj the received jsonObject
     */
    public void setPlayerInfo(JSONObject obj){
        try{
            List<String> info = new LinkedList<>();
            int playerId = obj.getInt("playerId");
            info.add("Player "+playerId+":");

            StringBuilder sb = new StringBuilder("Owns territory: ");
            if (territoryOwnerShip != null) {
                for(String str : territoryOwnerShip.get(playerId)){
                    String toAppend = str+" ";
                    sb.append(toAppend);
                }
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

//    public void setColorStrategy(int playerNum){
//        colorStrategy.put(1,"Red");
//        colorStrategy.put(2,"Blue");
//        if(playerNum>=3){
//            colorStrategy.put(3,"Yellow");
//        }
//        if(playerNum>=4){
//            colorStrategy.put(4,"Green");
//        }
//        if(playerNum>=5){
//            colorStrategy.put(5,"Purple");
//        }
//    }

    /**
     * set the terr related infos according to the given json
     * ownership, terrinfo
     * @param obj the given jsonobject
     */
    public void setTerritoryInfos(JSONObject obj){
        try{
            HashMap<String,List<String>> infos = new HashMap<>();
            JSONObject tInfos = obj.getJSONObject("TerritoriesInformation");
            for(String key:tInfos.keySet()){
                JSONObject singleT = tInfos.getJSONObject(key);
                JSONObject armies = singleT.getJSONObject("armies");
                int ownerId = singleT.getInt("owner");
                List<String> currOwners = territoryOwnerShip.getOrDefault(ownerId,new LinkedList<String>());
                currOwners.add(key);
                territoryOwnerShip.put(ownerId, currOwners);
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
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    /**
     * get the terr's info to show
     * @param tName the required terr's name
     * @return a list of string for controller to show in listview
     */
    public List<String> getTerritoryInfo(String tName){
        return territoryInfos.getOrDefault(tName,null);
    }

    /**
     * get the playerinfo to show
     * @return a list of string for controller to show in listview
     */
    public List<String> getPlayerInfo(){
        return playerInfo;
    }

//    public JSONObject generateTestJSON(){
//        JSONObject ans = new JSONObject();
//        ans.put("type","Game").put("sub","Attack").put("playerId",1);
//        ans.put("prompt","valid\n").put("reason","").put("playerStatus","A")
//                .put("foodResource",100).put("technologyResource",100)
//                .put("technologyLevel",2).put("canUpgrade",true).put("playerNumber",5);
//
//        JSONObject tInfo = new JSONObject();
//        for(int i=0;i<15;i++){
//            JSONObject singleT = new JSONObject();
//            JSONObject armies = new JSONObject();
//            for(int j = 0;j<7;j++){
//                String curr = ""+(char)('0'+j);
//                armies.put(curr,2);
//            }
//            singleT.put("armies",armies);
//            singleT.put("owner",i%5+1);
//            singleT.put("foodResourceGenerationRate",1);
//            singleT.put("technologyResourceGenerationRate",2);
//            String name = ""+(char)('A'+i);
//            tInfo.put(name,singleT);
//        }
//        ans.put("TerritoriesInformation",tInfo);
//        return ans;
//    }
}
