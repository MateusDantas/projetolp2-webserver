package ubet.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class UserRoomDB extends UserDB {

	private static final String INSERT_USER_ROOM = "INSERT INTO userRoom (user, room, points) VALUES(?,?,?)";
	private static final String UPDATE_USER_ROOM = "UPDATE userRoom SET points=? WHERE id = ?";
	private static final String GET_BY_ROOM_AND_USER = "SELECT * FROM userRoom WHERE user=? AND room=?";
	private static final String GET_BY_ROOM = "SELECT * FROM userRoom WHERE room=?";
	private static final String GET_BY_USER = "SELECT * FROM userRoom WHERE user=?";
	private static final String GET_BY_ID = "SELECT * FROM userRoom WHERE id=?";

	private int userRoomId;
	private int userId;
	private int roomId;
	private int pointsUserRoom;

	public UserRoomDB() {

	}

	/**
	 * 
	 * @param userRoomId
	 * @param roomId
	 * @param userId
	 * @param points
	 * @param status
	 */
	public UserRoomDB(int userRoomId, int userId, int roomId, int points) {

		setUserRoomId(userRoomId);
		setUserId(userId);
		setRoomId(roomId);
		setPointsUserRoom(points);
	}

	/**
	 * 
	 * @param roomId
	 * @param userId
	 * @param points
	 * @param status
	 */
	public UserRoomDB(int roomId, int userId, int points) {

		setUserId(userId);
		setRoomId(roomId);
		setPointsUserRoom(points);
	}

	/**
	 * 
	
	
	
	 * @return Variables * @throws SQLException * @throws SQLException
	 */
	public Variables addUserRoom() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidUserId(this.userId))
			return Variables.INVALID_ID;

		if (!isValidRoomId(this.roomId))
			return Variables.INVALID_ROOM_ID;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.userId);
		newList.add(this.roomId);
		newList.add(this.pointsUserRoom);

		return changeQuery(INSERT_USER_ROOM, newList);
	}

	/**
	 * Method updateUserBet.
	
	
	 * @return Variables * @throws SQLException * @throws SQLException
	 */
	public Variables updateUserRoom() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidUserId(this.userId))
			return Variables.INVALID_ID;

		if (!isValidRoomId(this.roomId))
			return Variables.INVALID_ID;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.pointsUserRoom);
		newList.add(this.userRoomId);
		
		return changeQuery(UPDATE_USER_ROOM, newList);
	}

	/**
	 * Method setUserBet.
	 * @param userId2 int
	 * @param roomId2 int
	
	
	 * @return boolean * @throws SQLException * @throws SQLException
	 */
	public boolean setUserRoom(int userId2, int roomId2) throws SQLException {
		return setUserRoom(userId2, roomId2, true);
	}

	/**
	 * Method setUserBet.
	 * @param userId2 int
	 * @param roomId2 int
	 * @param flagToUpdate boolean
	
	
	 * @return boolean * @throws SQLException * @throws SQLException
	 */
	public boolean setUserRoom(int userId2, int roomId2, boolean flagToUpdate)
			throws SQLException {

		if (!isValidUserId(userId2))
			return false;
		if (!isValidRoomId(roomId2))
			return false;
		List<Object> newList = new ArrayList<Object>();
		newList.add(userId2);
		newList.add(roomId2);

		List<UserRoomDB> userBet = getUserRoom(GET_BY_ROOM_AND_USER, newList);

		if (userBet == null)
			return false;

		if (userBet.size() == 0)
			return false;

		if (flagToUpdate) {
			setUserRoomId(userBet.get(0).userRoomId);
			setUserId(userBet.get(0).userId);
			setRoomId(userBet.get(0).roomId);
			setPointsUserRoom(userBet.get(0).pointsUserRoom);
		}

		return true;
	}

	/**
	 * Method setUserBet.
	
	 * @return boolean */
	public boolean setUserRoom() {

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.userRoomId);

		List<UserRoomDB> userRoom = getUserRoom(GET_BY_ID, newList);

		if (userRoom == null)
			return false;

		if (userRoom.size() == 0)
			return false;

		setUserRoomId(userRoom.get(0).userRoomId);
		setUserId(userRoom.get(0).userId);
		setRoomId(userRoom.get(0).roomId);
		setPointsUserRoom(userRoom.get(0).pointsUserRoom);

		return true;
	}

	/**
	 * 
	 * @param roomId
	 * @return
	 */
	public List<UserRoomDB> getUsersInRoom(int roomId) {
		List <Object> newList = new ArrayList<Object>();
		newList.add(roomId);
		
		List<UserRoomDB> userRoom = getUserRoom(GET_BY_ROOM, newList);
		
		return userRoom;
	}
	
	/**
	 * 
	 * @param userId
	 * @return
	 */
	public List<UserRoomDB> getRoomsByUser(int userId) {
		List <Object> newList = new ArrayList<Object>();
		newList.add(userId);
		
		List<UserRoomDB> roomsUser = getUserRoom(GET_BY_USER, newList);
		
		return roomsUser;
	}
	
	/**
	 * Method getUserBet.
	 * @param pattern String
	 * @param values List<Object>
	
	 * @return List<UserBetDB> */
	public List<UserRoomDB> getUserRoom(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;

		newStatement = buildStatements(pattern, values);
		List<HashMap<String, Object>> result = getQuery(newStatement);
		if (result.size() == 0)
			return null;

		List<UserRoomDB> newList = new ArrayList<UserRoomDB>();
		for (int i = 0; i < result.size(); i++) {
			int nowUserBetId = (Integer) result.get(i).get("id");
			int nowUserId = (Integer) result.get(i).get("user");
			int nowRoomId = (Integer) result.get(i).get("room");
			int nowRoomPoints = (Integer) result.get(i).get("points");
			newList.add(new UserRoomDB(nowUserBetId, nowUserId, nowRoomId,
					nowRoomPoints));
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
	 * Method isValidBetId.
	 * @param betId2 int
	
	 * @return boolean */
	private boolean isValidRoomId(int betId2) {

		RoomsDB newBet = new RoomsDB();
		newBet.setRoomId(betId2);

		return newBet.setRoom();
	}

	/**
	 * Method isValidUserId.
	 * @param userId2 int
	
	
	 * @return boolean * @throws SQLException * @throws SQLException
	 */
	private boolean isValidUserId(int userId2) throws SQLException {

		UserDB newUser = new UserDB();
		return newUser.getUserById(userId2, true) != Variables.INVALID_ID
				.getValue();
	}

	/**
	 * Method getUserId.
	
	 * @return int */
	public int getUserId() {
		return userId;
	}

	/**
	 * Method setUserId.
	 * @param userId int
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Method getRoomId.
	
	 * @return int */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * Method setBetId.
	 * @param betId int
	 */
	public void setRoomId(int betId) {
		this.roomId = betId;
	}
	
	/**
	 * Method addPointsUserBet.
	 * @param newPoints int
	 */
	public void addPointsUserRoom(int newPoints) {
		this.pointsUserRoom += newPoints;
	}
	
	/**
	 * Method getPointsUserBet.
	
	 * @return int */
	public int getPointsUserRoom() {
		return pointsUserRoom;
	}

	/**
	 * Method setPointsUserBet.
	 * @param pointsUserRoom int
	 */
	public void setPointsUserRoom(int pointsUserRoom) {
		this.pointsUserRoom = pointsUserRoom;
	}

	/**
	 * Method setUserBetId.
	 * @param userBetId2 int
	 */
	public void setUserRoomId(int userBetId2) {
		// TODO Auto-generated method stub
		this.userRoomId = userBetId2;
	}
}
