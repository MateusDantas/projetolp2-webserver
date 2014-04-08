package ubet.api;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import ubet.database.GamesDB;
import ubet.util.JsonReader;
import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class Games {

	private static final SimpleDateFormat formatter = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	/**
	 * Update all game`s from the link provided
	 * @param link String
	 * @param rodada int
	 * @param name String
	
	
	
	 * @throws IOException * @throws ParseException * @throws SQLException */
	public static void updateGames(String link, int rodada, String name)
			throws IOException, ParseException, SQLException {

		JSONObject json = JsonReader.getJson(link);

		JSONArray array = json.getJSONArray(name);

		for (int i = 0; i < array.length(); i++) {

			String firstTeamName = array.getJSONObject(i).getString("mandante");
			String secondTeamName = array.getJSONObject(i).getString(
					"visitante");
			String status = array.getJSONObject(i).getString("status");
			int firstTeamScore = array.getJSONObject(i)
					.getInt("placarMandante");
			int secondTeamScore = array.getJSONObject(i).getInt(
					"placarVisitante");
			String data = array.getJSONObject(i).getString("data");

			if (status.equals("Partida não iniciada"))
				firstTeamScore = secondTeamScore = -1;

			Date date = new SimpleDateFormat("dd/MM/yyyy HH'h'mm").parse(data);

			int firstTeamId = Teams.getTeamId(firstTeamName);
			int secondTeamId = Teams.getTeamId(secondTeamName);

			createGame(firstTeamId, secondTeamId, firstTeamScore, secondTeamScore, rodada, date);
		}

		
		System.out.println("Finished");
	}


	/**
	 * Create a game given the parameters
	 * @param firstTeamId int
	 * @param secondTeamId int
	 * @param scoreOne int
	 * @param scoreTwo int
	 * @param round int
	 * @param date Date
	
	
	 * @return boolean * @throws SQLException */
	public static boolean createGame(int firstTeamId, int secondTeamId,
			int scoreOne, int scoreTwo, int round, Date date)
			throws SQLException {

		GamesDB newGame = new GamesDB(firstTeamId, secondTeamId, scoreOne,
				scoreTwo, round, date);

		if (!newGame.setGame(firstTeamId, secondTeamId, round))
			return (newGame.addGames()) == Variables.SUCCESS;

		return false;
	}

	/**
	 * Get game by its ID
	 * @param id int
	
	 * @return GamesDB */
	public static GamesDB getGameById(int id) {

		GamesDB game = new GamesDB();
		game.setGame(id);

		return game;
	}

	/**
	 * Get a list of games by round
	 * @param round int
	
	 * @return List<GamesDB> */
	public static List<GamesDB> getGamesByRound(int round) {

		GamesDB game = new GamesDB();

		return game.getGamesByRound(round);
	}

}
