package moneytransferMain.connection;

import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionFactory {
	public static Connection getConnection() throws SQLException {

      return ConnectionUtil.getMySQLConnection();
  }
   
  public static void closeQuietly(Connection conn) {
      try {
          conn.close();
      } catch (Exception e) {
      }
  }

  public static void rollbackQuietly(Connection conn) {
      try {
          conn.rollback();
      } catch (Exception e) {
      }
  }
}
