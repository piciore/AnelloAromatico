package anelloAromaticoServerPack;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class ServletProva
 */
@WebServlet("/ServletProva")
public class ServletProva extends HttpServlet {
	private static final long serialVersionUID = 1L;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletProva() {
        super();
    }
    
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
    	response.setContentType("text");
    	response.getWriter().println("Ho ricevuto questa richiesta: "+request.toString());
    	response.getWriter().println("con id di sessione: "+request.getSession().getId());
    	System.out.println(response.getStatus());
		/*
		try {
			AnelloAromaticoDb db=new AnelloAromaticoDb();
			Gson g=new Gson();
			response.getWriter().println(g.toJson(new Ammostamento(2), Ammostamento.class));
			db.getCottaFromId(2).stampa(response.getWriter());
			response.getWriter().flush();
			//db.chiudi();
		} catch (beerException | SQLException | dbException e) {
			e.printStackTrace();
			System.out.println("C'è un' eccezione!!!!!!!!!!");
		}*/
	
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		this.doGet(request, response);
	}

}
