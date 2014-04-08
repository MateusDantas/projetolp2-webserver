package ubet.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author mdantas
 * @version $Revision: 1.0 $
 */
public abstract class ConnectSQL {

	private static String DB_URL = "jdbc:mysql://192.241.241.32:3306/ubet";
	private static String USERNAME = "mdantas";
	private static String PASSWORD = "";
	protected static Connection dbConnection = null;
	

	public ConnectSQL(){
		
	}
	/**
	 * 
	 * 
	 * 
	 * 
	
	 * @return true if connection to server is alive, false otherwise * @throws
	 *         SQLException * @throws SQLException * @throws SQLException * @throws SQLException * @throws SQLException
	 */
	public static boolean isConnected() throws SQLException {

		if (dbConnection == null)
			return false;

		if (dbConnection.isClosed())
			return false;

		return testConnection("SELECT 1");
	}

	/**
	 * Test if the connection to the database is alive
	 * @param query
	 *            SQL Query Strig
	 * 
	
	 * @return true if it's possible to execute a query, false otherwise */
	private static boolean testConnection(String query) {

		Statement newStatement = null;
		ResultSet result = null;
		try {

			newStatement = dbConnection.createStatement();

			if (newStatement == null)
				return false;

			result = newStatement.executeQuery(query);

			if (result == null)
				return false;

			if (result.next())
				return true;

			return false;

		} catch (Exception e) {

			e.printStackTrace();
			return false;
		} finally {

			try {
				newStatement.close();
				result.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * Tries to reconnect
	 */
	public static void reconnect() {

		disconnect();
		connect();
	}

	/**
	 * Connect to the server
	 */
	public static void connect() {
		try {

			Class.forName("com.mysql.jdbc.Driver");
			dbConnection = DriverManager.getConnection(DB_URL, USERNAME,
					PASSWORD);
		} catch (SQLException se) {

			se.printStackTrace();
			System.out.println(se);
		} catch (Exception e) {

			e.printStackTrace();
			System.out.println(e);
		}
	}
	
	/**
	 * Tries to disconnect
	 */
	public static void disconnect() {

		try {

			if (dbConnection != null)
				dbConnection.close();

		} catch (SQLException se) {

			se.printStackTrace();
		}
	}
	
	/**
	 * 
	 * 
	
	 * @return Connection link of the current connection */
	public static Connection getConnection() {

		return dbConnection;
	}
}
