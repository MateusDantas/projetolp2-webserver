package ubet.api;

import java.sql.SQLException;

import ubet.database.TeamsDB;
import ubet.util.Variables;

public class Teams {

	/**
	 * 
	 * @param teamName
	 * @return
	 * @throws SQLException
	 */
	public static TeamsDB updateTeam(String teamName) throws SQLException {
		// TeamsDB(String name, String link, int group) {
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

	public static int getTeamId(String teamName) throws SQLException {

		return updateTeam(teamName).getId();
	}
	
	public static String getTeamName(int teamId) throws SQLException {
		
		TeamsDB team = new TeamsDB();
		
		if (team.setTeam(teamId))
			return team.getName();
		
		return null;
	}
}
