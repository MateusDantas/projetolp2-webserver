package ubet.api;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

import ubet.database.*;
import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public abstract class Bets {

	public final static int FIRST_TEAM = 1;
	public final static int SECOND_TEAM = 2;
	public final static int DRAW = 3;
	public final static int GROUP_ROUND = 1;
	public final static int FINAL_16 = 2;
	public final static int FINAL_8 = 3;
	public final static int SEMI_FINALS = 4;
	public final static int FINALS = 5;

	public final static int GROUP_ROUND_GAMES = 3;
	public final static int FINALS_16_GAMES = 4;
	public final static int FINALS_8_GAMES = 5;
	public final static int SEMI_FINALS_GAMES = 6;
	public final static int FINALS_GAMES = 7;

	public final static int GROUP_ROUND_MULTIPLIER = 1;
	public final static int FINAL_16_MULTIPLIER = 2;
	public final static int FINAL_8_MULTIPLIER = 4;
	public final static int SEMI_FINALS_MULTIPLIER = 8;
	public final static int FINALS_MULTIPLIER = 16;

	public final static String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss.SSS";

	/**
	 * 
	 * @param username
	 * @param roomId
	 * @param round
	 * @param gameId
	 * @param firstTeamScore
	 * @param secondTeamScore
	 * @param isExtraBet
	
	
	 * @return Variables * @throws SQLException * @throws SQLException
	 */
	public static int makeBet(String username, int roomId, int round,
			int gameId, int firstTeamScore, int secondTeamScore,
			boolean isExtraBet) throws SQLException {

		int userId = Users.getUserId(username);

		if (userId == Variables.INVALID_ID.getValue())
			return Variables.INVALID_ID.getValue();

		if (!Rooms.isUserInRoom(username, roomId))
			return Variables.UNAUTHORIZED.getValue();

		GamesDB newGame = new GamesDB();
		if (!newGame.setGame(gameId))
			return Variables.INVALID_GAME.getValue();
		Date gameDate = newGame.getDate();

		TimeZone timeZone = TimeZone.getTimeZone("GMT-3:00");
		Calendar c = Calendar.getInstance(timeZone);
		Date nowDate = c.getTime();

		if (gameDate.getTime() - nowDate.getTime() <= Variables.LIM_BET_DATE
				.getValue())
			return Variables.TIME_IS_UP.getValue();

		List<BetsDB> betsByUser = betsByUserByGames(username, roomId, round,
				gameId);

		if (betsByUser == null && isExtraBet)
			return Variables.INTERNAL_ERROR.getValue();

		if (betsByUser != null
				&& ((betsByUser.size() == 0 && isExtraBet) || (betsByUser
						.size() >= 1 && !isExtraBet)))
			return Variables.UNAUTHORIZED.getValue();

		if (isExtraBet) {
			int cost = Rooms.getRoomPriceExtra(roomId);
			int lim = Rooms.getRomExtraLim(roomId);

			if (lim == Variables.INVALID_ID.getValue()
					|| betsByUser.size() >= lim)
				return Variables.LIM_EXTRA_BETS_EXCEEDED.getValue();

			int coinsUser = Users.getCoins(username);

			if (cost > coinsUser)
				return Variables.INSUFFICIENT_COINS.getValue();

			coinsUser -= cost;

			BetsDB newBet = new BetsDB(userId, roomId, round, gameId,
					firstTeamScore, secondTeamScore);

			Variables result = newBet.addBetGame();

			if (result != Variables.SUCCESS)
				return result.getValue();

			Users.setCoins(username, coinsUser);

			return result.getValue();
		}

		BetsDB newBet = new BetsDB(userId, roomId, round, gameId,
				firstTeamScore, secondTeamScore);

		Variables result = newBet.addBetGame();

		if (result != Variables.SUCCESS)
			return result.getValue();

		return result.getValue();
	}

	/**
	 * Change user's bet
	 * 
	 * @param betId
	 * @param scoreOne
	 * @param scoreTwo
	 * @return Variables * @throws SQLException * @throws SQLException
	 */
	public static int changeBet(int betId, int scoreOne, int scoreTwo)
			throws SQLException {

		BetsDB newBet = new BetsDB();
		if (!newBet.setBet(betId)) {
			return Variables.INVALID_BET.getValue();
		}

		int gameId = newBet.getGameId();

		GamesDB game = new GamesDB();
		if (!game.setGame(gameId)) {
			return Variables.INVALID_BET.getValue();
		}

		TimeZone timeZone = TimeZone.getTimeZone("GMT-3:00");
		Calendar c = Calendar.getInstance(timeZone);
		Date nowDate = c.getTime();

		Date gameDate = game.getDate();

		if (gameDate.getTime() - nowDate.getTime() <= Variables.LIM_BET_DATE
				.getValue()) {
			return Variables.TIME_IS_UP.getValue();
		}

		newBet.setScoreTeamOne(scoreOne);
		newBet.setScoreTeamTwo(scoreTwo);

		return newBet.updateBetGame().getValue();
	}

	/**
	 * List of bets made by user in game
	 * 
	 * @param username
	 * @param roomId
	 * @param round
	 * @param gameId
	
	
	 * @return List<BetsDB> * @throws SQLException * @throws SQLException
	 */
	public static List<BetsDB> betsByUserByGames(String username, int roomId,
			int round, int gameId) throws SQLException {

		int userId = Users.getUserId(username);

		if (userId == Variables.INVALID_ID.getValue())
			return null;

		BetsDB newBets = new BetsDB();

		return newBets.getBet(userId, roomId, round, gameId);
	}

	/**
	 * List of bets made by user in room
	 * 
	 * @param username
	 * @param roomId
	
	
	 * @return List<BetsDB> * @throws SQLException * @throws SQLException
	 */
	public static List<BetsDB> betsByUserByRoom(String username, int roomId)
			throws SQLException {

		int userId = Users.getUserId(username);

		if (userId == Variables.INVALID_ID.getValue())
			return null;

		BetsDB newBets = new BetsDB();

		return newBets.getBet(userId, roomId);
	}

	/**
	 * List of all bets made by user
	 * 
	 * @param username
	
	
	 * @return List<BetsDB> * @throws SQLException * @throws SQLException
	 */
	public static List<BetsDB> betsByUser(String username) throws SQLException {

		int userId = Users.getUserId(username);

		if (userId == Variables.INVALID_ID.getValue())
			return null;

		BetsDB newBets = new BetsDB();

		return newBets.getBet(userId);
	}

	/**
	 * List of all bets in some game
	 * 
	 * @param gameId
	
	 * @return List<BetsDB> */
	protected static List<BetsDB> getBetsByGame(int gameId) {

		BetsDB newBets = new BetsDB();

		return newBets.getBetsByGame(gameId);
	}

	/**
	 * List of all bets
	 * 
	
	 * @return List<BetsDB> */
	protected static List<BetsDB> getAllBets() {

		BetsDB newBets = new BetsDB();

		return newBets.getAllBets();
	}

	/**
	 * Parse round (1,2,3 = GROUP_ROUND, 4 = FINAL_16, 5 = FINAL_8, 4 =
	 * SEMI_FINALS, 5 = FINALS)
	 * 
	 * @param round
	
	
	 * @return int */
	protected static int parseRound(int round) {

		if (round <= GROUP_ROUND_GAMES)
			return GROUP_ROUND;
		else if (round <= FINALS_16_GAMES)
			return FINAL_16;
		else if (round <= FINALS_8_GAMES)
			return FINAL_8;
		else if (round <= SEMI_FINALS_GAMES)
			return SEMI_FINALS;

		return FINALS;
	}

	/**
	 * Get the correct multiplier for each round
	 * @param round
	
	
	 * @return int */
	protected static int getRoundMultiplier(int round) {

		switch (parseRound(round)) {
		case GROUP_ROUND:
			return GROUP_ROUND_MULTIPLIER;
		case FINAL_16:
			return FINAL_16_MULTIPLIER;
		case FINAL_8:
			return FINAL_8_MULTIPLIER;
		case SEMI_FINALS:
			return SEMI_FINALS_MULTIPLIER;
		case FINALS:
			return FINALS_MULTIPLIER;
		default:
			return FINALS_MULTIPLIER;
		}
	}

	/**
	 * Calculate user`s point based on the bet he made
	 * @param scoreOne
	 * @param scoreTwo
	 * @param realScoreOne
	 * @param realScoreTwo
	 * @param round
	
	 * @return The user`s pontuation */
	protected static int calculatePoints(int scoreOne, int scoreTwo,
			int realScoreOne, int realScoreTwo, int round) {

		int realWinner, userWinner;

		if (realScoreOne > realScoreTwo)
			realWinner = FIRST_TEAM;
		else if (realScoreOne == realScoreTwo)
			realWinner = DRAW;
		else
			realWinner = SECOND_TEAM;

		if (scoreOne > scoreTwo)
			userWinner = FIRST_TEAM;
		else if (scoreOne == scoreTwo)
			userWinner = DRAW;
		else
			userWinner = SECOND_TEAM;

		int totalPoints = 0;

		if (realWinner == userWinner)
			totalPoints++;

		if (scoreOne == realScoreOne)
			totalPoints++;

		if (scoreTwo == realScoreTwo)
			totalPoints++;

		return totalPoints * getRoundMultiplier(round);
	}

	/**
	 * Update all user`s points
	
	 * @throws SQLException */
	public static void updatePoints() throws SQLException {

		List<BetsDB> allBets = getAllBets();

		if (allBets == null)
			return;

		int last_user = -1;
		int last_room = -1;
		int last_game = -1;
		int cur_points = 0;
		int max_points = 0;

		for (BetsDB bet : allBets) {
			int userId = bet.getUserId();
			int roomId = bet.getRoomId();
			int gameId = bet.getGameId();
			int scoreOne = bet.getScoreTeamOne();
			int scoreTwo = bet.getScoreTeamTwo();

			GamesDB game = Games.getGameById(gameId);

			int realScoreOne = game.getScoreOne();
			int realScoreTwo = game.getScoreTwo();

			if (realScoreOne == -1 || realScoreTwo == -1)
				continue;

			int totPoints = calculatePoints(scoreOne, scoreTwo, realScoreOne,
					realScoreTwo, game.getRound());

			if (userId == last_user && roomId == last_room) {

				if (gameId == last_game) {
					max_points = Math.max(max_points, totPoints);
				} else {
					cur_points += max_points;
					max_points = totPoints;
				}
			} else {

				Rooms.setPointsUserInRoom(last_user, last_room, cur_points
						+ max_points);
				cur_points = 0;
				max_points = totPoints;
			}

			last_user = userId;
			last_room = roomId;
			last_game = gameId;

		}

		Rooms.setPointsUserInRoom(last_user, last_room, cur_points + max_points);
	}
}
