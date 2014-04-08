package ubet.database;

import java.sql.PreparedStatement;
import java.util.HashMap;
import java.util.List;

import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public interface DBQueries {

	/**
	 * Method insertQuery.
	 * @param query PreparedStatement
	
	 * @return Variables */
	public Variables insertQuery(PreparedStatement query);
	/**
	 * Method updateQuery.
	 * @param query PreparedStatement
	
	 * @return Variables */
	public Variables updateQuery(PreparedStatement query);
	/**
	 * Method getQuery.
	 * @param query PreparedStatement
	
	 * @return List<HashMap<String,Object>> */
	public List<HashMap<String, Object>> getQuery(PreparedStatement query);
}