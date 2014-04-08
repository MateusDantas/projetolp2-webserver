package ubet.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import ubet.api.Bets;
import ubet.api.Rooms;
import ubet.api.Users;
import ubet.database.BetsDB;
import ubet.database.RoomsDB;
import ubet.database.UserDB;
import ubet.util.StringTemplate;

@Path("/bets")
public class BetServer {

	@Path("/makebet")
	@GET
	@Produces("text/html")
	public Response makeBet(@QueryParam("username") String username,
			@QueryParam("roomid") int roomId, @QueryParam("round") int round,
			@QueryParam("gameid") int gameId,
			@QueryParam("scoreone") int firstTeamScore,
			@QueryParam("scoretwo") int secondTeamScore,
			@QueryParam("isextrabet") int isExtraBet,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int result = Bets.makeBet(username, roomId, round, gameId,
					firstTeamScore, secondTeamScore, isExtraBet == 1);
			values.put("status", result);
			responseStatus = Response.Status.ACCEPTED;
		}
		String output = (new StringTemplate(Templates.MAKE_BET_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}
	
	@Path("/changebet")
	@GET
	@Produces("text/html")
	public Response makeBet(@QueryParam("username") String username,
			@QueryParam("betid") int betId,
			@QueryParam("scoreone") int firstTeamScore,
			@QueryParam("scoretwo") int secondTeamScore,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int result = Bets.changeBet(betId, firstTeamScore, secondTeamScore);
			values.put("status", result);
			responseStatus = Response.Status.ACCEPTED;
		}
		String output = (new StringTemplate(Templates.MAKE_BET_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	@Path("betsbyuserbyroom")
	@GET
	@Produces("text/html")
	public Response betsByUserByRoom(@QueryParam("username") String username,
			@QueryParam("roomid") int roomId, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(
				Templates.GET_BETS_BY_USER_BY_ROOM_TLP)).getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			List<BetsDB> listBets = Bets.betsByUserByRoom(username, roomId);
			if (listBets != null) {
				values.put("status", 1);
				output = (new StringTemplate(
						Templates.GET_BETS_BY_USER_BY_ROOM_TLP))
						.getString(values);
				for (int i = 0; i < listBets.size(); i++) {
					int gameid = listBets.get(i).getGameId();
					int scoreone = listBets.get(i).getScoreTeamOne();
					int scoretwo = listBets.get(i).getScoreTeamTwo();
					int betId = listBets.get(i).getId();
					HashMap<String, Object> nowBet = new HashMap<String, Object>();
					nowBet.put("betid", betId);
					nowBet.put("gameid", gameid);
					nowBet.put("scoreone", scoreone);
					nowBet.put("scoretwo", scoretwo);

					output += ((new StringTemplate(
							Templates.GET_BETS_BY_USER_BY_ROOM_LIST_TLP))
							.getString(nowBet));
				}
				responseStatus = Response.Status.ACCEPTED;
			}
		}

		return Response.status(responseStatus).entity(output).build();
	}
	
	@Path("/betsbyuserbygame")
	@GET
	@Produces("text/html")
	public Response betsByUserByGame(@QueryParam("username") String username,
			@QueryParam("gameid") int gameId, @QueryParam("roomid") int roomId,
			@QueryParam("round") int round, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(
				Templates.GET_BETS_BY_USER_BY_ROOM_TLP)).getString(values);
	
		if (AuthTokenManager.isUserAuthentic(token, username)) {
			
			AuthTokenManager.authenticateToken(token);
			List<BetsDB> listBets = Bets.betsByUserByGames(username, roomId,
					round, gameId);
			if (listBets != null) {
				values.put("status", 1);
				output = (new StringTemplate(
						Templates.GET_BETS_BY_USER_BY_ROOM_TLP))
						.getString(values);
				for (int i = 0; i < listBets.size(); i++) {
					int gameid = listBets.get(i).getGameId();
					int scoreone = listBets.get(i).getScoreTeamOne();
					int scoretwo = listBets.get(i).getScoreTeamTwo();
					int betId = listBets.get(i).getId();
					HashMap<String, Object> nowBet = new HashMap<String, Object>();
					nowBet.put("betid", betId);
					nowBet.put("gameid", gameid);
					nowBet.put("scoreone", scoreone);
					nowBet.put("scoretwo", scoretwo);


					output += ((new StringTemplate(
							Templates.GET_BETS_BY_USER_BY_ROOM_LIST_TLP))
							.getString(nowBet));
				}
				
			}
			responseStatus = Response.Status.ACCEPTED;
		}

		return Response.status(responseStatus).entity(output).build();
	}

	@Path("/betsbyuser")
	@GET
	@Produces("text/html")
	public Response betsByUser(@QueryParam("username") String username,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(
				Templates.GET_BETS_BY_USER_BY_ROOM_TLP)).getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			List<BetsDB> listBets = Bets.betsByUser(username);
			if (listBets != null) {
				values.put("status", 1);
				output = (new StringTemplate(
						Templates.GET_BETS_BY_USER_BY_ROOM_TLP))
						.getString(values);
				for (int i = 0; i < listBets.size(); i++) {
					int gameid = listBets.get(i).getGameId();
					int scoreone = listBets.get(i).getScoreTeamOne();
					int scoretwo = listBets.get(i).getScoreTeamTwo();
					int betId = listBets.get(i).getId();
					HashMap<String, Object> nowBet = new HashMap<String, Object>();
					nowBet.put("betid", betId);
					nowBet.put("gameid", gameid);
					nowBet.put("scoreone", scoreone);
					nowBet.put("scoretwo", scoretwo);


					output += ((new StringTemplate(
							Templates.GET_BETS_BY_USER_BY_ROOM_LIST_TLP))
							.getString(nowBet));
				}
				responseStatus = Response.Status.ACCEPTED;
			}
		}

		return Response.status(responseStatus).entity(output).build();
	}

}
