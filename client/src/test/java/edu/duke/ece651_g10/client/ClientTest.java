package edu.duke.ece651_g10.client;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class ClientTest {
  private Client create_client(String inputData, OutputStream bytes, int port) throws IOException {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Client client = new Client(output, input, "0.0.0.0", port);
    return client;
  }

  @Test
  public void test_read_string() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String expectString = "123";
    Client c1 = create_client(expectString, bytes, 1);

    String prompt = "Please input your command";
    assertEquals(expectString, c1.readString(prompt));
    assertEquals(prompt + "\n", bytes.toString());
    bytes.reset();
  }

  @Test
  public void test_read_action() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    HashSet<String> legalAction = new HashSet<>();
    legalAction.add("A");
    legalAction.add("B");
    Client client = create_client("C\nb\n", bytes, 2);

    String prompt = "Please input your command.";
    String error = "Please input valid actions.";
    String s = client.readAction(prompt, legalAction);
    assertEquals("B", s);
    assertEquals(prompt + "\n" + error + "\n" + prompt + "\n", bytes.toString());
    bytes.reset();
  }

  @Test
  public void test_read_integer() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client client = create_client("1.23\nb\n55\n", bytes, 3);
    String prompt = "Please input your number.";
    String error = "Please input valid integer.";
    assertEquals("55", client.readInteger(prompt));
    assertEquals(prompt + "\n" + error + "\n" + prompt + "\n" + error + "\n" + prompt + "\n", bytes.toString());
    bytes.reset();
  }

  @Test
  public void test_generate_order_string() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    HashSet<String> legalOrderSet = new HashSet<String>();
    legalOrderSet.add("M");
    legalOrderSet.add("D");
    Client client1 = create_client("M\nDurham\nOrange\n42\n", bytes, 4);
    ArrayList<String> res1 = new ArrayList<String>();
    res1.add("M");
    res1.add("Durham");
    res1.add("Orange");
    res1.add("42");
    Client client2 = create_client("d\n", bytes, 4);
    ArrayList<String> res2 = new ArrayList<String>();
    res2.add("D");
    res2.add("");
    res2.add("");
    res2.add("");
    String prompt = "Input order:";
    assertEquals(res1, client1.generateOrderString(prompt, legalOrderSet));
    assertEquals(res2, client2.generateOrderString(prompt, legalOrderSet));
  }

}











