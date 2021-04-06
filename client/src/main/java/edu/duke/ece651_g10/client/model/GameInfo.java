package edu.duke.ece651_g10.client.model;

import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class GameInfo {
    List<String> playerInfo = new ArrayList<>();
    HashMap<String,List<String>> territoryInfos;

    public GameInfo() {
        territoryInfos = new HashMap<>();
        playerInfo.add("playerinfo, line1");
        playerInfo.add("playerinfo, line2");
        List<String> ta = new ArrayList<>();
        ta.add("ta,line1");
        ta.add("ta,line2");
        List<String> tb = new ArrayList<>();
        tb.add("tb,line1");
        tb.add("tb,line2");
        territoryInfos.put("A",ta);
        territoryInfos.put("B",tb);
    }

    public List<String> getTerritoryInfo(String tName){
        return territoryInfos.getOrDefault(tName,null);
    }

    public List<String> getPlayerInfo(){
        return playerInfo;
    }

//    public GameInfo(JSONObject obj){
//        playerInfo = new ArrayList<>();
//        territoryInfos = new HashMap<>();
//        //setPlayerInfo();
//    }

}
