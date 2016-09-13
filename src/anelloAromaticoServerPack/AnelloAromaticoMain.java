package anelloAromaticoServerPack;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class AnelloAromaticoMain extends Object{

	public AnelloAromaticoMain() {
		super();
	}

	public static int esegui() {
		try{	
		/*Client client = new Client();
		URL u = new URL("http://www.google.it");
		HttpURLConnection uc = (HttpURLConnection) u.openConnection();
		CookieJar cj = client.getCookies(uc);*/
					
		/*Hashtable<String, String> env = new Hashtable<String, String>(); 
		env.put("java.naming.factory.initial", "org.apache.naming.java.javaURLContextFactory"); 
		env.put("java.naming.factory.url.pkgs","org.apache.naming"); 
		env.put("java.naming.factory.url.pkgs.prefixes","org.apache.naming" ); 
		env.put("java.naming.provider.url","org.apache.naming "); 
		Context initCtx = new InitialContext(env);*/
			
		Context initCtx = new InitialContext();
		Context envCtx = (Context)initCtx.lookup("java:comp/env");
		DataSource ds = (DataSource) envCtx.lookup("jdbc/AnelloAromaticoDB");
		Connection conn = ds.getConnection();
		Statement stmt=conn.createStatement();
		ResultSet rs=stmt.executeQuery("SHOW CHARACTER SET");
		while(rs.next()){
			System.out.println(rs.getString(1));
		}
		conn.close();
		System.out.println("Prova");
		}
		catch (Exception e){
			e.printStackTrace();
		}
		return 1;
	}
	
	public static void main(String args[]){
		esegui();
	}
}

