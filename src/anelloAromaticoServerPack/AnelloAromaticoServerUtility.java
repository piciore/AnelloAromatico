package anelloAromaticoServerPack;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.google.gson.Gson;

public class AnelloAromaticoServerUtility {

	public static Connection getAnelloAromaticoDBConnection() throws NamingException, SQLException{
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/AnelloAromaticoDb");
		Connection conn=null;
		conn=ds.getConnection();
		return conn;
	}
	
	public static void sendError(PrintWriter out, Gson gson, String sessionId, int errorNumber, String excMessage){
		ServerResponse<Exception> sr=new ServerResponse<Exception>(sessionId, errorNumber, excMessage);
		out.println(gson.toJson(sr));
		out.close();
	}
}
