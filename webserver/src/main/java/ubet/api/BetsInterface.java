package ubet.api;

import java.sql.SQLException;
import java.util.List;

import ubet.database.BetsDB;

public interface BetsInterface {

	/**
	 * 
	 * @param nickname
	 *            User's nickname
	 * @param roomId
	 *            Room's id
	 * @param round           
	 *            int Round (0 - Qualifying Round, 1 - Round of 16, 2 - Round of
	 *            4, 3 - Semi Finals, 4 - Finals)
	 * @param gameId
	 *            Game's id
	 * @param firstTeamScore
	 *            User's bet on the first team
	 * @param secondTeamScore
	 *            User's bet on the second team
	 * @param isExtraBet
	 *            If the bet the user's is trying to make is an extra one
	 * @return Variables of possibles outcomes
	 * @throws SQLException
	 */
	public int makeBet(String nickname, int roomId, int round, int gameId,
			int firstTeamScore, int secondTeamScore, boolean isExtraBet)
			throws SQLException;

	/**
	 * 
	 * @param nickname
	 *            User's nickname
	 * @param roomId
	 *            Room's id
	 * @param gameId
	 *            Game's id
	 * @return List of <BetsDB> of all bet`s made by user in game `gameId` in
	 *         room `roomId`
	 * @throws SQLException
	 */
	public List<BetsDB> betsByUserByGames(String nickname, int roomId, int round,
			int gameId) throws SQLException;

	/**
	 * 
	 * @param nickname
	 *            User`s nickname
	 * @param roomId
	 *            Room`s id
	 * @return List of <BetsDB> of all bet`s made by user in room `roomId`
	 * @throws SQLException
	 */
	public List<BetsDB> betsByUser(String nickname, int roomId)
			throws SQLException;
}
