package anelloAromaticoServerPack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;

/**
 * Servlet implementation class Login
 */
@WebServlet("/Login")
public class Login extends HttpServlet {
	Gson gson;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
        gson=new Gson();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		ServerResponse<?> sr=null;
		AnelloAromaticoDb database=null;
		HttpSession sessione=request.getSession();
		//Mi connetto al db, altrimenti restituisco un errore
		try {
			database=new AnelloAromaticoDb();
		} catch (SQLException e) {
			AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 1, e.getMessage());
		}
		//Controllo che ci siano i campi username e password
		String username=request.getParameter("username");
		String password=request.getParameter("password");
		if((username==null)||(password==null)){
			AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 2, "Login non riuscito. Parametri mancanti");
		}
		//Effettuo il login
		try {
			Utente u=database.login(username, password);
			sessione.setAttribute("utente", u);
			sr=new ServerResponse<Utente>(sessione.getId(), u);
			out.println(gson.toJson(sr));
			out.close();
			database.chiudi();
		} catch (dbException | SQLException e) {
			AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 2, e.getMessage());
		}
		out.close();
	}
}
