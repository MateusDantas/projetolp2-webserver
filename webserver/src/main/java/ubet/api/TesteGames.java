package ubet.api;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

import ubet.database.TeamsDB;

public class TesteGames {

	private final static String URL_JOGOS = "http://futebol.statig.com.br/campeonatos/973/";
	private final static String URL_GRUPOS_1 = "grupos_1_jogos";
	private final static String URL_GRUPOS_2 = "grupos_2_jogos";
	private final static String URL_GRUPOS_3 = "grupos_3_jogos";
	private final static String URL_OITAVAS = "final_oitavas_de_final_jogos";
	private final static String URL_QUARTAS = "final_quartas_de_final_jogos";
	private final static String URL_SEMIS = "final_semifinal_jogos";
	private final static String URL_FINAL = "final_final_jogos";
	private final static String URL_EXTENSION = ".json";
	
	public static void main(String[] args) throws IOException, ParseException,
			SQLException {

		Games.updateGames(URL_JOGOS + URL_GRUPOS_1 + URL_EXTENSION, 1,
				URL_GRUPOS_1);
		Games.updateGames(URL_JOGOS + URL_GRUPOS_2 + URL_EXTENSION, 2,
				URL_GRUPOS_2);
		Games.updateGames(URL_JOGOS + URL_GRUPOS_3 + URL_EXTENSION, 3,
				URL_GRUPOS_3);
		Games.updateGames(URL_JOGOS + URL_OITAVAS + URL_EXTENSION, 4,
				URL_OITAVAS);
		Games.updateGames(URL_JOGOS + URL_QUARTAS + URL_EXTENSION, 5,
				URL_QUARTAS);
		Games.updateGames(URL_JOGOS + URL_SEMIS + URL_EXTENSION, 6,
				URL_SEMIS);
		Games.updateGames(URL_JOGOS + URL_FINAL + URL_EXTENSION, 7,
				URL_FINAL);
		
		Bets.updatePoints();
	}
}
