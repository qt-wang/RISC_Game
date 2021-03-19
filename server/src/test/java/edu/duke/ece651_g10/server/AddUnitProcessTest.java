package edu.duke.ece651_g10.server;

import static org.junit.jupiter.api.Assertions.*;
import java.util.*;
import org.junit.jupiter.api.Test;

public class AddUnitProcessTest {
  @Test
  public void test_addUnitProcess() {
    V1Territory t1 = new V1Territory("A");
    V1Territory t2 = new V1Territory("B");
    List<Territory> l = new ArrayList<Territory>();
    l.add(t1);
    l.add(t2);
    AddUnitProcess a = new AddUnitProcess(l);
    a.addOneUnit();
    assertEquals(1, t1.getNumUnit());
  }
  }





