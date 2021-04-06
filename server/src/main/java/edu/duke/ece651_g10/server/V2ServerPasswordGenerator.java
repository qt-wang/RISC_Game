package edu.duke.ece651_g10.server;

/**
 * A password generator used to generate random strings given back to client so that
 * they can use it to login.
 */
public class V2ServerPasswordGenerator implements  PasswordGenerator{

    static int password = 0;

    public V2ServerPasswordGenerator() {

    }

    @Override
    //TODO: Implementation.
    public String generate() {
        return Integer.toString(password++);
    }
}
