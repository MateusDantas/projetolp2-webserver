package ubet.api;

import java.sql.SQLException;
import java.util.List;

import ubet.database.RoomsDB;
import ubet.database.UserDB;
import ubet.util.Variables;

public abstract class Users {

	/**
	 * You know what Tries to create an user
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
	public static int createUser(String firstName, String secondName,
			String email, String nickname, String password, int language)
			throws SQLException {

		Variables nowLanguage;
		if (language == Variables.ENGLISH.getValue())
			nowLanguage = Variables.ENGLISH;
		else
			nowLanguage = Variables.PORTUGUESE;
		UserDB newUser = new UserDB(firstName, secondName, nickname, email,
				password, nowLanguage, Variables.DEFAULT_COINS.getValue());

		int result = newUser.addUser().getValue();
		if (result != Variables.SUCCESS.getValue())
			return result;
		
		List <RoomsDB> rooms = Rooms.getRoomsByUser("master");
		if (rooms == null)
			return result;
		
		for (RoomsDB room : rooms) {
			Rooms.enterToRoom(nickname, room.getHashedPassword(), room.getRoomId());
		}
		
		return result;
	}

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @param password
	 *            User`s password
	 * @return true if user`s was successfully authenticated
	 * @throws SQLException
	 */
	public static boolean loginUser(String nickname, String password)
			throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return false;

		return newUser.autenticatePassword(password);
	}


	/**
	 * 
	 * @return
	 */
	public static List<UserDB> getAllUsers() {
		
		UserDB newUser = new UserDB();
		
		return newUser.getAllUsers();
	}
	
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
	public static int changePassword(String nickname, String newPassword)
			throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return Variables.INVALID_ID.getValue();

		newUser.setPassword(newPassword);

		return newUser.updateUser().getValue();
	}

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @return The number of coins that the user`s has
	 * @throws SQLException
	 */
	public static int getCoins(String nickname) throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return Variables.INVALID_ID.getValue();

		return newUser.getCoins();
	}

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @return The score of user
	 * @throws SQLException
	 */
	public static int getScore(String nickname) throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return Variables.INVALID_ID.getValue();

		return newUser.getPoints();
	}

	/**
	 * 
	 * @param nickname
	 * @return
	 * @throws SQLException
	 */
	public static int getUserId(String nickname) throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return Variables.INVALID_ID.getValue();

		return newUser.getId();
	}

	public static String getNickname(int userid) throws SQLException {

		UserDB newUser = new UserDB();

		if (newUser.getUserById(userid, true) == Variables.INVALID_ID
				.getValue())
			return "";

		return newUser.getNickname();
	}

	/**
	 * 
	 * @param nickname
	 * @param newCoins
	 * @return
	 * @throws SQLException
	 */
	protected static boolean setCoins(String nickname, int newCoins)
			throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return false;

		newUser.setCoins(newCoins);

		if (newUser.updateUser() == Variables.SUCCESS)
			return true;

		return false;
	}

	/**
	 * 
	 * @param nickname
	 * @param newScore
	 * @return
	 * @throws SQLException
	 */
	protected static boolean setScore(String nickname, int newScore)
			throws SQLException {

		UserDB newUser = new UserDB(nickname);

		if (newUser.getUserByNickname(nickname, true) == Variables.INVALID_ID
				.getValue())
			return false;

		newUser.setPoints(newScore);

		if (newUser.updateUser() == Variables.SUCCESS)
			return true;

		return false;
	}

}