package ubet.auth;

import java.util.UUID;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class RandomGenerator {
	
	/**
	 * Randomly generates a string
	
	 * @return String */
	public String randomString()  {
		
		return UUID.randomUUID().toString();
	}
}
