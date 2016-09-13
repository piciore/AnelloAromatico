<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@ page
	import="anelloAromaticoServerPack.AnelloAromaticoMain, anelloAromaticoServerPack.AnelloAromaticoServerUtility, anelloAromaticoServerPack.AnelloAromaticoDb ,java.sql.*"%>
<% 
	String name=request.getParameter("name");
	int num=3;
	/*Connection connessione = AnelloAromaticoServerUtility.getAnelloAromaticoDBConnection();
	Statement stmt=connessione.createStatement();
	ResultSet rs=stmt.executeQuery("SHOW CHARACTER SET");
	while(rs.next()){
		System.out.println(rs.getString(1));
	}*/
	AnelloAromaticoDb db=new AnelloAromaticoDb();
	ResultSet rs=null;
	try {
		rs=db.getAllStile();
	} catch (SQLException e) {
		e.printStackTrace();
	}
	//int i=AnelloAromaticoMain.esegui();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Questa pagina serve per i test</title>
</head>
<body>
	<%	//System.out.println(name + "" + num + i); %>
	<% 	
	System.out.println(rs.toString());
	while(rs.next()){
		System.out.print(rs.getString(1));
		System.out.println(rs.getString(2));
	}
	%>

</body>
</html>