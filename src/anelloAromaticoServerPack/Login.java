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
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	AnelloAromaticoDb db=null;
	ServerResponse risposta=null;
	private static final long serialVersionUID = 1L;

	public Login(){
		super();
	}
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Entro nel login");
		Utente u=null;
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		Gson gson=new Gson();
		try {
			db=new AnelloAromaticoDb();
			u=db.login(username, password);
		} catch (SQLException e) {
			e.printStackTrace();
			risposta=new ServerResponse(1, e, e.getClass(), e.getMessage());
			response.getWriter().println(gson.toJson(risposta, ServerResponse.class));
		}
		risposta=new ServerResponse(u, u.getClass());
		System.out.println("Sto per rispondere");
		response.getWriter().println(gson.toJson(risposta, ServerResponse.class));
		response.flushBuffer();
		
		/*try{
			System.out.println("Sono stato chiamato");
			String username=request.getParameter("username");
			System.out.println(username);
			String password=request.getParameter("password");
			AnelloAromaticoDb db=new AnelloAromaticoDb();
			Gson g=new Gson();
			Utente u=db.login(username, password);
			response.getWriter().println(g.toJson(u, Utente.class));
			response.getWriter().flush();
			db.chiudi();
		}catch(SQLException e){
			e.printStackTrace();
			response.getWriter().println(e.getErrorCode() + "---" + e.getMessage());
		}*/
		
	}

}
