package ubet.api;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ubet.database.RoomsDB;
import ubet.database.UserDB;
import ubet.database.UserRoomDB;
import ubet.util.Variables;

public abstract class Rooms {

	/**
	 * 
	 * @param name
	 * @param username
	 * @param priceRoom
	 * @param priceExtra
	 * @param limExtra
	 * @param password
	 * @return
	 * @throws SQLException
	 */
	public static int createRoom(String name, String username, int priceRoom,
			int priceExtra, int limExtra, String password) throws SQLException {

		List<RoomsDB> roomsCreatedByUser = getRoomsCreatedByUser(username);

		int totalRoomsCreatedByUser = 0;
		if (roomsCreatedByUser != null)
			totalRoomsCreatedByUser = roomsCreatedByUser.size();

		if (totalRoomsCreatedByUser >= Variables.LIM_ROOMS.getValue())
			return Variables.LIM_ROOMS_EXCEEDED.getValue();

		int adminId = Users.getUserId(username);

		RoomsDB newRoom = new RoomsDB(name, adminId, password, priceRoom,
				priceExtra, limExtra);

		UserRoomDB newUserRoom = new UserRoomDB();
		int result = newRoom.addRoom().getValue();

		if (result != Variables.SUCCESS.getValue())
			return result;

		newUserRoom = new UserRoomDB(newRoom.getLastInsertedId(), adminId, 0);
		int result2 = newUserRoom.addUserRoom().getValue();

		if (result2 != Variables.SUCCESS.getValue())
			return result2;

		return result;
	}


	/**
	 * 
	 * @param roomId
	 * @return
	 */
	public static int getRomExtraLim(int roomId) {

		RoomsDB newQuery = new RoomsDB();
		newQuery.setRoomId(roomId);
		if (!newQuery.setRoom())
			return Variables.INVALID_ID.getValue();
		return newQuery.getLimExtra();
	}

	/**
	 * 
	 * @param roomId
	 * @return
	 */
	public static int getRoomPriceExtra(int roomId) {

		RoomsDB newQuery = new RoomsDB();
		newQuery.setRoomId(roomId);
		if (!newQuery.setRoom())
			return Variables.INVALID_ID.getValue();
		return newQuery.getPriceExtra();
	}

	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static List<RoomsDB> getRoomsCreatedByUser(String username)
			throws SQLException {

		int userId = Users.getUserId(username);
		RoomsDB newQuery = new RoomsDB();
		newQuery.setAdminId(userId);
		return newQuery.getRoomsByUser(userId);
	}

	/**
	 * 
	 * @param username
	 * @return
	 * @throws SQLException
	 */
	public static List<RoomsDB> getAllRooms() throws SQLException {

		RoomsDB newQuery = new RoomsDB();
		return newQuery.getAllRooms();
	}

	/**
	 * 
	 * @param username
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	public static boolean isUserInRoom(String username, int roomId)
			throws SQLException {

		int userId = Users.getUserId(username);
		UserRoomDB userRoom = new UserRoomDB();

		return userRoom.setUserRoom(userId, roomId);
	}

	/**
	 * 
	 * @param nickname
	 * @param password
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	public static int enterToRoom(String nickname, String password, int roomId)
			throws SQLException {

		RoomsDB newRoom = new RoomsDB();
		newRoom.setRoomId(roomId);

		if (!newRoom.setRoom())
			return Variables.INTERNAL_ERROR.getValue();

		if (!newRoom.autenticatePassword(password))
			return Variables.INVALID_PASSWORD.getValue();

		int userCoins = Users.getCoins(nickname);

		if (userCoins == Variables.INVALID_ID.getValue())
			return Variables.INTERNAL_ERROR.getValue();

		if (userCoins < newRoom.getPriceRoom())
			return Variables.INSUFFICIENT_COINS.getValue();

		int userId = Users.getUserId(nickname);

		if (userId == Variables.INVALID_ID.getValue())
			return Variables.INTERNAL_ERROR.getValue();

		UserRoomDB userRoom = new UserRoomDB(newRoom.getRoomId(), userId,
				Variables.DEFAULT_POINTS.getValue());

		if (userRoom.setUserRoom(userId, newRoom.getRoomId(), false))
			return Variables.PLAYING.getValue();
		
		int result = userRoom.addUserRoom().getValue();

		if (result == Variables.SUCCESS.getValue()
				&& !Users
						.setCoins(nickname, userCoins - newRoom.getPriceRoom()))
			return Variables.INTERNAL_ERROR.getValue();

		return result;
	}

	/**
	 * 
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	public static List<UserDB> getUsersInRoom(int roomId) throws SQLException {

		UserRoomDB newRoom = new UserRoomDB();
		List<UserRoomDB> usersInRoom = newRoom.getUsersInRoom(roomId);

		if (usersInRoom == null)
			return null;

		List<UserDB> usersList = new ArrayList<UserDB>();

		for (int i = 0; i < usersInRoom.size(); i++) {
			UserDB newUser = new UserDB();
			newUser.getUserById(usersInRoom.get(i).getUserId(), true);
			usersList.add(newUser);
		}

		return usersList;
	}

	/**
	 * 
	 * @param nickname
	 * @return
	 * @throws SQLException
	 */
	public static List<RoomsDB> getRoomsByUser(String nickname)
			throws SQLException {

		int userId = Users.getUserId(nickname);

		if (userId == Variables.INVALID_ID.getValue())
			return null;

		UserRoomDB newUserRoomQuery = new UserRoomDB();
		List<UserRoomDB> roomsByUser = newUserRoomQuery.getRoomsByUser(userId);

		if (roomsByUser == null)
			return null;

		List<RoomsDB> roomsList = new ArrayList<RoomsDB>();

		for (int i = 0; i < roomsByUser.size(); i++) {
			RoomsDB newRoom = new RoomsDB();
			if (roomsByUser.get(i).getRoomId() == Variables.INVALID_ID
					.getValue())
				return null;
			newRoom.setRoomId(roomsByUser.get(i).getRoomId());

			if (!newRoom.setRoom())
				return null;
			roomsList.add(newRoom);
		}
		return roomsList;
	}

	/**
	 * 
	 * @param nickname
	 * @param roomId
	 * @return
	 * @throws SQLException
	 */
	public static int getPointsByUserInRoom(String nickname, int roomId)
			throws SQLException {

		int userId = Users.getUserId(nickname);

		if (userId == Variables.INVALID_ID.getValue())
			return Variables.INVALID_ID.getValue();

		UserRoomDB newUserRoom = new UserRoomDB();
		if (!newUserRoom.setUserRoom(userId, roomId, true))
			return Variables.INTERNAL_ERROR.getValue();

		return newUserRoom.getPointsUserRoom();
	}

	
	protected static boolean setPointsUserInRoom(int userId, int roomId, int newPoints) throws SQLException {
		
		UserRoomDB newUserRoom = new UserRoomDB();
		if (!newUserRoom.setUserRoom(userId, roomId, true))
			return false;

		newUserRoom.setPointsUserRoom(newPoints);

		if (newUserRoom.updateUserRoom() == Variables.SUCCESS)
			return true;

		return false;
	}
	
	/**
	 * hmm
	 * 
	 * @param nickname
	 * @param roomId
	 * @param newPoints
	 * @throws SQLException
	 */
	protected static boolean setPointsUserInRoom(String nickname, int roomId,
			int newPoints) throws SQLException {

		int userId = Users.getUserId(nickname);

		if (userId == Variables.INVALID_ID.getValue())
			return false;

		return setPointsUserInRoom(userId, roomId, newPoints);
	}

	public static boolean addPointsUserInRoom(int userId, int roomId, int points)
			throws SQLException {

		UserRoomDB newUserRoom = new UserRoomDB();
		if (!newUserRoom.setUserRoom(userId, roomId, true))
			return false;

		newUserRoom.addPoints(points);

		if (newUserRoom.updateUserRoom() == Variables.SUCCESS)
			return true;

		return false;
	}
	
	public static boolean addPointsUserInRoom(String nickname, int roomId,
			int points) throws SQLException {

		int userId = Users.getUserId(nickname);

		if (userId == Variables.INVALID_ID.getValue())
			return false;

		return addPointsUserInRoom(userId, roomId, points);
	}

}
