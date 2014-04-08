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
public class BetsDB extends Database {
	private static final String INSERT_BET_GAME = "INSERT INTO bets (user, room, round, game, score_one, score_two) VALUES(?,?,?,?,?,?)";
	private static final String UPDATE_BET_GAME = "UPDATE bets SET user=?, room=?, round=?, game=?, score_one=?, score_two=? WHERE id=?";
	private static final String GET_BY_ID = "SELECT * FROM bets WHERE id=?";
	private static final String GET_BY_USER_BY_GAME = "SELECT * FROM bets WHERE user=? AND room=? AND round=? AND game=? ORDER BY `id` DESC";
	private static final String GET_BY_USER_BY_ROOM = "SELECT * FROM bets WHERE user=? and room=? ORDER BY `id` DESC";
	private static final String GET_BY_GAME = "SELECT * FROM bets WHERE game=?";
	private static final String GET_BY_USER = "SELECT * FROM bets WHERE user=? ORDER BY `id` DESC";
	private static final String GET_ALL_BETS = "SELECT * FROM bets  ORDER BY `user` DESC, `room` DESC, `game` DESC";
	private int userId, roomId, round, gameId, id;
	private int scoreTeamOne, scoreTeamTwo;

	public BetsDB() {

	}

	/**
	 * Constructor for BetsDB.
	 * 
	 * @param userId
	 *            int User`s id
	 * @param roomId
	 *            int Room`s id
	 * @param round
	 *            int Round (1,2,3 - Qualifying Round, 4 - Round of 16, 5 -
	 *            Round of 6, 6 - Semi Finals, 7 - Finals)
	 * @param gameId
	 *            int Game`s id
	 * @param scoreOne
	 *            int Score of the first team
	 * @param scoreTwo
	 *            int Score of the second team
	 */
	public BetsDB(int userId, int roomId, int round, int gameId, int scoreOne,
			int scoreTwo) {
		this.setUserId(userId);
		this.setRoomId(roomId);
		this.setRound(round);
		this.setGameId(gameId);
		this.setScoreTeamOne(scoreOne);
		this.setScoreTeamTwo(scoreTwo);
	}

	/**
	 * Constructor for BetGamesDB.
	 * 
	 * @param id
	 *            Bet`s id
	 * 
	 * @param userId
	 *            int User`s id
	 * 
	 * @param round
	 *            int Round (1,2,3 - Qualifying Round, 4 - Round of 16, 5 -
	 *            Round of 6, 6 - Semi Finals, 7 - Finals)
	 * @param gameId
	 *            int Game`s id
	 * @param betId
	 *            int
	 */
	public BetsDB(int id, int userId, int betId, int round, int gameId) {
		this.setId(id);
		this.setUserId(userId);
		this.setRoomId(betId);
		this.setRound(round);
		this.setGameId(gameId);
	}

	/**
	 * Constructor for BetGamesDB.
	 * 
	 * @param id
	 *            Bet`s id
	 * 
	 * @param userId
	 *            int User`s id
	 * 
	 * @param round
	 *            int Round (1,2,3 - Qualifying Round, 4 - Round of 16, 5 -
	 *            Round of 6, 6 - Semi Finals, 7 - Finals)
	 * @param gameId
	 *            int Game`s id
	 * @param betId
	 *            int
	 * @param scoreOne
	 *            int
	 * @param scoreTwo
	 *            int
	 */
	public BetsDB(int id, int userId, int betId, int round, int gameId,
			int scoreOne, int scoreTwo) {
		this.setId(id);
		this.setUserId(userId);
		this.setRoomId(betId);
		this.setRound(round);
		this.setGameId(gameId);
		this.setScoreTeamOne(scoreOne);
		this.setScoreTeamTwo(scoreTwo);
	}

	/**
	 * Method addBetGame. Tries to add to the database the current bet
	 * 
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	public Variables addBetGame() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidUser(this.userId))
			return Variables.INVALID_ID;

		if (!isValidBet(this.roomId))
			return Variables.INVALID_BET;

		if (!isValidRound(this.round))
			return Variables.INVALID_ROUND;

		if (!isValidGame(this.gameId))
			return Variables.INVALID_GAME;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.userId);
		newList.add(this.roomId);
		newList.add(this.round);
		newList.add(this.gameId);
		newList.add(this.scoreTeamOne);
		newList.add(this.scoreTeamTwo);

		return changeQuery(INSERT_BET_GAME, newList);
	}

	/**
	 * Method updateBetGame. Tries to update the current bet
	 * 
	 * 
	 * 
	 * 
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	public Variables updateBetGame() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidUser(this.userId))
			return Variables.INVALID_ID;

		if (!isValidBet(this.roomId))
			return Variables.INVALID_BET;

		if (!isValidRound(this.round))
			return Variables.INVALID_ROUND;

		if (!isValidGame(this.gameId))
			return Variables.INVALID_GAME;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.userId);
		newList.add(this.roomId);
		newList.add(this.round);
		newList.add(this.gameId);
		newList.add(this.scoreTeamOne);
		newList.add(this.scoreTeamTwo);
		newList.add(this.id);

		return changeQuery(UPDATE_BET_GAME, newList);
	}

	/**
	 * Gets a list of all bets made by user, returns null if the user doesn't
	 * exist
	 * 
	 * @param userId
	
	 * @return List<BetsDB> */
	public List<BetsDB> getBet(int userId) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(userId);

		return getBet(GET_BY_USER, newList);
	}

	/**
	 * Get a list of all bets made by user inside room, returns null if the
	 * provided data doesn't match the database
	 * 
	 * @param userId
	 * @param roomId
	
	 * @return List<BetsDB> */
	public List<BetsDB> getBet(int userId, int roomId) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(userId);
		newList.add(roomId);

		return getBet(GET_BY_USER_BY_ROOM, newList);
	}

	/**
	 * Get a list of all bets made by user inside room in the (round, game), returns null if the
	 * provided data doesn't match the database
	 * @param userId
	 *            User's ID
	 * @param roomId
	 *            Room's ID
	 * @param round
	 *            Round number
	 * @param gameId
	 *            Game's ID
	 * 
	
	 * @return List of <BetsDB> of all bets made by some user */
	public List<BetsDB> getBet(int userId, int roomId, int round, int gameId) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(userId);
		newList.add(roomId);
		newList.add(round);
		newList.add(gameId);

		return getBet(GET_BY_USER_BY_GAME, newList);
	}

	/**
	 * Get all the bets made for game
	 * @param gameId
	 * 
	
	 * @return List<BetsDB> */
	public List<BetsDB> getBetsByGame(int gameId) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(gameId);

		return getBet(GET_BY_GAME, newList);
	}

	/**
	 * Get a list of all bets registered on the database
	 * 
	
	 * @return List<BetsDB> */
	public List<BetsDB> getAllBets() {

		List<Object> newList = new ArrayList<Object>();

		return getBet(GET_ALL_BETS, newList);
	}

	/**
	 * Set the current bet to bet's id
	 * 
	 * @param id
	 *            int Id of the bet
	 * 
	
	 * @return boolean true is successful, false otherwise */
	public boolean setBetGame(int id) {

		return setBet(id);
	}

	/**
	 * Set the current bet to the current bet's id
	 * 
	 * 
	
	 * @return boolean true is successful, false otherwise */
	public boolean setBetGame() {

		return setBet(this.id);
	}

	/**
	 * Given the id, set the current bet to this bet's id
	 * 
	 * @param id
	 *            int id of the bet
	 * 
	
	 * @return boolean true is successful, false otherwise */
	public boolean setBet(int id) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(id);

		List<BetsDB> betGame = getBet(GET_BY_ID, newList);

		if (betGame == null || betGame.size() == 0)
			return false;

		setId(betGame.get(0).id);
		setUserId(betGame.get(0).userId);
		setRoomId(betGame.get(0).roomId);
		setRound(betGame.get(0).round);
		setGameId(betGame.get(0).gameId);
		setScoreTeamOne(betGame.get(0).scoreTeamOne);
		setScoreTeamTwo(betGame.get(0).scoreTeamTwo);

		return true;
	}

	/**
	 * Return's a list of bets matching the given SQL Statement
	 * @param pattern
	 *            String MySQL Query Statement
	 * @param values
	 *            List<Object> Entries for the query statement
	 * 
	
	 * @return List<BetsDB> List of Bets that corresponds to the Query Statement */
	public List<BetsDB> getBet(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);

		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return null;

		List<BetsDB> newList = new ArrayList<BetsDB>();

		for (int i = 0; i < result.size(); i++) {
			int getUserId = (Integer) result.get(i).get("user");
			int getRoomId = (Integer) result.get(i).get("room");
			int getRound = (Integer) result.get(i).get("round");
			int getGameId = (Integer) result.get(i).get("game");
			int getScoreOne = (Integer) result.get(i).get("score_one");
			int getScoreTwo = (Integer) result.get(i).get("score_two");
			int getId = (Integer) result.get(i).get("id");

			newList.add(new BetsDB(getId, getUserId, getRoomId, getRound,
					getGameId, getScoreOne, getScoreTwo));
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
	 * Method isValidGame.
	 * 
	 * @param gameId2
	 *            int
	 * 
	
	 * @return boolean */
	private boolean isValidGame(int gameId2) {

		GamesDB newGame = new GamesDB();
		newGame.setId(gameId2);

		return newGame.setGame();
	}

	/**
	 * Method isValidRound.
	 * 
	 * @param round2
	 *            int
	 * 
	
	 * @return boolean */
	private boolean isValidRound(int round2) {

		if (round < 0 || round > 10)
			return false;
		return true;
	}

	/**
	 * Method isValidBet.
	 * 
	 * @param betId2
	 *            int
	 * 
	
	 * @return boolean */
	private boolean isValidBet(int betId2) {

		RoomsDB newBet = new RoomsDB();
		newBet.setRoomId(betId2);

		return newBet.setRoom();
	}

	/**
	 * Method isValidUser.
	 * 
	 * @param userId2
	 *            int
	 * 
	 * 
	 * 
	
	 * @return boolean * @throws SQLException * @throws SQLException * @throws
	 *         SQLException * @throws SQLException
	 */
	private boolean isValidUser(int userId2) throws SQLException {

		UserDB newUser = new UserDB();
		newUser.setId(userId2);

		return newUser.getUser() != Variables.INVALID_ID.getValue();
	}

	/**
	 * Method getGameId.
	 * 
	 * 
	
	 * @return int */
	public int getGameId() {
		return gameId;
	}

	/**
	 * Method setGameId.
	 * 
	 * @param gameId
	 *            int
	 */
	public void setGameId(int gameId) {
		this.gameId = gameId;
	}

	/**
	 * Method getUserId.
	 * 
	 * 
	
	 * @return int */
	public int getUserId() {
		return userId;
	}

	/**
	 * Method setUserId.
	 * 
	 * @param userId
	 *            int
	 */
	public void setUserId(int userId) {
		this.userId = userId;
	}

	/**
	 * Method getRound.
	 * 
	 * 
	
	 * @return int */
	public int getRound() {
		return round;
	}

	/**
	 * Method setRound.
	 * 
	 * @param round
	 *            int
	 */
	public void setRound(int round) {
		this.round = round;
	}

	/**
	 * Method getBetId.
	 * 
	 * 
	
	 * @return int */
	public int getRoomId() {
		return roomId;
	}

	/**
	 * Method setBetId.
	 * 
	 * @param roomId
	 *            int
	 */
	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	/**
	 * Method getScoreTeamTwo.
	 * 
	 * 
	
	 * @return int */
	public int getScoreTeamTwo() {
		return scoreTeamTwo;
	}

	/**
	 * Method setScoreTeamTwo.
	 * 
	 * @param scoreTeamTwo
	 *            int
	 */
	public void setScoreTeamTwo(int scoreTeamTwo) {
		this.scoreTeamTwo = scoreTeamTwo;
	}

	/**
	 * Method getScoreTeamOne.
	 * 
	 * 
	
	 * @return int */
	public int getScoreTeamOne() {
		return scoreTeamOne;
	}

	/**
	 * Method setScoreTeamOne.
	 * 
	 * @param scoreTeamOne
	 *            int
	 */
	public void setScoreTeamOne(int scoreTeamOne) {
		this.scoreTeamOne = scoreTeamOne;
	}

	/**
	 * Method setId.
	 * 
	 * @param id
	 *            int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method getId.
	 * 
	 * 
	
	 * @return int */
	public int getId() {
		return id;
	}
}
