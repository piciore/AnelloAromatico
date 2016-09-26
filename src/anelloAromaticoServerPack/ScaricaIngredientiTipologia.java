package anelloAromaticoServerPack;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beer.Ingrediente;
import beer.Malto;
import beer.beerException;

/**
 * Servlet implementation class ScaricaIngredientiTipologia
 */
@WebServlet("/GestioneIngredienti/ScaricaIngredientiTipologia")
public class ScaricaIngredientiTipologia extends HttpServlet {
	Gson gson;
	ServerResponse<?> sr;
	AnelloAromaticoDb database;
	PrintWriter out;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ScaricaIngredientiTipologia() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		out=response.getWriter();
		gson=new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
		HttpSession sessione=request.getSession();
		if(sessione.isNew()) {
			AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 4, "Operazione non consentita. Non hai effettuato il login oppure la tua sessione è scaduta");
			sessione.invalidate();
			out.close();
		}else{
			//Mi connetto al db, altrimenti restituisco un errore
			try {
				database=new AnelloAromaticoDb();
			} catch (SQLException e) {
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 1, e.getMessage());
			}
			if(request.getParameter("tipologia")==null) AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 4, 
					"Reperimento ingredienti non riuscito. Parametri mancanti");
			String tipologia=request.getParameter("tipologia").toLowerCase();
			ArrayList<Ingrediente> listaIngredienti=new ArrayList<Ingrediente>();
			switch(tipologia){
				case "malto": listaIngredienti=new ArrayList<Ingrediente>(); break;
				case "lievito": listaIngredienti=new ArrayList<Ingrediente>(); break;
				case "luppolo": listaIngredienti=new ArrayList<Ingrediente>(); break;
				case "spezia": listaIngredienti=new ArrayList<Ingrediente>(); break;
				case "altro": listaIngredienti=new ArrayList<Ingrediente>(); break;
				default: AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 4, 
						"Reperimento ingredienti non riuscito. Tipologia non valida");
			}
			try {
				//Metto in un unico ResultSet tutti gli ingredienti
				ResultSet rs=database.getAllIngrediente();
				//Per ogni ingrediente controllo se è della tipologia cercata e lo metto nell'arraylist
				while(rs.next()){
					if(rs.getString("tipologia").equals(tipologia)){
						listaIngredienti.add(database.getIngredienteFromId(rs.getInt(1)));
					}
				}
				sr=new ServerResponse<ArrayList<Ingrediente>>(sessione.getId(), listaIngredienti);
				out.println(gson.toJson(sr));
				out.close();
			} catch (SQLException | beerException | dbException e) {
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 100 , e.getMessage());
			}
		}
		try {
			database.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
