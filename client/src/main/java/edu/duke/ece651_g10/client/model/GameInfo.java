package edu.duke.ece651_g10.client.model;

import org.json.JSONException;
import org.json.JSONObject;

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
    HashMap<Integer, List<String>> territoryOwnerShip = new HashMap<>();
    //HashMap<Integer, String> colorStrategy = new HashMap<>();

    public GameInfo(JSONObject received) {
        //JSONObject
        //received = generateTestJSON();
        System.out.println(received);
        sub = received.getString("sub");
        canUpgrade = received.getBoolean("canUpgrade");
        int playerNumber = received.getInt("playerNumber");
        //setColorStrategy(playerNumber);
        setTerritoryInfos(received);
        setPlayerInfo(received);
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
    public List<String> getTerritoryInfo(String tName){
        return territoryInfos.getOrDefault(tName,null);
    }

    public List<String> getPlayerInfo(){
        return playerInfo;
    }

    public void updateGameInfo(JSONObject obj){

    }

    public JSONObject generateTestJSON(){
        JSONObject ans = new JSONObject();
        ans.put("type","Game").put("sub","Attack").put("playerId",1);
        ans.put("prompt","valid\n").put("reason","").put("playerStatus","A")
                .put("foodResource",100).put("technologyResource",100)
                .put("technologyLevel",2).put("canUpgrade",true).put("playerNumber",5);

        JSONObject tInfo = new JSONObject();
        for(int i=0;i<15;i++){
            JSONObject singleT = new JSONObject();
            JSONObject armies = new JSONObject();
            for(int j = 0;j<7;j++){
                String curr = ""+(char)('0'+j);
                armies.put(curr,2);
            }
            singleT.put("armies",armies);
            singleT.put("owner",i%5+1);
            singleT.put("foodResourceGenerationRate",1);
            singleT.put("technologyResourceGenerationRate",2);
            String name = ""+(char)('A'+i);
            tInfo.put(name,singleT);
        }
        ans.put("TerritoriesInformation",tInfo);
        return ans;
    }
//    public GameInfo(JSONObject obj){
//        playerInfo = new ArrayList<>();
//        territoryInfos = new HashMap<>();
//        //setPlayerInfo();
//    }

}
