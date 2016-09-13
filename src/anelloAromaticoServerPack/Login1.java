package anelloAromaticoServerPack;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

/**
 * Servlet implementation class Login1
 */
@WebServlet("/Login1")
public class Login1 extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @throws SQLException 
     * @see HttpServlet#HttpServlet()
     */
    public Login1() throws SQLException {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//request.getSession();
		//Provo a connettermi al database
		AnelloAromaticoDb db=null;
		try {
			db = new AnelloAromaticoDb();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		Utente u=db.login(request.getParameter("username"), request.getParameter("password"));
		//ServerResponse sr=new ServerResponse(u, u.getClass());
		Gson gson=new Gson();
		response.getWriter().println(gson.toJson(u));
	}
}
