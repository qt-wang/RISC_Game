package edu.duke.ece651_g10.server;

/**
 * A password generator used to generate valid password (no duplicate)
 * to be used in the Server.
 */
public interface PasswordGenerator {
    /**
     * Generate a password to be assigned to the player.
     * @return
     */
    public String generate();
}
