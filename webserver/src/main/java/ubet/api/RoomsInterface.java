package ubet.api;

import java.sql.SQLException;
import java.util.List;

import ubet.database.RoomsDB;
import ubet.database.UserDB;

public interface RoomsInterface {

	/**
	 * 
	 * @param name
	 *            Name of the room
	 * @param nickname
	 *            Nickname of the user's admin
	 * @param priceBet
	 *            Price of entering in the room
	 * @param priceExtra
	 *            Price of buying another bet on this room
	 * @param limExtra
	 *            Limit of extra bets per user
	 * @param password
	 *            Password of the room, empty if the room is public
	 * @return Variables of possible outcomes
	 * @throws SQLException
	 */
	public int createRoom(String name, String nickname, int priceBet,
			int priceExtra, int limExtra, String password) throws SQLException;

	/**
	 * 
	 * @param nickname User's nickname
	 * @return List of all rooms owned by user
	 * @throws SQLException
	 */
	public List<RoomsDB> getRoomsCreatedByUser(String nickname) throws SQLException;
	
	/**
	 * Sign in some user into some room
	 * 
	 * @param nickname
	 *            Nickname of the user's who wants to join the room
	 * @param password
	 *            Password given by the user's to the room
	 * @param roomId
	 *            ID of the room that the user's wants to enter
	 * @return Variables of possible outcomes
	 * @throws SQLException
	 */
	public int enterToRoom(String nickname, String password, int roomId)
			throws SQLException;

	/**
	 * List of all user's on some room
	 * 
	 * @param roomId
	 *            Room's ID
	 * @return A list of <UserDB> of all user's signed in to the room
	 * @throws SQLException
	 */
	public List<UserDB> getUsersInRoom(int roomId) throws SQLException;

	/**
	 * List of all rooms signed in by the user
	 * 
	 * @param nickname
	 *            Nickname of the user
	 * @return A list of <RoomsDB> of all rooms by sme user
	 * @throws SQLException
	 */
	public List<RoomsDB> getRoomsByUser(String nickname) throws SQLException;

	/**
	 * 
	 * @param nickname
	 *            User's nickname
	 * @param roomId
	 *            ID of the room
	 * @return The total number of points made by the user in room with roomId
	 * @throws SQLException
	 */
	public int getPointsByUser(String nickname, int roomId) throws SQLException;

	
}
