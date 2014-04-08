package ubet.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ubet.util.BCrypt;
import ubet.util.EmailValidator;
import ubet.util.Variables;

/**
 * Implements Database Management for User's class.
 * 
 * @author Mateus Dantas
 * @version 0.0.1
 */
public class UserDB extends Database {

	private static final String INSERT_USER = "INSERT INTO user (first_name, last_name, email, password, language, nickname, coins, points) VALUES(?,?,?,?,?,?,?,0)";
	private static final String UPDATE_USER = "UPDATE user SET password=?, language=?, email=?, points=?, coins=? WHERE id=?";
	private static final String GET_BY_ID = "SELECT * FROM user WHERE id=?";
	private static final String GET_BY_NICKNAME = "SELECT * FROM user WHERE nickname=?";
	private static final String GET_BY_EMAIL = "SELECT * FROM user WHERE email=?";
	private static final String GET_ALL_USERS = "SELECT * FROM user";
	private String firstName, lastName, nickname, email, originalPassword,
			hashedPassword;

	private int language, points, id = Variables.INVALID_ID.getValue(), coins;

	public UserDB() {

	}

	/**
	 * Set a User DB class for some user
	 * 
	 * @param firstName
	 *            User's first name
	 * @param lastName
	 *            User's last name
	 * @param nickname
	 *            User's nickname
	 * @param email
	 *            User's email
	 * @param password
	 *            User's password
	 * @param language
	 *            User's language
	 * 
	 * @param coins
	 *            int
	 */
	public UserDB(String firstName, String lastName, String nickname,
			String email, String password, Variables language, int coins) {
		setFirstName(firstName);
		setLastName(lastName);
		setNickname(nickname);
		setEmail(email);
		setPassword(password);
		setLanguage(language.getValue());
		setPoints(0);
		setCoins(coins);
		setHashedPassword(hashPassword(password));
	}

	/**
	 * 
	 * @param firstName
	 *            String
	 * @param lastName
	 *            String
	 * @param nickname
	 *            String
	 * @param email
	 *            String
	 * @param password
	 *            String
	 * @param language
	 *            Variables
	 */
	public UserDB(String firstName, String lastName, String nickname,
			String email, String password, Variables language) {
		setFirstName(firstName);
		setLastName(lastName);
		setNickname(nickname);
		setEmail(email);
		setPassword(password);
		setLanguage(language.getValue());
		setPoints(0);
		setCoins(0);
		setHashedPassword(hashPassword(password));
	}

	/**
	 * Constructor for UserDB.
	 * 
	 * @param nickname
	 *            String
	 */
	public UserDB(String nickname) {
		setNickname(nickname);
	}

	/**
	 * Tries to add the current user to the database Check whether the server is
	 * connected or not, if it's not then tries to connect
	 * 
	 * 
	 * 
	 * 
	
	
	 * @return Variables.(SERVER_DOWN, INVALID_USERNAME, INVALID_EMAIL,
	 *         INVALID_NAME, INVALID_PASSWORD, USERNAME_EXISTS, EMAIL_EXISTS) * @throws
	 *         SQLException * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables addUser() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.SERVER_DOWN;

		if (!isValidUsername(this.nickname))
			return Variables.INVALID_USERNAME;

		if (!isValidEmail(this.email))
			return Variables.INVALID_EMAIL;

		if (!isValidName(this.lastName) || !isValidName(this.firstName))
			return Variables.INVALID_NAME;

		if (!isValidPassword(this.originalPassword))
			return Variables.INVALID_PASSWORD;

		if (userExists(this.nickname))
			return Variables.USERNAME_EXISTS;

		if (userExists(this.email))
			return Variables.EMAIL_EXISTS;

		List<Object> newList = new ArrayList<Object>();
		System.out.println(this.hashedPassword);
		newList.add(this.firstName);
		newList.add(this.lastName);
		newList.add(this.email);
		newList.add(this.hashedPassword);
		newList.add(this.language);
		newList.add(this.nickname);
		newList.add(this.coins);
		return changeQuery(INSERT_USER, newList);
	}

	/**
	 * Tries to update the current user to the database
	 * 
	 * 
	 * 
	 * 
	
	
	 * @return Variables.(SERVER_DOWN, INVALID_USERNAME, INVALID_EMAIL,
	 *         INVALID_NAME, INVALID_PASSWORD,USER_DOES_NOT_EXIST,
	 *         USERNAME_EXISTS, EMAIL_EXISTS) * @throws SQLException * @throws
	 *         SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables updateUser() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.SERVER_DOWN;

		if (!isValidUsername(this.nickname))
			return Variables.INVALID_USERNAME;

		if (!isValidEmail(this.email))
			return Variables.INVALID_EMAIL;

		if (!isValidName(this.lastName) || !isValidName(this.firstName))
			return Variables.INVALID_NAME;

		if (!isValidPassword(this.originalPassword))
			return Variables.INVALID_PASSWORD;

		if (getUserById(this.id, false) == Variables.INVALID_ID.getValue())
			return Variables.USER_DOES_NOT_EXIST;

		if (getUserByEmail(this.email, false) != this.id)
			return Variables.EMAIL_EXISTS;

		if (getUserByNickname(this.nickname, false) != this.id)
			return Variables.USERNAME_EXISTS;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.hashedPassword);
		newList.add(this.language);
		newList.add(this.email);
		newList.add(this.points);
		newList.add(this.coins);
		newList.add(this.id);

		return changeQuery(UPDATE_USER, newList);
	}

	/**
	 * 
	 * @param str
	 *            Either an email adress or a nickname
	 * 
	 * 
	 * 
	
	
	 * @return True if exists such user, False otherwise * @throws SQLException
	 *         * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public boolean userExists(String str) throws SQLException {

		if (new EmailValidator().isEmailValid(str))
			return getUserByEmail(str, false) != Variables.INVALID_ID
					.getValue();
		return getUserByNickname(str, false) != Variables.INVALID_ID.getValue();
	}

	/**
	 * 
	 * @param email
	 *            User's email
	 * @param flagToUpdate
	 *            True if it's needed to update the current user, False
	 *            otherwise
	 * 
	 * 
	 * 
	
	
	 * @return The ID of such user * @throws SQLException * @throws SQLException
	 *         * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public int getUserByEmail(String email, boolean flagToUpdate)
			throws SQLException {

		List<Object> newList = new ArrayList<Object>();
		newList.add(email);
		return getUser(GET_BY_EMAIL, newList, flagToUpdate);
	}

	/**
	 * 
	 * @param nowNickname
	 *            User's nickname
	 * @param flagToUpdate
	 *            True if it's needed to update the current user, False
	 *            otherwise
	 * 
	 * 
	 * 
	
	
	 * @return The ID of such user * @throws SQLException * @throws SQLException
	 *         * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public int getUserByNickname(String nowNickname, boolean flagToUpdate)
			throws SQLException {

		if (!isValidUsername(nowNickname))
			return Variables.INVALID_NAME.getValue();
		List<Object> newList = new ArrayList<Object>();
		newList.add(nowNickname);
		return getUser(GET_BY_NICKNAME, newList, flagToUpdate);
	}

	/**
	 * 
	 * @param nowUserId
	 *            User's ID
	 * @param flagToUpdate
	 *            True if it's needed to update the current user, False
	 *            otherwise
	 * 
	 * 
	 * 
	
	
	 * @return The ID of such user * @throws SQLException * @throws SQLException
	 *         * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public int getUserById(int nowUserId, boolean flagToUpdate)
			throws SQLException {

		List<Object> newList = new ArrayList<Object>();
		newList.add(nowUserId);
		return getUser(GET_BY_ID, newList, flagToUpdate);
	}

	/**
	 * Get the user with the current ID
	 * 
	 * 
	 * 
	 * 
	
	
	 * @return The ID of such user or -1 if it doesn't exist * @throws
	 *         SQLException * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public int getUser() throws SQLException {

		if (this.id == Variables.INVALID_ID.getValue())
			return Variables.INVALID_ID.getValue();
		return getUserById(this.id, true);
	}

	/**
	 * 
	
	
	 * @return List<UserDB> */
	public List<UserDB> getAllUsers() {

		List<Object> newList = new ArrayList<Object>();

		return getUser(GET_ALL_USERS, newList);
	}

	/**
	 * 
	 * @param pattern
	 * @param values
	
	
	 * @return List<UserDB> */
	public List<UserDB> getUser(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);
		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return null;

		List<UserDB> listOfUsers = new ArrayList<UserDB>();

		for (int i = 0; i < result.size(); i++) {
			UserDB newUser = new UserDB();
			if (String.valueOf(result.get(i).get("nickname")).equals("master"))
				continue;
			newUser.setFirstName((String) result.get(i).get("first_name"));
			newUser.setLastName((String) result.get(i).get("last_name"));
			newUser.setNickname((String) result.get(i).get("nickname"));
			newUser.setEmail((String) result.get(i).get("email"));
			newUser.setPassword((String) result.get(i).get("password"));
			newUser.setHashedPassword((String) result.get(i).get("password"));
			newUser.setLanguage((Integer) result.get(i).get("language"));
			newUser.setPoints((Integer) result.get(i).get("points"));
			newUser.setCoins((Integer) result.get(i).get("coins"));
			newUser.setId((Integer) result.get(i).get("id"));
			listOfUsers.add(newUser);
		}
		try {
			if (newStatement != null)
				newStatement.close();
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}

		return listOfUsers;
	}

	/**
	 * 
	 * @param pattern
	 * @param values
	 * @param flagToUpdate
	 * 
	 * 
	 * 
	
	
	 * @return int * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public int getUser(String pattern, List<Object> values, boolean flagToUpdate)
			throws SQLException {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);
		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return -1;
		for (int i = 0; i < result.size() && flagToUpdate; i++) {
			setFirstName((String) result.get(i).get("first_name"));
			setLastName((String) result.get(i).get("last_name"));
			setNickname((String) result.get(i).get("nickname"));
			setEmail((String) result.get(i).get("email"));
			setPassword((String) result.get(i).get("password"));
			setHashedPassword((String) result.get(i).get("password"));
			setLanguage((Integer) result.get(i).get("language"));
			setPoints((Integer) result.get(i).get("points"));
			setCoins((Integer) result.get(i).get("coins"));
			setId((Integer) result.get(i).get("id"));
		}
		try {
			if (newStatement != null)
				newStatement.close();
		} catch (SQLException se) {
			se.printStackTrace();
			return -1;
		}
		return (Integer) result.get(0).get("id");
	}

	/**
	 * Generates a hash string for a given String using a high level
	 * encryptation function
	 * 
	 * @param nowPassword
	 *            A password to be hashed
	 * 
	
	 * @return The password's hash */
	public String hashPassword(String nowPassword) {

		String originalPassword = nowPassword;
		String hashedPassword = BCrypt.hashpw(originalPassword,
				BCrypt.gensalt(4));
		return hashedPassword;
	}

	/**
	 * Autenticates textPassword (in Plain Text) with hashedPassword
	 * 
	 * @param textPassword
	 *            Password in Plain Text to be compared
	 * @param hashedPassword
	 *            Password already hashed to be compared to textPassword
	 * 
	
	 * @return True if they're the same, False otherwise */
	public boolean autenticatePassword(String textPassword,
			String hashedPassword) {

		return BCrypt.checkpw(textPassword, hashedPassword);
	}

	/**
	 * Autenticates textPassword (in Plain Text) with the current Password
	 * 
	 * @param textPassword
	 *            Password in Plain Text to be compared
	 * 
	
	 * @return True if textPassword is the same as this.currentPassword */
	public boolean autenticatePassword(String textPassword) {

		return autenticatePassword(textPassword, this.hashedPassword);
	}

	/**
	 * 
	 * @param name
	 * 
	 * 
	
	 * @return boolean */
	public boolean isValidName(String name) {

		if (name == null)
			return false;
		return true;
	}

	/**
	 * 
	 * @param password
	 * 
	 * 
	
	 * @return boolean */
	public boolean isValidPassword(String password) {

		if (password == null)
			return false;
		return true;
	}

	/**
	 * 
	 * @param nowEmail
	 * 
	 * 
	
	 * @return boolean */
	public boolean isValidEmail(String nowEmail) {

		if (nowEmail == null)
			return false;
		if (!(new EmailValidator().isEmailValid(nowEmail)))
			return false;

		return true;
	}

	/**
	 * 
	 * @param nowNickname
	 * 
	 * 
	
	 * @return boolean */
	public boolean isValidUsername(String nowNickname) {

		if (nowNickname == null)
			return false;

		if (nowNickname.length() <= 3) {
			return false;
		}
		
		if (new EmailValidator().isEmailValid(nowNickname))
			return false;

		return true;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * 
	 * @param firstName
	 */
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getLastName() {
		return lastName;
	}

	/**
	 * 
	 * @param lastName
	 */
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getPassword() {
		return originalPassword;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.originalPassword = password;
		setHashedPassword(hashPassword(password));
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getEmail() {
		return email;
	}

	/**
	 * 
	 * @param email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getNickname() {
		return nickname;
	}

	/**
	 * 
	 * @param nickname
	 */
	private void setNickname(String nickname) {
		this.nickname = nickname;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return int */
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return int */
	public int getLanguage() {
		return language;
	}

	/**
	 * 
	 * @param language
	 */
	public void setLanguage(int language) {
		this.language = language;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return int */
	public int getPoints() {
		return points;
	}

	/**
	 * 
	 * @param newPoints
	 */
	public void addPoints(int newPoints) {
		this.points += newPoints;
	}

	/**
	 * 
	 * @param points
	 */
	public void setPoints(int points) {
		this.points = points;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getHashedPassword() {
		return hashedPassword;
	}

	/**
	 * 
	 * @param hashedPassword
	 */
	private void setHashedPassword(String hashedPassword) {
		this.hashedPassword = hashedPassword;
	}

	/**
	 * 
	 * @param nowCoins
	 */
	public void setCoins(int nowCoins) {
		// TODO Auto-generated method stub
		this.coins = nowCoins;
	}

	/**
	 * Method getScore.
	 * 
	
	 * @return int */
	public int getCoins() {

		return coins;
	}

}