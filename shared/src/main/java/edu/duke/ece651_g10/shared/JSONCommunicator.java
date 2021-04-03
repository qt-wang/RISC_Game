package edu.duke.ece651_g10.shared;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class JSONCommunicator {
    private final BufferedWriter bw;
    private final BufferedReader br;

    public JSONCommunicator(BufferedReader br, BufferedWriter bw) {
        this.br = br;
        this.bw = bw;
    }

    /**
     * read JSONObject from the server
     *
     * @return the JSONObject
     * @throws IOException
     */
    public JSONObject receive() throws IOException {
        String jsonString = br.readLine();
        while (jsonString == null) {
            jsonString = br.readLine();
        }
        JSONObject obj = new JSONObject(jsonString);
        return obj;
    }

    /**
     * Read a json object from the buffered reader.
     * This function should not block.
     *
     * @return The json object read from the buffered reader.
     * @throws IOException
     */
    public JSONObject nonBlockingRead() throws IOException {
        String jsonString = null;
        // This ensures that it will not block.
        if (br.ready()) {
            jsonString = br.readLine();
        }
        return jsonString == null ? null : new JSONObject(jsonString);
    }
    /**
     * send a JSONObject to the server
     *
     * @param obj the JSONObject to be sent
     * @throws IOException
     */
    public void send(JSONObject obj) throws IOException {
        String jsonString = obj.toString();
        bw.write(jsonString + "\n");
        bw.flush();
    }
}
