package ubet.sv;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import ubet.api.Rooms;
import ubet.api.Users;
import ubet.auth.AuthToken;
import ubet.database.UserDB;
import ubet.util.StringTemplate;
import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
@Path("/users")
public class UserServer {

	public HashMap<String, Object> values = new HashMap<String, Object>();

	/**
	 * Method createUser.
	 * @param firstName String
	 * @param secondName String
	 * @param email String
	 * @param username String
	 * @param password String
	 * @param language int
	
	
	 * @return Response * @throws Exception */
	@Path("/createuser")
	@GET
	@Produces("text/html")
	public Response createUser(@QueryParam("firstname") String firstName,
			@QueryParam("secondname") String secondName,
			@QueryParam("email") String email,
			@QueryParam("username") String username,
			@QueryParam("password") String password,
			@QueryParam("language") int language) throws Exception {

		int result = Users.createUser(firstName, secondName, email, username,
				password, language);

		values.clear();
		values.put("status", result);
		values.put("token", 0);

		String output = (new StringTemplate(Templates.CREATE_USER_TLP)
				.getString(values));

		return Response.status(Response.Status.ACCEPTED).entity(output).build();
	}

	/**
	 * Method loginUser.
	 * @param username String
	 * @param password String
	
	
	 * @return Response * @throws Exception */
	@Path("/login")
	@GET
	@Produces("text/html")
	public Response loginUser(@QueryParam("username") String username,
			@QueryParam("password") String password) throws Exception {

		boolean result = Users.loginUser(username, password);

		values.clear();
		values.put("status", 0);
		values.put("token", 0);
		if (result) {

			String token = AuthTokenManager.getTokenUser(username);
			if (token == null) {
				token = RandomToken.generateToken();
				AuthToken userToken = new AuthToken(token, new Date(), username);
				AuthTokenManager.addToken(token, userToken);
			} else
				AuthTokenManager.authenticateToken(token);
			values.put("status", 1);
			values.put("token", token);
		}

		String output = (new StringTemplate(Templates.LOGIN_USER_TLP)
				.getString(values));

		if (result)
			return Response.status(Response.Status.ACCEPTED).entity(output)
					.build();
		else
			return Response.status(Response.Status.UNAUTHORIZED).entity(output)
					.build();
	}

	/**
	 * Method changePassword.
	 * @param username String
	 * @param newPassword String
	 * @param token String
	
	
	 * @return Response * @throws Exception */
	@Path("/changepassword")
	@GET
	@Produces("text/html")
	public Response changePassword(@QueryParam("username") String username,
			@QueryParam("password") String newPassword,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;

		values.clear();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int result = Users.changePassword(username, newPassword);
			values.put("status", result);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.CHANGE_PASSWORD_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * Method refreshToken.
	 * @param username String
	 * @param token String
	
	 * @throws Exception */
	@Path("/refreshtoken")
	@GET
	@Produces("text/html")
	public void refreshToken(@QueryParam("username") String username,
			@QueryParam("token") String token) throws Exception {
		if (AuthTokenManager.isUserAuthentic(token, username))
			AuthTokenManager.authenticateToken(token);
	}

	/**
	 * Method logoffUser.
	 * @param username String
	 * @param token String
	
	
	 * @return Response * @throws Exception */
	@Path("/logoff")
	@GET
	@Produces("text/html")
	public Response logoffUser(@QueryParam("username") String username,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;

		values.clear();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.removeAuthToken(token);
			AuthTokenManager.removeUserToken(username);
			values.put("status", 1);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.LOGOFF_USER_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * Method getCoins.
	 * @param username String
	 * @param token String
	 * @param requestedUser String
	
	
	 * @return Response * @throws Exception */
	@Path("/getcoins")
	@GET
	@Produces("text/html")
	public Response getCoins(@QueryParam("username") String username,
			@QueryParam("token") String token,
			@QueryParam("requested_user") String requestedUser)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;

		values.clear();
		values.put("status", 0);
		values.put("coins", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int coins = Users.getCoins(requestedUser);
			values.put("status", 1);
			values.put("coins", coins);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.GET_COINS_USER))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * Method getAllUsers.
	 * @param username String
	 * @param token String
	
	
	 * @return Response * @throws Exception */
	@Path("/getallusers")
	@GET
	@Produces("text/html")
	public Response getAllUsers(@QueryParam("username") String username,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.USERS_IN_ROOM_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {

			AuthTokenManager.authenticateToken(token);
			List<UserDB> usersInRoom = new ArrayList<UserDB>();

			usersInRoom = Users.getAllUsers();
			if (usersInRoom != null) {
				values.put("status", 1);
				output = (new StringTemplate(Templates.USERS_IN_ROOM_TLP))
						.getString(values);
				for (int i = 0; i < usersInRoom.size(); i++) {

					String nickname = usersInRoom.get(i).getNickname();
					int score = 0;
					int coins = usersInRoom.get(i).getCoins();

					HashMap<String, Object> nicknameHash = new HashMap<String, Object>();
					nicknameHash.put("username", nickname);
					nicknameHash.put("score", score);
					nicknameHash.put("coins", coins);
					output += (new StringTemplate(
							Templates.USERS_IN_ROOM_LIST_TLP))
							.getString(nicknameHash);
				}
			}
			responseStatus = Response.Status.ACCEPTED;
		}

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * Method getScore.
	 * @param username String
	 * @param token String
	 * @param requestedUser String
	
	
	 * @return Response * @throws Exception */
	@Path("/getscore")
	@GET
	@Produces("text/html")
	public Response getScore(@QueryParam("username") String username,
			@QueryParam("token") String token,
			@QueryParam("requested_user") String requestedUser)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;

		values.clear();
		values.put("status", 0);
		values.put("score", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int score = Users.getScore(requestedUser);
			values.put("status", 1);
			values.put("score", score);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.GET_SCORE_USER))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}
}
