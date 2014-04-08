package ubet.sv;

import javax.ws.rs.ext.Provider;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
@Provider
public class Templates {

	public final static String CREATE_USER_TLP = "<div id='returnCode'>$(status)</div>"
			+ "<div id='authToken'>$(token)</div>";
	public final static String LOGIN_USER_TLP = "<div id='returnCode'>$(status)</div>"
			+ "<div id='authToken'>$(token)</div>";
	public static final String CHANGE_PASSWORD_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String LOGOFF_USER_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String GET_COINS_USER = "<div id='returnCode'>$(status)</div>"
			+ "<div id='coins'>$(coins)</div> ";
	public static final String GET_SCORE_USER = "<div id='returnCode'>$(status)</div>"
			+ "<div id='coins'>$(score)</div>";
	public static final String CREATE_ROOM_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String ROOMS_CREATED_BY_USER_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String ROOMS_CREATED_BY_USER_LIST_TLP = "<div class='room' name='$(name)' "
			+ " roomid=$(roomid) admin='$(admin)' roomprice=$(room_price) "
			+ " priceextra=$(priceextra) limextra=$(limextra) peopleinside=$(people_inside) />";
	public static final String ALL_ROOMS_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String ALL_ROOMS_LIST_TLP = "<div class='room' name='$(name)' "
			+ "roomid=$(roomid) admin='$(admin)' "
			+ " priceextra=$(priceextra) limextra=$(limextra) roomprice=$(room_price) peopleinside=$(people_inside) />";
	public static final String ENTER_TO_ROOM_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String USERS_IN_ROOM_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String USERS_IN_ROOM_LIST_TLP = "<div class='user' username='$(username)' score=$(score) coins=$(coins) />";
	public static final String ROOMS_BY_USER_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String ROOMS_BY_USER_LIST_TLP = "<div class='room' name='$(name)' "
			+ "roomid=$(roomid) admin='$(admin)'"
			+ " priceextra=$(priceextra) limextra=$(limextra) roomprice=$(room_price) peopleinside=$(people_inside) />";
	public static final String POINTS_BY_USER_ROOM_TLP = "<div id='returnCode'>$(status)</div>"
			+ "<div id='points'>$(points)</div>";
	public static final String MAKE_BET_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String GET_BETS_BY_USER_BY_ROOM_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String GET_BETS_BY_USER_BY_ROOM_LIST_TLP = "<div class='bets' "
			+ " betid=$(betid)"
			+ " gameid=$(gameid) scoreone=$(scoreone)"
			+ " scoretwo=$(scoretwo) />";

	public static final String IS_USER_IN_ROOM_TLP = "<div id='returnCode'>$(status)</div>";

	public static final String GET_GAMES_BY_ROUND_TLP = "<div id='returnCode'>$(status)</div>";
	public static final String GET_GAMES_BY_ROUND_LIST_TLP = "<div class='game' gameid=$(gameid)"
			+ " first_team=$(first_team)"
			+ " second_team=$(second_team) score_one=$(score_one)"
			+ " score_two=$(score_two)"
			+ " first_team_name="
			+ '"'
			+ "$(first_team_name)"
			+ '"'
			+ " second_team_name="
			+ '"'
			+ "$(second_team_name)" + '"' + " date='$(date)' />";

}
