package ubet.api;

import java.sql.SQLException;

public interface UsersInterface {

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @param password
	 *            User`s password
	 * @return true if user`s was successfully authenticated
	 * @throws SQLException
	 */
	public boolean loginUser(String nickname, String password)
			throws SQLException;

	/**
	 * Tries to create an user
	 * 
	 * @param firstName
	 *            User`s first name
	 * @param secondName
	 *            User`s last name
	 * @param email
	 *            User`s email
	 * @param nickname
	 *            User`s nickname
	 * @param password
	 *            User`s password
	 * @param language
	 *            User`s language
	 * @return Variables of possible outcomes
	 * @throws SQLException
	 */
	public int createUser(String firstName, String secondName, String email,
			String nickname, String password, int language) throws SQLException;

	/**
	 * Tries to change`s user`s password
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @param newPassword
	 *            User`s new password
	 * @return Variables of possible outcomes
	 * @throws SQLException
	 */
	public int changePassword(String nickname, String newPassword)
			throws SQLException;

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @return The number of coins that the user`s has
	 * @throws SQLException
	 */
	public int getCoins(String nickname) throws SQLException;

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @return The score of user
	 * @throws SQLException
	 */
	public int getScore(String nickname) throws SQLException;

}
