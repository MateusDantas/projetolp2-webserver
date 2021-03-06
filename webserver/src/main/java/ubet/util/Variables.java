package ubet.util;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public enum Variables {

	// OPTIONS
	PORTUGUESE(1), ENGLISH(2), NORMAL_BET(3), MIX_BET(4), PLAYER_BET(5), GENERAL_ADMIN(
			0), INVALID_ID(-1), DEFAULT_POINTS(0),
	// OPTIONS BET
	MIN_BET(0), MAX_BET(1000), DEFAULT_COINS(1000), LIM_ROOMS(5), LIM_BET_DATE(
			30 * 60000),
	// ERRORS
	USERNAME_EXISTS(3), EMAIL_EXISTS(4), INVALID_EMAIL(5), INVALID_USERNAME(6), SUCCESS(
			7), CONNECTION_ERROR(8), UNKNOWN_ERROR(9), SERVER_DOWN(10), DEFAULT(
			11), SQL_ERROR(12), INVALID_NAME(13), INVALID_PASSWORD(14), USER_DOES_NOT_EXIST(
			15), INVALID_TYPE(16), INVALID_ADMIN(17), INVALID_MIN_BET(18), INVALID_STATUS(
			19), BET_EXISTS(20), NOT_PLAYING(0), PLAYING(1), MAX_LENGTH(49), INVALID_BET(
			21), INVALID_GAME(22), INVALID_ROUND(23), ROUND_TEAM(0), SCORER(1), INVALID_PLAYER(
			24), LIM_ROOMS_EXCEEDED(25), INTERNAL_ERROR(26), INSUFFICIENT_COINS(
			27), UNAUTHORIZED(28), LIM_EXTRA_BETS_EXCEEDED(29), INVALID_ROOM_ID(30), TIME_IS_UP(31);
	private int value;

	/**
	 * Constructor for Variables.
	 * @param value int
	 */
	private Variables(int value) {
		this.setValue(value);
	}

	/**
	 * Method getValue.
	
	 * @return int */
	public int getValue() {
		return value;
	}

	/**
	 * Method setValue.
	 * @param value int
	 */
	public void setValue(int value) {
		this.value = value;
	}
}