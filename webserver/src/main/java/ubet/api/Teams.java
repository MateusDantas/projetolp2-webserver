package ubet.api;

import java.sql.SQLException;

import ubet.database.TeamsDB;
import ubet.util.Variables;

public class Teams {

	/**
	 * If team is not registered on the database, register it and return its
	 * Object otherwise just return the object
	 * 
	 * @param teamName
	 * @return TeamsDB
	 * @throws SQLException
	 */
	public static TeamsDB updateTeam(String teamName) throws SQLException {

		TeamsDB team = new TeamsDB();

		if (team.setTeam(teamName)) {
			return team;
		}

		team = new TeamsDB(teamName, "a", 0);
		if (team.addTeam() == Variables.SUCCESS) {
			team.setTeam(teamName);
			return team;
		}

		return null;
	}

	/**
	 * Returns team's id, if this team doesn't exist, then create it and return
	 * the created id
	 * 
	 * @param teamName
	 *            String
	 * @return int
	 * @throws SQLException
	 */
	public static int getTeamId(String teamName) throws SQLException {

		return updateTeam(teamName).getId();
	}

	/**
	 * Get team name based on the id, return null if the id doesn't match any
	 * team
	 * 
	 * @param teamId
	 *            int Team's ID
	 * @return String or null if the team doesn't exist
	 * @throws SQLException
	 */
	public static String getTeamName(int teamId) throws SQLException {

		TeamsDB team = new TeamsDB();

		if (team.setTeam(teamId))
			return team.getName();

		return null;
	}
}
