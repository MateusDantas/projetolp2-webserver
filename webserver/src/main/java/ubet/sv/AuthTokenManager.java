package ubet.sv;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import ubet.auth.AuthToken;

public class AuthTokenManager {

	private static final long TIME_TOKEN_LIMIT = 3600000L;
	private static final long REFRESH_PERIOD = 5000L;
	private static final Map<String, AuthToken> tokensMap = new HashMap<String, AuthToken>();
	private static final Map<String, String> usersTokens = new HashMap<String, String>();

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
	
	protected static boolean isUserAuthentic(String token, String username) {
		if (!isValidToken(token))
			return false;
		if (getAuthToken(token).getUsername().equals(username))
			return true;
		return false;
	}

	protected static void addToken(String token, AuthToken userToken) {
		synchronized (tokensMap) {
			tokensMap.put(token, userToken);
		}
		synchronized (usersTokens) {
			usersTokens.put(userToken.getUsername(), token);
		}
	}

	protected static void removeAuthToken(String token) {
		synchronized (tokensMap) {
			tokensMap.remove(token);
		}
	}
	
	protected static void removeUserToken(String username) {
		synchronized (usersTokens) {
			usersTokens.remove(username);
		}
	}

	protected static AuthToken getAuthToken(String token) {
		synchronized (tokensMap) {
			return tokensMap.get(token);
		}
	}
	
	protected static String getTokenUser(String username) {
		synchronized (usersTokens) {
			return usersTokens.get(username);
		}
	}

	protected static void removeExpiredTokens() {
		new Timer(true).schedule(timerExpiredTokens(), 0, REFRESH_PERIOD);
	}

	protected static TimerTask timerExpiredTokens() {
		return new TimerTask() {
			public void run() {
				checkExpiredTokens();
			}
		};
	}

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

	protected static boolean isValidToken(String token) {
		AuthToken authToken = getAuthToken(token);
		if (authToken == null)
			return false;
		if (isTokenExpired(authToken))
			return false;
		return true;
	}

	protected static boolean isTokenExpired(AuthToken authToken) {
		return (new Date().getTime()) - authToken.getLastDate().getTime() > TIME_TOKEN_LIMIT;
	}

}
