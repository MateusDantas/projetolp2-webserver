package ubet.auth;

import java.util.Date;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class AuthToken {

	private final String token;
	private final String username;
	private final Date firstTokenDate;
	private Date lastDate;

	/**
	 * Constructor for AuthToken.
	 * @param token String
	 * @param firstTokenDate Date
	 * @param username String
	 */
	public AuthToken(String token, Date firstTokenDate, String username) {
		this.token = token;
		this.firstTokenDate = firstTokenDate;
		this.username = username;
		this.lastDate = firstTokenDate;
	}

	/**
	 * Get object's token
	
	 * @return String */
	public String getToken() {
		return token;
	}

	/**
	 * Get object's username
	
	 * @return String */
	public String getUsername() {
		return username;
	}

	/**
	 * Method getFirstTokenDate.
	
	 * @return Date */
	public Date getFirstTokenDate() {
		return firstTokenDate;
	}

	/**
	 * Method getLastDate.
	
	 * @return Date */
	public Date getLastDate() {
		return lastDate;
	}

	/**
	 * Method setLastDate.
	 * @param lastDate Date
	 */
	public void setLastDate(Date lastDate) {
		this.lastDate = lastDate;
	}

}
