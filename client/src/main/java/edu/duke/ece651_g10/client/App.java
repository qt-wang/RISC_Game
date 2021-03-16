/*
 * The App skeleton for the RISK client
 */
package edu.duke.ece651_g10.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * The App class
 */
public class App {
  final Client client;

  /**
   * The constructor of the App class
   *
   * @param input    The BufferedReader input
   * @param hostname The hostname of the game
   * @param port     The port of the game
   */
  public App(BufferedReader input, String hostname, int port) throws IOException {
    this.client = new Client(System.out, input, hostname, port);
  }

  public static void main(String[] args) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    App app = new App(input, "0.0.0.0", 1111);
  }
}
