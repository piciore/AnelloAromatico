package anelloAromaticoServerPack;

import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

public class AnelloAromaticoServerUtility {

	public static Connection getAnelloAromaticoDBConnection() throws NamingException, SQLException{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/AnelloAromaticoDb");
		Connection conn=null;
		conn=ds.getConnection();
		return conn;
	}
}
