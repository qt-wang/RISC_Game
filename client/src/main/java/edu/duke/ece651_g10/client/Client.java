package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * The client of the game
 */
public class Client {
  final int playerID;
  final PrintStream out;
  final BufferedReader inputReader;
  boolean couldCommand;
  boolean isDisconnected;

  /**
   * The constructor of the Client
   */
  public Client(int playerID, PrintStream out, BufferedReader input) {
    this.playerID = playerID;
    this.out = out;
    this.inputReader = input;
    couldCommand = true;
    isDisconnected = false;
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
}












