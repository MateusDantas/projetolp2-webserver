package ubet.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ubet.util.BCrypt;
import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class RoomsDB extends Database {

	private static final String INSERT_ROOM = "INSERT INTO rooms (name, admin, password, price_room, price_extra, lim_extra) VALUES(?,?,?,?,?,?)";
	private static final String UPDATE_ROOM = "UPDATE rooms SET name=?, admin=?, password=?, price_room=?, price_extra=?, lim_extra=? WHERE id=?";
	private static final String GET_ROOM_BY_USER_ID = "SELECT * FROM rooms WHERE admin=? ORDER BY id DESC";
	private static final String GET_ROOM_BY_ID = "SELECT * FROM rooms WHERE id=?";
	private static final String GET_ALL_ROOMS = "SELECT * FROM rooms";
	private static final int MIN_ROOM_NAME_LENGTH = 1;
	private static final int MAX_ROOM_NAME_LENGTH = 15;
	private static final int FIRST_ELEMENT_INDEX = 0;

	private int roomId, adminId, priceRoom, priceExtra, limExtra;
	private String roomName, password;
	private String hashedPassword;

	public RoomsDB() {

	}

	/**
	 * Constructor for RoomsDB.
	 * 
	 * @param roomName
	 *            Name of the bet String
	 * @param betAdminId
	 *            Id of the user that created the bet, 0 if the room is public
	 *            int
	 * 
	 * @param betPriceExtra
	 *            int
	 * @param betLimExtra
	 *            int
	 * @param password
	 *            String
	 * @param betPriceRoom
	 *            int
	 */
	public RoomsDB(String roomName, int betAdminId, String password,
			int betPriceRoom, int betPriceExtra, int betLimExtra) {
		this.roomName = roomName;
		this.adminId = betAdminId;
		this.setPassword(password);
		this.priceRoom = betPriceRoom;
		this.priceExtra = betPriceExtra;
		this.limExtra = betLimExtra;
		this.setHashedPassword(hashPassword(password));
	}

	/**
	 * Constructor for RoomsDB.
	 * 
	 * @param roomId
	 *            int
	 * @param roomName
	 *            String
	 * 
	 * @param betAdminId
	 *            int
	 * @param betPriceRoom
	 *            int
	 * @param password
	 *            String
	 * @param betPriceExtra
	 *            int
	 * @param betLimExtra
	 *            int
	 */
	public RoomsDB(int roomId, String roomName, int betAdminId,
			String password, int betPriceRoom, int betPriceExtra,
			int betLimExtra) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.adminId = betAdminId;
		this.setPassword(password);
		this.priceRoom = betPriceRoom;
		this.priceExtra = betPriceExtra;
		this.limExtra = betLimExtra;
		this.setHashedPassword(hashPassword(password));
	}

	/**
	 * 
	 * @param roomId
	 * @param roomName
	 * @param betAdminId
	 * @param betPriceRoom
	 * @param betPriceExtra
	 * @param betLimExtra
	 * @param hashedPassword
	 */
	public RoomsDB(int roomId, String roomName, int betAdminId,
			int betPriceRoom, int betPriceExtra, int betLimExtra,
			String hashedPassword) {
		this.roomId = roomId;
		this.roomName = roomName;
		this.adminId = betAdminId;
		this.setPassword(hashedPassword);
		this.priceRoom = betPriceRoom;
		this.priceExtra = betPriceExtra;
		this.limExtra = betLimExtra;
		this.setHashedPassword(hashedPassword);
	}

	/**
	 * Method addRoom.
	 * 
	 * 
	 * 
	 * 
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	public Variables addRoom() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidRoomName(this.roomName))
			return Variables.INVALID_NAME;

		if (!isValidAdminId(this.adminId))
			return Variables.INVALID_ADMIN;

		if (!isValidPriceRoom(this.priceRoom, this.adminId))
			return Variables.INVALID_MIN_BET;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.roomName);
		newList.add(this.adminId);
		newList.add(this.hashedPassword);
		newList.add(this.priceRoom);
		newList.add(this.priceExtra);
		newList.add(this.limExtra);

		return changeQuery(INSERT_ROOM, newList);
	}

	/**
	 * 
	 * 
	 * 
	 * 
	
	 * @return Variables * @throws SQLException * @throws SQLException
	 */
	public Variables updateRoom() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidRoomName(this.roomName))
			return Variables.INVALID_NAME;

		if (!isValidAdminId(this.adminId))
			return Variables.INVALID_ADMIN;

		if (!isValidPriceRoom(this.priceRoom, this.adminId))
			return Variables.INVALID_MIN_BET;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.roomName);
		newList.add(this.adminId);
		newList.add(this.hashedPassword);
		newList.add(this.priceRoom);
		newList.add(this.priceExtra);
		newList.add(this.limExtra);
		newList.add(this.roomId);

		return changeQuery(UPDATE_ROOM, newList);
	}

	/**
	 * 
	
	
	 * @return int
	 * @throws SQLException */
	public int getLastInsertedId() throws SQLException {

		List<RoomsDB> list = getRoomsByUser(this.adminId);
		if (list == null)
			return Variables.INVALID_ID.getValue();

		return list.get(0).getRoomId();
	}

	/**
	 * Method getTotalBetsByUser.
	 * 
	 * @param userId
	 *            int
	 * 
	 * 
	 * 
	
	 * @return int * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	public int getTotalRoomsByUser(int userId) throws SQLException {

		List<RoomsDB> newList = getRoomsByUser(userId);
		if (newList == null)
			return 0;
		return newList.size();
	}

	/**
	 * 
	
	
	 * @return List<RoomsDB>
	 * @throws SQLException */
	public List<RoomsDB> getAllRooms() throws SQLException {
		
		List<Object> newList = new ArrayList<Object>();
		return getRooms(GET_ALL_ROOMS, newList);
	}
	
	/**
	 * Method getBetsByUser.
	 * 
	 * @param userId
	 *            int
	 * 
	 * 
	 * 
	
	 * @return List<BetsDB> * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	public List<RoomsDB> getRoomsByUser(int userId) throws SQLException {
		List<Object> newList = new ArrayList<Object>();
		newList.add(userId);

		return getRooms(GET_ROOM_BY_USER_ID, newList);
	}

	/**
	 * Method setRoom to get a room by id
	 * 
	 * 
	
	 * @return boolean */
	public boolean setRoom() {

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.roomId);

		List<RoomsDB> nowRoom = getRooms(GET_ROOM_BY_ID, newList);

		if (nowRoom == null)
			return false;

		if (nowRoom.size() == 0)
			return false;

		setRoomId(nowRoom.get(FIRST_ELEMENT_INDEX).roomId);
		setAdminId(nowRoom.get(FIRST_ELEMENT_INDEX).adminId);
		setRoomName(nowRoom.get(FIRST_ELEMENT_INDEX).roomName);
		setPriceRoom(nowRoom.get(FIRST_ELEMENT_INDEX).priceRoom);
		setPriceExtra(nowRoom.get(FIRST_ELEMENT_INDEX).priceExtra);
		setLimExtra(nowRoom.get(FIRST_ELEMENT_INDEX).getLimExtra());
		setHashedPassword(nowRoom.get(FIRST_ELEMENT_INDEX).hashedPassword);
		return true;
	}

	/**
	 * Method getBets.
	 * 
	 * @param pattern
	 *            String
	 * @param values
	 *            List<Object>
	 * 
	
	 * @return List<RoomsDB> */
	public List<RoomsDB> getRooms(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);
		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return null;

		List<RoomsDB> newList = new ArrayList<RoomsDB>();

		for (int i = 0; i < result.size(); i++) {
			String getRoomName = (String) result.get(i).get("name");
			int getId = (Integer) result.get(i).get("id");
			int getAdminId = (Integer) result.get(i).get("admin");
			String getPasswordRoom = (String) result.get(i).get("password");
			int getPriceRoom = (Integer) result.get(i).get("price_room");
			int getPriceExtra = (Integer) result.get(i).get("price_extra");
			int getLimExtra = (Integer) result.get(i).get("lim_extra");
			newList.add(new RoomsDB(getId, getRoomName, getAdminId,
					getPriceRoom, getPriceExtra, getLimExtra, getPasswordRoom));
		}
		try {
			if (newStatement != null)
				newStatement.close();
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}

		return newList;
	}

	/**
	 * Generates a hash string for a given String using a high level
	 * encryptation function
	 * 
	 * @param nowPassword
	 *            A password to be hashed
	 * 
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
	 * 
	
	 * @return True if textPassword is the same as this.currentPassword */
	public boolean autenticatePassword(String textPassword) {

		return autenticatePassword(textPassword, this.hashedPassword);
	}

	/**
	 * Method isValidPriceRoom
	 * 
	 * @param curPriceRoom
	 *            int
	 * @param curAdminId
	 *            int
	 * 
	
	 * @return boolean */
	private boolean isValidPriceRoom(int curPriceRoom, int curAdminId) {

		if (this.adminId == Variables.GENERAL_ADMIN.getValue())
			return true;

		return Variables.MIN_BET.getValue() <= curPriceRoom
				&& curPriceRoom <= Variables.MAX_BET.getValue();
	}

	/**
	 * Method isValidAdminId.
	 * 
	 * @param curAdminId
	 *            int
	 * 
	 * 
	 * 
	
	 * @return boolean * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	private boolean isValidAdminId(int curAdminId) throws SQLException {

		if (curAdminId == Variables.GENERAL_ADMIN.getValue())
			return true;

		UserDB newUser = new UserDB();
		return newUser.getUserById(curAdminId, true) != Variables.INVALID_ID
				.getValue();
	}

	/**
	 * Method isValidRoomName.
	 * 
	 * @param curRoomName
	 *            String
	 * 
	
	 * @return boolean */
	private boolean isValidRoomName(String curRoomName) {

		if (curRoomName.length() > MIN_ROOM_NAME_LENGTH
				&& curRoomName.length() < MAX_ROOM_NAME_LENGTH)
			return true;
		return false;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return int */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * 
	 * @param roomId
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return int */
	public int getPriceExtra() {
		return priceExtra;
	}

	/**
	 * 
	 * @param priceExtra
	 */
	public void setPriceExtra(int priceExtra) {
		this.priceExtra = priceExtra;
	}

	/**
	 * Method getLimExtra.
	 * 
	 * 
	
	 * @return int */
	public int getLimExtra() {
		return limExtra;
	}

	/**
	 * Method setLimExtra.
	 * 
	 * @param limExtra
	 *            int
	 */
	public void setLimExtra(int limExtra) {
		this.limExtra = limExtra;
	}

	/**
	 * Method getPriceRoom.
	 * 
	 * 
	
	 * @return int */
	public int getPriceRoom() {
		return priceRoom;
	}

	/**
	 * Method setPriceRoom.
	 * 
	 * @param priceRoom
	 *            int
	 */
	public void setPriceRoom(int priceRoom) {
		this.priceRoom = priceRoom;
	}

	/**
	 * Method getAdminId.
	 * 
	 * 
	
	 * @return int */
	public int getAdminId() {
		return adminId;
	}

	/**
	 * Method setAdminId.
	 * 
	 * @param adminId
	 *            int
	 */
	public void setAdminId(int adminId) {
		this.adminId = adminId;
	}

	/**
	 * Method getRoomName.
	 * 
	 * 
	
	 * @return String */
	public String getRoomName() {
		return roomName;
	}

	/**
	 * Method setRoomName.
	 * 
	 * @param roomName
	 *            String
	 */
	public void setRoomName(String roomName) {
		this.roomName = roomName;
	}

	/**
	 * 
	 * 
	 * 
	
	 * @return String */
	public String getPassword() {
		return password;
	}

	/**
	 * 
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
		setHashedPassword(hashPassword(password));
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
}
