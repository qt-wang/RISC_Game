package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.json.JSONObject;

/**
 * The class for client side socket
 */
public class SocketClient {
    final String serverHostname;
    final int serverPort;
    private Socket socket;
    private BufferedReader br;
    private BufferedWriter bw;

    /**
     * The constructor of the ClientSocket
     */
    public SocketClient(String hostname, int port) throws IOException {
        this.serverHostname = hostname;
        this.serverPort = port;
        initSocket(hostname, port);
    }

    /**
     * Initiate the socket and connect to the server
     */
    private void initSocket(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
            this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        } catch (IOException e) {
            System.err.println("Failed to connect to server.");
        }
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
     * Try to receive a json object from the server.
     * @return
     * @throws IOException
     */
    public JSONObject tryReceive() throws IOException {
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
