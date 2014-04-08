package ubet.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class GamesDB extends Database {
	private static final String INSERT_GAMES = "INSERT INTO games (first_team, second_team, round, score_one, score_two, date) VALUES(?, ?, ?, ?, ?, ?)";
	private static final String UPDATE_GAMES = "UPDATE games SET first_team=?, second_team=?, round=?, score_one=?,score_two=?, date=? WHERE id=?";
	private static final String GET_BY_TEAMS = "SELECT * FROM games WHERE first_team=? AND second_team=? AND round=?";
	private static final String GET_BY_ROUND = "SELECT * FROM games WHERE round=?";
	private static final String GET_BY_ID = "SELECT * FROM games WHERE id=?";

	private static final int DEFAULT_SCORE = -1;

	private String link;
	private int id, firstTeam, secondTeam, round;
	private int scoreOne, scoreTwo;
	private Date date;

	public GamesDB() {

	}

	/**
	 * Constructor for GamesDB.
	 * 
	 * @param id
	 *            int
	
	 * @param firstTeam
	 *            int
	 * @param secondTeam
	 *            int
	 * @param round
	 *            int
	 * @param date
	 *            Date
	 */
	public GamesDB(int id, int firstTeam, int secondTeam, int round, Date date) {
		setId(id);
		setFirstTeam(firstTeam);
		setSecondTeam(secondTeam);
		setScoreOne(DEFAULT_SCORE);
		setScoreTwo(DEFAULT_SCORE);
		setRound(round);
		setDate(date);
	}

	/**
	 * 
	
	 * @param firstTeam
	 * @param secondTeam
	 * @param round
	 * @param date
	 */
	public GamesDB(int firstTeam, int secondTeam, int round, Date date) {

		setScoreOne(DEFAULT_SCORE);
		setScoreTwo(DEFAULT_SCORE);
		setFirstTeam(firstTeam);
		setSecondTeam(secondTeam);
		setRound(round);
		setDate(date);
	}

	/**
	 * Constructor for GamesDB.
	 * @param firstTeam int
	 * @param secondTeam int
	 * @param scoreOne int
	 * @param scoreTwo int
	 * @param round int
	 * @param date Date
	 */
	public GamesDB(int firstTeam, int secondTeam, int scoreOne, int scoreTwo,
			int round, Date date) {
		setScoreOne(scoreOne);
		setScoreTwo(scoreTwo);
		setFirstTeam(firstTeam);
		setSecondTeam(secondTeam);
		setRound(round);
		setDate(date);
	}

	/**
	 * Constructor for GamesDB.
	 * @param id int
	 * @param firstTeam int
	 * @param secondTeam int
	 * @param scoreOne int
	 * @param scoreTwo int
	 * @param round int
	 * @param date Date
	 */
	public GamesDB(int id, int firstTeam, int secondTeam, int scoreOne,
			int scoreTwo, int round, Date date) {
		setId(id);
		setScoreOne(scoreOne);
		setScoreTwo(scoreTwo);
		setFirstTeam(firstTeam);
		setSecondTeam(secondTeam);
		setRound(round);
		setDate(date);
	}

	/**
	 * Method addGames.
	 * 
	 * 
	
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables addGames() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.firstTeam);
		newList.add(this.secondTeam);
		newList.add(this.round);
		newList.add(this.scoreOne);
		newList.add(this.scoreTwo);
		newList.add(this.date);

		return changeQuery(INSERT_GAMES, newList);
	}

	/**
	 * Method updatePlayer.
	 * 
	 * 
	
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables updateGame() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.firstTeam);
		newList.add(this.secondTeam);
		newList.add(this.round);
		newList.add(this.scoreOne);
		newList.add(this.scoreTwo);
		newList.add(this.date);
		newList.add(this.id);

		return changeQuery(UPDATE_GAMES, newList);
	}

	/**
	 * Method setGame.
	 * 
	 * @param firstTeam
	 *            int
	 * @param secondTeam
	 *            int
	 * 
	
	 * @param round int
	 * @return boolean */
	public boolean setGame(int firstTeam, int secondTeam, int round) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(firstTeam);
		newList.add(secondTeam);
		newList.add(round);

		List<GamesDB> game = getGame(GET_BY_TEAMS, newList);

		if (game == null || game.size() == 0)
			return false;

		setId(game.get(0).id);
		setScoreOne(game.get(0).scoreOne);
		setScoreTwo(game.get(0).scoreTwo);
		setFirstTeam(game.get(0).firstTeam);
		setSecondTeam(game.get(0).secondTeam);
		setRound(game.get(0).round);
		setDate(game.get(0).date);

		return true;
	}

	/**
	 * Method setGame.
	 * 
	
	 * @return boolean */
	public boolean setGame() {

		return setGame(this.id);
	}

	/**
	 * Method setGame.
	 * 
	 * @param gameId
	 *            int
	 * 
	
	 * @return boolean */
	public boolean setGame(int gameId) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(gameId);

		List<GamesDB> game = getGame(GET_BY_ID, newList);

		if (game == null || game.size() == 0)
			return false;

		setId(game.get(0).id);
		setScoreOne(game.get(0).scoreOne);
		setScoreTwo(game.get(0).scoreTwo);
		setFirstTeam(game.get(0).firstTeam);
		setSecondTeam(game.get(0).secondTeam);
		setRound(game.get(0).round);
		setDate(game.get(0).date);

		return true;
	}

	/**
	 * 
	 * @param round
	
	
	 * @return List<GamesDB> */
	public List<GamesDB> getGamesByRound(int round) {

		List<Object> newList = new ArrayList<Object>();
		newList.add(round);

		return getGame(GET_BY_ROUND, newList);
	}

	/**
	 * Method getGame.
	 * 
	 * @param pattern
	 *            String
	 * @param values
	 *            List<Object>
	 * 
	
	 * @return List<GamesDB> */
	public List<GamesDB> getGame(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);

		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return null;

		List<GamesDB> newList = new ArrayList<GamesDB>();

		for (int i = 0; i < result.size(); i++) {

			int getId = (Integer) result.get(i).get("id");
			int getFirstTeam = (Integer) result.get(i).get("first_team");
			int getSecondTeam = (Integer) result.get(i).get("second_team");
			int getRound = (Integer) result.get(i).get("round");
			int getScoreOne = (Integer) result.get(i).get("score_one");
			int getScoreTwo = (Integer) result.get(i).get("score_two");
			Date getDate = (Date) result.get(i).get("date");
			newList.add(new GamesDB(getId, getFirstTeam, getSecondTeam,
					getScoreOne, getScoreTwo, getRound, getDate));
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
	 * Method getId.
	 * 
	
	 * @return int */
	public int getId() {
		return id;
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
	 * Method getLink.
	 * 
	
	 * @return String */
	public String getLink() {
		return link;
	}

	/**
	 * Method setLink.
	 * 
	 * @param link
	 *            String
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Method getRound.
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
	 * Method getSecondTeam.
	 * 
	
	 * @return int */
	public int getSecondTeam() {
		return secondTeam;
	}

	/**
	 * Method setSecondTeam.
	 * 
	 * @param second_team
	 *            int
	 */
	public void setSecondTeam(int second_team) {
		this.secondTeam = second_team;
	}

	/**
	 * Method getFirstTeam.
	 * 
	
	 * @return int */
	public int getFirstTeam() {
		return firstTeam;
	}

	/**
	 * Method setFirstTeam.
	 * 
	 * @param first_team
	 *            int
	 */
	public void setFirstTeam(int first_team) {
		this.firstTeam = first_team;
	}

	/**
	 * Method getDate.
	 * 
	
	 * @return Date */
	public Date getDate() {
		return date;
	}

	/**
	 * Method setDate.
	 * 
	 * @param date
	 *            Date
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * Method getScoreTwo.
	
	 * @return int */
	public int getScoreTwo() {
		return scoreTwo;
	}

	/**
	 * Method setScoreTwo.
	 * @param scoreTwo int
	 */
	public void setScoreTwo(int scoreTwo) {
		this.scoreTwo = scoreTwo;
	}

	/**
	 * Method getScoreOne.
	
	 * @return int */
	public int getScoreOne() {
		return scoreOne;
	}

	/**
	 * Method setScoreOne.
	 * @param scoreOne int
	 */
	public void setScoreOne(int scoreOne) {
		this.scoreOne = scoreOne;
	}
}
