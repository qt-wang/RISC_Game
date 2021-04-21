package edu.duke.ece651_g10.server;

/**
 * A password generator used to generate random strings given back to client so that
 * they can use it to login.
 */
public class V2ServerPasswordGenerator implements  PasswordGenerator{

    static int password = 0;

    public V2ServerPasswordGenerator() {

    }

    /**
     * Use this to initialize the passwordGenerator after resume it from the database.
     * @param initialPassword
     */
    public V2ServerPasswordGenerator(int initialPassword) {
        V2ServerPasswordGenerator.password = initialPassword;
    }

    @Override
    public String generate() {
       synchronized (V2ServerPasswordGenerator.class) {
           return Integer.toString(password++);
       }
    }
}
