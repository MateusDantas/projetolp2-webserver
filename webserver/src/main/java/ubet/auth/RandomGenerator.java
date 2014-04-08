package ubet.auth;

import java.util.UUID;

/**
 */
public class RandomGenerator {
	
	/**
	 * Randomly generates a string
	 * @return String
	 */
	public String randomString()  {
		
		return UUID.randomUUID().toString();
	}
}
