package edu.duke.ece651_g10.client;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

public class ClientTest {
  private Client create_client(String inputData, OutputStream bytes, int port) throws IOException {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A")
        .put("prompt", "test string");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client = new Client(output, input, mockSocketClient);
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

  @Test
  public void test_send_order_normal() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    HashSet<String> legalOrderSet = new HashSet<String>();
    legalOrderSet.add("M");
    legalOrderSet.add("D");
    BufferedReader input = new BufferedReader(new StringReader("M\nDurham\nOrange\n42\n"));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A")
        .put("prompt", "test string");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A").put("prompt",
        "valid\n");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    ArrayList<String> ans1 = client1.sendOrder("", legalOrderSet);

    ArrayList<String> res1 = new ArrayList<String>();
    res1.add("M");
    res1.add("Durham");
    res1.add("Orange");
    res1.add("42");

    assertEquals(res1, ans1);
  }

  @Test
  public void test_send_order_unnormal() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    HashSet<String> legalOrderSet = new HashSet<String>();
    legalOrderSet.add("M");
    legalOrderSet.add("D");
    BufferedReader input = new BufferedReader(new StringReader("M\nDurham\nOrange\n42\nD\n"));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A")
        .put("prompt", "test string");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    JSONObject json1 = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A").put("prompt",
        "invalid\n");
    JSONObject json2 = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A").put("prompt",
        "valid\n");

    when(mockSocketClient.receive()).thenAnswer(new Answer<JSONObject>() {
      private int count = 0;

      public JSONObject answer(InvocationOnMock invocation) {
        if (count++ == 1)
          return json2;

        return json1;
      }
    });

    ArrayList<String> ans2 = client1.sendOrder("", legalOrderSet);

    ArrayList<String> res2 = new ArrayList<String>();
    res2.add("D");
    res2.add("");
    res2.add("");
    res2.add("");

    assertEquals(res2, ans2);
  }

  @Test
  public void test_do_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("M\nDurham\nOrange\n42\nD\n"));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A")
        .put("prompt", "valid\n");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    boolean ans = client1.doPlacement();
    assertEquals(true, ans);
  }

  @Test
  public void test_play_game_end() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader(""));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "E")
        .put("prompt", "End Game");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    boolean ans = client1.playGame();
    assertEquals(true, ans);
  }

   @Test
  public void test_play_game_lost() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader(""));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "L")
        .put("prompt", "Please watch the game");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    boolean ans = client1.playGame();
    assertEquals(false, ans);
  }

   @Test
  public void test_play_game_normal() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    BufferedReader input = new BufferedReader(new StringReader("M\nDurham\nOrange\n42\nD\n"));
    PrintStream output = new PrintStream(bytes, true);
    SocketClient mockSocketClient = mock(SocketClient.class);
    JSONObject jsonObject = new JSONObject().put("type", "info").put("playerID", 1).put("playerStatus", "A")
        .put("prompt", "Please play the game.");
    when(mockSocketClient.receive()).thenReturn(jsonObject);
    Client client1 = new Client(output, input, mockSocketClient);
    boolean ans = client1.playGame();
    assertEquals(false, ans);
  }

  @Test
  public void test_JSONObject_generator_getter() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    Client c = create_client("", bytes, 1);
    JSONObject commit = c.generateCommitJSON();
    assertEquals(commit.getString("type"), "commit");
    String[] prompts = { "a", "1", "*", "\n" };
    JSONObject[] promptJs = new JSONObject[4];
    for (int i = 0; i < prompts.length; i++) {
      promptJs[i] = c.generateInfoJSON(prompts[i]);
    }
    for (int i = 0; i < prompts.length; i++) {
      assertEquals(promptJs[i].getString("type"), "inform");
      assertEquals(promptJs[i].getString("prompt"), prompts[i]);
    }
    JSONObject order1 = c.generateOrderJSON("attack", "durham", "shanghai", 5);
    JSONObject order2 = c.generateOrderJSON("move", "beijing", "chicago", 9);
    orderJSON_test_helper(order1, "attack", "durham", "shanghai", 5);
    orderJSON_test_helper(order2, "move", "beijing", "chicago", 9);
    JSONObject testNullGetter = new JSONObject();
    assertNull(c.getMessageType(testNullGetter));
    assertThrows(AssertionError.class, () -> c.getAskingType(testNullGetter));
    assertNull(c.getPlayerId(testNullGetter));
    assertNull(c.getPrompt(testNullGetter));
    assertNull(c.getPlayerStatus(testNullGetter));
    JSONObject testGetter = new JSONObject().put("type", "ask").put("playerID", 3).put("playerStatus", "L")
        .put("prompt", "aaa").put("asking", "regular");
    assertEquals(c.getMessageType(testGetter), "ask");
    assertEquals(c.getAskingType(testGetter), "regular");
    assertEquals(c.getPlayerId(testGetter), 3);
    assertEquals(c.getPrompt(testGetter), "aaa");
    assertEquals(c.getPlayerStatus(testGetter), "L");
  }

  private void orderJSON_test_helper(JSONObject order, String orderType, String sourceT, String destT, int unitNum) {
    assertEquals(order.getString("type"), "order");
    assertEquals(order.getString("orderType"), orderType);
    assertEquals(order.getString("sourceTerritory"), sourceT);
    assertEquals(order.getString("destTerritory"), destT);
    assertEquals(order.getInt("unitNumber"), unitNum);
  }

}
