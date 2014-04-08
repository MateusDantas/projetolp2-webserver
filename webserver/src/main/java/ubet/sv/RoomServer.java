package ubet.sv;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import ubet.api.Rooms;
import ubet.api.Users;
import ubet.database.RoomsDB;
import ubet.database.UserDB;
import ubet.util.StringTemplate;

/**
 */
@Path("/rooms")
public class RoomServer {

	/**
	 * 
	 * @param name
	 * @param username
	 * @param priceRoom
	 * @param priceExtra
	 * @param limExtra
	 * @param password
	 * @param token
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/createroom")
	@GET
	@Produces("text/html")
	public Response createRoom(@QueryParam("name") String name,
			@QueryParam("username") String username,
			@QueryParam("price_room") int priceRoom,
			@QueryParam("price_extra") int priceExtra,
			@QueryParam("lim_extra") int limExtra,
			@QueryParam("password") String password,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int result = Rooms.createRoom(name, username, priceRoom,
					priceExtra, limExtra, password);
			values.put("status", result);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.CREATE_ROOM_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * 
	 * @param username
	 * @param token
	 * @param requestedUser
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/getroomscreatedbyuser")
	@GET
	@Produces("text/html")
	public Response getRoomsCreatedByUser(
			@QueryParam("username") String username,
			@QueryParam("token") String token,
			@QueryParam("requested_user") String requestedUser)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.ROOMS_CREATED_BY_USER_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			List<RoomsDB> rooms = Rooms.getRoomsCreatedByUser(username);
			values.put("status", 1);
			if (rooms != null) {
				output = (new StringTemplate(
						Templates.ROOMS_CREATED_BY_USER_TLP)).getString(values);
				for (int i = 0; i < rooms.size(); i++) {
					HashMap<String, Object> hashList = new HashMap<String, Object>();
					hashList.put("name", rooms.get(i).getRoomName());
					hashList.put("roomid", rooms.get(i).getRoomId());
					hashList.put("admin",
							Users.getNickname(rooms.get(i).getAdminId()));
					hashList.put("room_price", rooms.get(i).getPriceRoom());
					hashList.put("people_inside",
							Rooms.getUsersInRoom(rooms.get(i).getRoomId())
									.size());
					hashList.put("priceextra", rooms.get(i).getPriceExtra());
					hashList.put("limextra", rooms.get(i).getLimExtra());
					output += (new StringTemplate(
							Templates.ROOMS_CREATED_BY_USER_LIST_TLP))
							.getString(hashList);
				}
			}
			responseStatus = Response.Status.ACCEPTED;
		}

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * 
	 * @param username
	 * @param password
	 * @param roomId
	 * @param token
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/entertoroom")
	@GET
	@Produces("text/html")
	public Response enterToRoom(@QueryParam("username") String username,
			@QueryParam("password") String password,
			@QueryParam("roomid") int roomId, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int result = Rooms.enterToRoom(username, password, roomId);
			values.put("status", result);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.ENTER_TO_ROOM_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * 
	 * @param username
	 * @param roomId
	 * @param token
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/usersinroom")
	@GET
	@Produces("text/html")
	public Response getUserInRoom(@QueryParam("username") String username,
			@QueryParam("roomid") int roomId, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.USERS_IN_ROOM_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username))
			responseStatus = Response.Status.ACCEPTED;

		if (AuthTokenManager.isUserAuthentic(token, username)
				&& Rooms.isUserInRoom(username, roomId)) {

			AuthTokenManager.authenticateToken(token);
			List<UserDB> usersInRoom = new ArrayList<UserDB>();
			
			usersInRoom = Rooms.getUsersInRoom(roomId);
			if (usersInRoom != null) {
				values.put("status", 1);
				output = (new StringTemplate(Templates.USERS_IN_ROOM_TLP))
						.getString(values);
				for (int i = 0; i < usersInRoom.size(); i++) {

					String nickname = usersInRoom.get(i).getNickname();
					int score = Rooms.getPointsByUserInRoom(nickname, roomId);
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
	 * 
	 * @param username
	 * @param token
	 * @param requestedUser
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/roomsbyuser")
	@GET
	@Produces("text/html")
	public Response getRoomsByUser(@QueryParam("username") String username,
			@QueryParam("token") String token,
			@QueryParam("requested_user") String requestedUser)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.ROOMS_BY_USER_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			responseStatus = Response.Status.ACCEPTED;
			AuthTokenManager.authenticateToken(token);
			List<RoomsDB> roomsList = Rooms.getRoomsByUser(requestedUser);
			if (roomsList != null) {
				values.put("status", 1);
				output = (new StringTemplate(Templates.ROOMS_BY_USER_TLP))
						.getString(values);

				for (int i = 0; i < roomsList.size(); i++) {
					roomsList.get(i).setRoom();
					String roomName = roomsList.get(i).getRoomName();
					int roomId = roomsList.get(i).getRoomId();
					String adminName = Users.getNickname(roomsList.get(i)
							.getAdminId());

					HashMap<String, Object> roomData = new HashMap<String, Object>();
					roomData.put("name", roomName);
					roomData.put("roomid", roomId);
					roomData.put("admin", adminName);
					roomData.put("room_price", roomsList.get(i).getPriceRoom());
					roomData.put("people_inside",
							Rooms.getUsersInRoom(roomsList.get(i).getRoomId())
									.size());
					roomData.put("priceextra", roomsList.get(i).getPriceExtra());
					roomData.put("limextra", roomsList.get(i).getLimExtra());

					output += (new StringTemplate(
							Templates.ROOMS_BY_USER_LIST_TLP))
							.getString(roomData);
				}
			}
		}

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * Method getAllRooms.
	 * @param username String
	 * @param token String
	 * @return Response
	 * @throws Exception
	 */
	@Path("/getallrooms")
	@GET
	@Produces("text/html")
	public Response getAllRooms(@QueryParam("username") String username,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);

		String output = (new StringTemplate(Templates.ALL_ROOMS_TLP))
				.getString(values);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			responseStatus = Response.Status.ACCEPTED;
			AuthTokenManager.authenticateToken(token);
			List<RoomsDB> roomsList = Rooms.getAllRooms();
			if (roomsList != null) {
				values.put("status", 1);
				output = (new StringTemplate(Templates.ALL_ROOMS_TLP))
						.getString(values);

				for (int i = 0; i < roomsList.size(); i++) {
					roomsList.get(i).setRoom();
					String roomName = roomsList.get(i).getRoomName();
					int roomId = roomsList.get(i).getRoomId();
					String adminName = Users.getNickname(roomsList.get(i)
							.getAdminId());

					HashMap<String, Object> roomData = new HashMap<String, Object>();
					roomData.put("name", roomName);
					roomData.put("roomid", roomId);
					roomData.put("admin", adminName);
					roomData.put("room_price", roomsList.get(i).getPriceRoom());
					roomData.put("people_inside",
							Rooms.getUsersInRoom(roomsList.get(i).getRoomId())
									.size());
					roomData.put("priceextra", roomsList.get(i).getPriceExtra());
					roomData.put("limextra", roomsList.get(i).getLimExtra());
					output += (new StringTemplate(Templates.ALL_ROOMS_LIST_TLP))
							.getString(roomData);
				}
			}
		}

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * 
	 * @param username
	 * @param roomId
	 * @param requestedUser
	 * @param token
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/pointsuser")
	@GET
	@Produces("text/html")
	public Response getPointsByUserInRoom(
			@QueryParam("username") String username,
			@QueryParam("roomid") int roomId,
			@QueryParam("requested_user") String requestedUser,
			@QueryParam("token") String token) throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);
		values.put("points", 0);

		if (AuthTokenManager.isUserAuthentic(token, username)) {
			AuthTokenManager.authenticateToken(token);
			int points = Rooms.getPointsByUserInRoom(requestedUser, roomId);
			values.put("points", points);
			responseStatus = Response.Status.ACCEPTED;
		}

		String output = (new StringTemplate(Templates.POINTS_BY_USER_ROOM_TLP))
				.getString(values);

		return Response.status(responseStatus).entity(output).build();
	}

	/**
	 * 
	 * @param username
	 * @param roomId
	 * @param token
	
	
	 * @return Response
	 * @throws Exception */
	@Path("/isuserinroom")
	@GET
	@Produces("text/html")
	public Response isUserInRoom(@QueryParam("username") String username,
			@QueryParam("roomid") int roomId, @QueryParam("token") String token)
			throws Exception {

		Response.Status responseStatus = Response.Status.UNAUTHORIZED;
		HashMap<String, Object> values = new HashMap<String, Object>();
		values.put("status", 0);
		if (AuthTokenManager.isUserAuthentic(token, username)) {
			if (Rooms.isUserInRoom(username, roomId))
				values.put("status", 1);
			responseStatus = Response.Status.ACCEPTED;
		}
		String output = (new StringTemplate(Templates.IS_USER_IN_ROOM_TLP))
				.getString(values);
		return Response.status(responseStatus).entity(output).build();
	}
}
