package moneytransferMain.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionUtil {
	public static Connection getMySQLConnection()
	         throws SQLException {
		String jdbcDriver = "org.h2.Driver";
	     String userName = "sa";
	     String password = "root";
	     return getMySQLConnection(jdbcDriver, userName, password);
	 }
	  
	 public static Connection getMySQLConnection(String jdbcDriver,
			 String userName, String password) throws SQLException {
	    
	     try {
			Class.forName(jdbcDriver);
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	     String connectionURL = "jdbc:h2:~/test/moneytransfer";
	  
	     Connection conn = DriverManager.getConnection(connectionURL, userName,
	             password);
	     return conn;
	 }
}
