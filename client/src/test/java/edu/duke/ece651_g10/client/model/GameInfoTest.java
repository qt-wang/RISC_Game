package edu.duke.ece651_g10.client.model;

import org.json.JSONObject;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {
    @Disabled
    @Test
    void getPlayerStatus() {
        GameInfo g1 = new GameInfo(generateTestJSON("A"));
        assertEquals(g1.getPlayerStatus(),"A");
        GameInfo g2 = new GameInfo(generateTestJSON("L"));
        assertEquals(g2.getPlayerStatus(),"L");
    }
    @Disabled
    @Test
    void getSub() {
        GameInfo g1 = new GameInfo(generateTestJSON("A"));
        assertEquals(g1.getSub(),"Attack");
    }
    @Disabled
    @Test
    void getCanUpgrade() {
        GameInfo g1 = new GameInfo(generateTestJSON("A"));
        assertTrue(g1.getCanUpgrade());
    }

    @Disabled
    @Test
    void getTerritoryInfo() {
        GameInfo g1 = new GameInfo(generateTestJSON("A"));
        assertNull(g1.getTerritoryInfo("Z"));
        List<String> a = new LinkedList<>();
        a.add("Territory name: A");
        a.add("Owner: ");
        a.add("Owner: 1");
        a.add("Food increase rate: 1");
        a.add("Size: 1");
        a.add("Tech resource increase rate: 2");
        a.add("Number of units:");
        for(int i=0;i<7;i++){
            a.add("  Lv"+i+": 2");
        }
        assertEquals(g1.getTerritoryInfo("A"),a);
    }
    @Disabled
    @Test
    void getPlayerInfo() {

    }

    public JSONObject generateTestJSON(String playerStatus){
        JSONObject ans = new JSONObject();
        ans.put("type","Game").put("sub","Attack").put("playerId",1);
        ans.put("prompt","valid\n").put("reason","").put("playerStatus",playerStatus)
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
            singleT.put("territorySize",1);
            singleT.put("technologyResourceGenerationRate",2);
            String name = ""+(char)('A'+i);
            tInfo.put(name,singleT);
        }
        ans.put("TerritoriesInformation",tInfo);
        return ans;
    }
}