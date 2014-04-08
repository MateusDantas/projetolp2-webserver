package ubet.database;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class TeamsDB extends Database {

	private static final String INSERT_TEAM = "INSERT INTO teams (`name`, `link`, `group`) VALUES(?, ?, ?)";
	private static final String UPDATE_TEAM = "UPDATE teams SET name=?, link=?, group=? WHERE id=?";
	private static final String GET_BY_NAME = "SELECT * FROM teams WHERE name=?";
	private static final String GET_BY_ID = "SELECT * FROM teams WHERE id=?";

	private String name, link;
	private int id, group;

	public TeamsDB() {
		
	}
	
	/**
	 * Constructor for TeamsDB.
	 * @param name String
	 */
	public TeamsDB(String name) {
		setName(name);
	}

	/**
	 * Constructor for TeamsDB.
	 * @param id int
	 * @param name String
	 */
	public TeamsDB(int id, String name) {
		setId(id);
		setName(name);
	}

	/**
	 * Constructor for TeamsDB.
	 * @param id int
	 * @param name String
	 * @param link String
	 * @param group int
	 */
	public TeamsDB(int id, String name, String link, int group) {
		setId(id);
		setName(name);
		setLink(link);
		setGroup(group);
	}
	
	/**
	 * 
	 * @param name
	 * @param link
	 * @param group
	 */
	public TeamsDB(String name, String link, int group) {
		setName(name);
		setLink(link);
		setGroup(group);
	}
	/**
	 * Method addTeam.
	
	
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables addTeam() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidName(this.name))
			return Variables.INVALID_NAME;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.name);
		newList.add(this.link);
		newList.add(this.group);
		return changeQuery(INSERT_TEAM, newList);
	}

	/**
	 * Method updateTeam.
	
	
	
	 * @return Variables * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public Variables updateTeam() throws SQLException {

		if (!ConnectSQL.isConnected())
			connect();

		if (!ConnectSQL.isConnected())
			return Variables.CONNECTION_ERROR;

		if (!isValidName(this.name))
			return Variables.INVALID_NAME;

		List<Object> newList = new ArrayList<Object>();
		newList.add(this.name);
		newList.add(this.link);
		newList.add(this.group);
		newList.add(this.id);
		
		return changeQuery(UPDATE_TEAM, newList);
	}

	/**
	 * Method setTeam.
	 * @param teamName String
	
	 * @return boolean */
	public boolean setTeam(String teamName) {
		
		if (!isValidName(teamName))
			return false;
		
		List<Object> newList = new ArrayList<Object>();
		newList.add(teamName);
		
		List<TeamsDB> team = getTeam(GET_BY_NAME, newList);
		
		if (team == null || team.size() == 0)
			return false;
		
		setId(team.get(0).id);
		setName(team.get(0).name);
		setLink(team.get(0).link);
		setGroup(team.get(0).group);
		
		return true;
	}
	
	/**
	 * Method setTeam.
	
	 * @return boolean */
	public boolean setTeam() {
		
		return setTeam(this.id);
	}
	
	/**
	 * Method setTeam.
	 * @param teamId int
	
	 * @return boolean */
	public boolean setTeam(int teamId) {
		
		List<Object> newList = new ArrayList<Object>();
		newList.add(teamId);
		
		List<TeamsDB> team = getTeam(GET_BY_ID, newList);
		
		if (team == null || team.size() == 0)
			return false;
		
		setId(team.get(0).id);
		setName(team.get(0).name);
		setLink(team.get(0).link);
		setGroup(team.get(0).group);
		
		return true;
	}
	/**
	 * Method getTeam.
	 * @param pattern String
	 * @param values List<Object>
	
	 * @return List<TeamsDB> */
	public List<TeamsDB> getTeam(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;
		newStatement = buildStatements(pattern, values);

		List<HashMap<String, Object>> result = getQuery(newStatement);

		if (result.size() == 0)
			return null;

		List<TeamsDB> newList = new ArrayList<TeamsDB>();

		for (int i = 0; i < result.size(); i++) {
			String getTeamName = (String) result.get(i).get("name");
			int getId = (Integer) result.get(i).get("id");
			String getTeamLink = (String) result.get(i).get("link");
			int getGroup = (Integer) result.get(i).get("group");
			newList.add(new TeamsDB(getId, getTeamName, getTeamLink, getGroup));
		}
		try {
			if (newStatement != null)
				newStatement.close();
		} catch (SQLException se) {
			se.printStackTrace();
			return null;
		}

		return newList;
	}
	/**
	 * Method isValidName.
	 * @param name2 String
	
	 * @return boolean */
	private boolean isValidName(String name2) {

		if (name2 == null) System.out.println("hmmmm");
		if (name2.length() > Variables.MAX_LENGTH.getValue())
			return false;
		return true;
	}

	/**
	 * Method getName.
	
	 * @return String */
	public String getName() {
		return name;
	}

	/**
	 * Method setName.
	 * @param name String
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * Method getId.
	
	 * @return int */
	public int getId() {
		return id;
	}

	/**
	 * Method setId.
	 * @param id int
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * Method getLink.
	
	 * @return String */
	public String getLink() {
		return link;
	}

	/**
	 * Method setLink.
	 * @param link String
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Method getGroup.
	
	 * @return int */
	public int getGroup() {
		return group;
	}

	/**
	 * Method setGroup.
	 * @param group int
	 */
	public void setGroup(int group) {
		this.group = group;
	}
}
