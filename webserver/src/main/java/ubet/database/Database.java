package ubet.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ubet.util.Variables;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public class Database implements DBQueries {

	/**
	 * Automatically connects to the Database
	 */
	public Database() {
		
		try {
			if (!ConnectSQL.isConnected())
				ConnectSQL.connect();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param query
	 *            PreparedStatement
	 * 
	 * 
	 * @return Variables * @see
	 *         ubet.database.DBQueries#insertQuery(PreparedStatement) * @see
	 *         ubet.database.DBQueries#insertQuery(PreparedStatement)
	 */
	public Variables insertQuery(PreparedStatement query) {

		try {
			if (!ConnectSQL.isConnected())
				ConnectSQL.connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			return Variables.SQL_ERROR;
		}
		
		PreparedStatement newStatement = query;

		try {

			int affectedRows = newStatement.executeUpdate();
			
			if (affectedRows == 0)
				return Variables.UNKNOWN_ERROR;
			return Variables.SUCCESS;
		} catch (SQLException se) {

			se.printStackTrace();
			return Variables.SQL_ERROR;
		} finally {

			try {
				if (newStatement != null)
					newStatement.close();
			} catch (SQLException se) {

				se.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param query
	 *            PreparedStatement
	 * 
	 * 
	 * @return Variables * @see
	 *         ubet.database.DBQueries#updateQuery(PreparedStatement) * @see
	 *         ubet.database.DBQueries#updateQuery(PreparedStatement)
	 */
	public Variables updateQuery(PreparedStatement query) {

		return insertQuery(query);
	}

	/**
	 * Method changeQuery.
	 * 
	 * @param pattern
	 *            String
	 * @param values
	 *            List<Object>
	 * 
	 * @return Variables
	 */
	public Variables changeQuery(final String pattern, final List<Object> values) {

		PreparedStatement newStatement = null;

		newStatement = buildStatements(pattern, values);

		Variables result = insertQuery(newStatement);

		try {
			if (newStatement != null)
				newStatement.close();
		} catch (SQLException se) {
			se.printStackTrace();
			return Variables.SQL_ERROR;
		}

		return result;
	}

	/**
	 * 
	 * @param query
	 *            PreparedStatement
	 * 
	 * 
	 * @return List<HashMap<String,Object>> * @see
	 *         ubet.database.DBQueries#getQuery(PreparedStatement) * @see
	 *         ubet.database.DBQueries#getQuery(PreparedStatement)
	 */
	public List<HashMap<String, Object>> getQuery(PreparedStatement query) {

		try {
			if (!ConnectSQL.isConnected())
				ConnectSQL.connect();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			return null;
		}
		
		PreparedStatement newStatement = query;
		ResultSet result = null;
		ResultSetMetaData metaData = null;

		try {

			result = newStatement.executeQuery();
			metaData = result.getMetaData();

			int totalColumns = metaData.getColumnCount();

			ArrayList<HashMap<String, Object>> list = new ArrayList<HashMap<String, Object>>();

			while (result.next()) {

				HashMap<String, Object> row = new HashMap<String, Object>(
						totalColumns);

				for (int i = 1; i <= totalColumns; i++)
					row.put(metaData.getColumnName(i), result.getObject(i));

				list.add(row);
			}

			return list;
		} catch (SQLException se) {

			se.printStackTrace();
			return null;
		} finally {

			try {
				if (result != null)
					result.close();
				if (newStatement != null)
					newStatement.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Tries to reconnect
	 */
	public void reconnect() {

		ConnectSQL.reconnect();
	}

	/**
	 * Connect to the server
	 */
	public void connect() {
		
		ConnectSQL.connect();
	}

	/**
	 * Tries to disconnect
	 */
	public void disconnect() {

		ConnectSQL.disconnect();
	}

	/**
	 * 
	 * @param pattern
	 * @param values
	 * 
	 * 
	 * @return PreparedStatement
	 */
	public PreparedStatement buildStatements(String pattern, List<Object> values) {

		PreparedStatement newStatement = null;

		try {

			newStatement = ConnectSQL.getConnection().prepareStatement(pattern);

			if (newStatement == null)
				return null;

			if (values == null)
				return newStatement;
			
			for (int i = 0; i < values.size(); i++)
				newStatement.setObject(i + 1, values.get(i));

			return newStatement;
		} catch (SQLException e) {

			e.printStackTrace();
			return null;
		}

	}

}