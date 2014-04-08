package ubet.sv;

import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import ubet.api.Bets;
import ubet.api.Games;
import ubet.api.Teams;
import ubet.database.GamesDB;
import ubet.util.StringTemplate;

@Path("/games")
public class GameServer {

	
	@Path("/getgamesbyround")
	@GET
	@Produces("text/html; charset=ISO-8859-15")
	public Response getGames(@QueryParam("username") String username,
			@QueryParam("round") int round, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.GET_GAMES_BY_ROUND_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			List <GamesDB> games = Games.getGamesByRound(round);

			if (games != null) {
				values.put("status", 1);
				for (GamesDB game : games) {
					
					int firstTeamId = game.getFirstTeam();
					int secondTeamId = game.getSecondTeam();
					int scoreOne = game.getScoreOne();
					int scoreTwo = game.getScoreTwo();
					String date = game.getDate().toString();
					String firstTeamName = Teams.getTeamName(firstTeamId);
					String secondTeamName = Teams.getTeamName(secondTeamId);
					
					HashMap<String, Object> gameHash = new HashMap<String, Object>();
					gameHash.put("gameid", game.getId());
					gameHash.put("first_team", firstTeamId);
					gameHash.put("second_team", secondTeamId);
					gameHash.put("score_one", scoreOne);
					gameHash.put("score_two", scoreTwo);
					gameHash.put("first_team_name", firstTeamName);
					gameHash.put("second_team_name", secondTeamName);
					gameHash.put("date", date);
					
					output += (new StringTemplate(Templates.GET_GAMES_BY_ROUND_LIST_TLP))
							.getString(gameHash);
				}
			}
			
			responseStatus = Response.Status.ACCEPTED;
		}

		return Response.status(responseStatus).entity(output).build();
	}
}
