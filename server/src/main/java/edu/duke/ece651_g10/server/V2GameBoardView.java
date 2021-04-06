package edu.duke.ece651_g10.server;

import org.json.JSONObject;

public interface V2GameBoardView {

    /**
     * Return a JSON object which should describe the territory information
     * @param player
     * @return
     */
    public JSONObject territoryForUser(Player player);
}
