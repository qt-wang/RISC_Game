package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The client of the game
 */
public class Client {
    final int playerID;
    final PrintStream out;
    final BufferedReader inputReader;

    final String serverHostname;
    final int serverPort;
    private Socket socket;
    private InputStream is;
    private OutputStream os;
    private ObjectOutputStream objectOut;
    private BufferedReader br;

    private boolean couldCommand;
    private boolean isDisconnected;
    final HashSet<String> normalOrderSet;

    /**
     * The constructor of the Client
     */
    public Client(PrintStream out, BufferedReader input, String hostname, int port) throws IOException {
        this.serverHostname = hostname;
        this.serverPort = port;
        initSocket(hostname, port);

        // Don't know how to use Mockito to set playerID at the beginning, just set 0
        // for now.
        // this.playerID = Integer.parseInt(readLinesFromServer(this.br));
        this.playerID = 0;
        this.out = out;
        this.inputReader = input;
        couldCommand = true;
        isDisconnected = false;

        this.normalOrderSet = new HashSet<String>();
        normalOrderSet.add("M");
        normalOrderSet.add("A");
    }

    /**
     * Initiate the socket and connect to the server
     */
    private void initSocket(String hostname, int port) {
        try {
            this.socket = new Socket(hostname, port);
            this.is = this.socket.getInputStream();
            this.os = this.socket.getOutputStream();

            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(this.os));
            this.br = new BufferedReader(new InputStreamReader(this.is));
            this.objectOut = new ObjectOutputStream(this.os);

            bw.write("Testing, server, can you hear me?\n");
            bw.flush();
            //Thread.sleep(5000);
            //String ans = readLinesFromServer(this.br);
            //String ans = this.br.readLine();
            String ans = readLinesFromServer();
            System.out.println("Server：" + ans.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Read a bunch of lines and combine them into one String
     */
    public String readLinesFromServer() throws IOException {
        String msg = br.readLine();
        msg += "\n";
        StringBuilder ans = new StringBuilder(msg);
        while (br.ready()) {
            String temp = br.readLine();
            ans.append(temp);
            ans.append("\n");
        }
        return ans.toString();
    }



    public void run() {

    }

    /**
     * Read the input from the user
     *
     * @param prompt The String will print in the terminal before the user inputs
     *               data
     * @return the string that user input
     */
    public String readString(String prompt) throws IOException {
        out.println(prompt);
        return inputReader.readLine();
    }

    /**
     * Read the action from the user
     *
     * @param prompt        The String will print in the terminal before the user
     *                      inputs data
     * @param legalInputSet The set of legal input
     * @return the valid action
     */
    public String readAction(String prompt, HashSet<String> legalInputSet) throws IOException {
        String action = readString(prompt);
        if (!legalInputSet.contains(action.toUpperCase())) {
            out.println("Please input valid actions.");
            return readAction(prompt, legalInputSet);
        }
        return action.toUpperCase();
    }

    /**
     * Read the number from the user
     *
     * @param prompt The String will print in the terminal before the user inputs
     *               data
     * @return A number
     */
    public String readInteger(String prompt) throws IOException {
        try {
            int number = Integer.parseInt(readString(prompt));
            return String.valueOf(number);
        } catch (NumberFormatException e) {
            out.println("Please input valid integer.");
            return readInteger(prompt);
        }
    }

    /**
     * Generate the order string based on the user input
     *
     * @param prompt        The String will print in the terminal before the user
     *                      inputs data
     * @param legalOrderSet The order set
     * @return the ArrayList of the String describes the order
     */
    public ArrayList<String> generateOrderString(String prompt, HashSet<String> legalOrderSet) throws IOException {
        ArrayList<String> orderStringList = new ArrayList<String>();
        String orderType = readAction(prompt, legalOrderSet);
        orderStringList.add(orderType);
        if (normalOrderSet.contains(orderType)) {
            orderStringList.add(readString("Please input your source territory name:"));
            orderStringList.add(readString("Please input your target territory name:"));
            orderStringList.add(readInteger("Please input the number of nuit:"));
        } else {
            for (int i = 0; i < 3; i++) {
                orderStringList.add("");
            }
        }
        return orderStringList;
    }

    /**
     * Send order to server
     *
     * @param orderString The order string describes the Order
     */
    public void sendOrderToServer(ArrayList<String> orderString) {
        // TODO
    }

    /**
     * Send order
     *
     * @param prompt the information will print in the terminal before user input
     * @return the ArrayList of the String describes the order
     */
    public ArrayList<String> sendOrder(String prompt, HashSet<String> legalOrderSet) throws IOException {
        ArrayList<String> orderString = generateOrderString(prompt, legalOrderSet);
        sendOrderToServer(orderString);
        if (readLinesFromServer() == "invalid\n") {
            out.println("Your last order is invalid, please input your order again");
            orderString = sendOrder(prompt, legalOrderSet);
        }
        return orderString;
    }

    /**
     * Place the units at beginning of the game
     */
    public void doPlacement() throws IOException {
        out.println(readLinesFromServer());
        String prompt = "You can move your units now.\n   (M)ove\n   (D)one";
        out.println(prompt);
        HashSet<String> legalOrderSet = new HashSet<String>();
        legalOrderSet.add("M");
        legalOrderSet.add("D");
        ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
        while (orderString.get(0) != "D") {
            orderString = sendOrder(prompt, legalOrderSet);
        }
    }

    /**
     * Play the game after the placement phase
     */
    public void playGame() throws IOException {
        String receivedString = readLinesFromServer();
        out.println(receivedString.substring(0, receivedString.length() - 2));
        if (receivedString.substring(receivedString.length() - 2, receivedString.length() - 1) == "L") {
            ArrayList<String> commit = new ArrayList<String>();
            commit.add("D");
            commit.add("");
            commit.add("");
            commit.add("");
            sendOrderToServer(commit);
            // Should receive valid?
            readLinesFromServer();
        } else {
            String prompt = "You are the Player " + String.valueOf(playerID)
                    + ", What would you like to do?\n   (M)ove\n   (A)ttack\n   (D)one";
            out.println(prompt);
            HashSet<String> legalOrderSet = new HashSet<String>();
            legalOrderSet.add("M");
            legalOrderSet.add("A");
            legalOrderSet.add("D");
            ArrayList<String> orderString = sendOrder(prompt, legalOrderSet);
            while (orderString.get(0) != "D") {
                orderString = sendOrder(prompt, legalOrderSet);
            }
        }
    }
}
