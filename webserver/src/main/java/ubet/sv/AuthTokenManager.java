package ubet.sv;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ubet.auth.AuthToken;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class AuthTokenManager {

	private static final long TIME_TOKEN_LIMIT = 3600000L;
	private static final long REFRESH_PERIOD = 5000L;
	private static final Map<String, AuthToken> tokensMap = new HashMap<String, AuthToken>();
	private static final Map<String, String> usersTokens = new HashMap<String, String>();

	/**
	 * Refresh the tokens authentication inside the server
	 * 
	 * @param token
	 *            String
	
	 * @throws Exception */
	public static void authenticateToken(String token) throws Exception {

		if (getAuthToken(token) == null) {
			throw new Exception("Requires Authentication");
		} else {
			getAuthToken(token).setLastDate(new Date());
		}
	}

	static {
		removeExpiredTokens();
	}

	/**
	 * Check if the given token is authorized to be used by the user
	 * 
	 * @param token
	 *            String
	 * @param username
	 *            String
	
	 * @return boolean True if yes, False otherwise */
	protected static boolean isUserAuthentic(String token, String username) {
		if (!isValidToken(token))
			return false;
		if (getAuthToken(token).getUsername().equals(username))
			return true;
		return false;
	}

	/**
	 * Add an AuthToken into the server
	 * 
	 * @param token
	 *            String
	 * @param userToken
	 *            AuthToken
	 */
	protected static void addToken(String token, AuthToken userToken) {
		synchronized (tokensMap) {
			tokensMap.put(token, userToken);
		}
		synchronized (usersTokens) {
			usersTokens.put(userToken.getUsername(), token);
		}
	}

	/**
	 * Remove a token from server
	 * 
	 * @param token
	 *            String
	 */
	protected static void removeAuthToken(String token) {
		synchronized (tokensMap) {
			tokensMap.remove(token);
		}
	}

	/**
	 * Remove all user's token from the server
	 * 
	 * @param username
	 *            String
	 */
	protected static void removeUserToken(String username) {
		synchronized (usersTokens) {
			usersTokens.remove(username);
		}
	}

	/**
	 * Get an AuthToken from the given token, null if it doesn't exist
	 * 
	 * @param token
	 *            String
	
	 * @return AuthToken */
	protected static AuthToken getAuthToken(String token) {
		synchronized (tokensMap) {
			return tokensMap.get(token);
		}
	}

	/**
	 * Get's an AuthToken authenticated to user, null if it doesn't exist
	 * 
	 * @param username
	 *            String
	
	 * @return String */
	protected static String getTokenUser(String username) {
		synchronized (usersTokens) {
			return usersTokens.get(username);
		}
	}

	/**
	 * Remove all expired tokens from server
	 */
	protected static void removeExpiredTokens() {
		new Timer(true).schedule(timerExpiredTokens(), 0, REFRESH_PERIOD);
	}

	/**
	 * Returns a timer that runs a check on all expired tokens
	 * 
	
	 * @return TimerTask */
	protected static TimerTask timerExpiredTokens() {
		return new TimerTask() {
			public void run() {
				checkExpiredTokens();
			}
		};
	}

	/**
	 * Check and remove all expired tokens
	 */
	protected static void checkExpiredTokens() {
		synchronized (tokensMap) {
			Iterator<AuthToken> iterator = tokensMap.values().iterator();

			while (iterator.hasNext()) {
				AuthToken authToken = iterator.next();
				if (isTokenExpired(authToken)) {
					usersTokens.remove(authToken.getUsername());
					iterator.remove();
				}
			}
		}
	}

	/**
	 * Check if a given token is valid or not
	 * 
	 * @param token
	 *            String
	
	 * @return boolean */
	protected static boolean isValidToken(String token) {
		AuthToken authToken = getAuthToken(token);
		if (authToken == null)
			return false;
		if (isTokenExpired(authToken))
			return false;
		return true;
	}

	/**
	 * Check if some AuthToken is expired
	 * 
	 * @param authToken
	 *            AuthToken
	
	 * @return boolean */
	protected static boolean isTokenExpired(AuthToken authToken) {
		return (new Date().getTime()) - authToken.getLastDate().getTime() > TIME_TOKEN_LIMIT;
	}

}
