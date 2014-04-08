package ubet.sv;

import ubet.auth.RandomGenerator;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class RandomToken {

	/**
	 * Method generateToken.
	
	 * @return String */
	public static String generateToken() {
		while (true) {
			String nowToken = (new RandomGenerator()).randomString();
			if (AuthTokenManager.getAuthToken(nowToken) == null)
				return nowToken;
		}
	}
}
