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
import com.google.gson.GsonBuilder;

import beer.Ingrediente;
import beer.Lievito;
import beer.Luppolo;
import beer.Malto;

/**
 * Servlet implementation class InserisciIngrediente
 * Riceve un solo parametro "ingrediente", il cui valore deve essere scritto in Json (accettando parametri floating speciali)
 * Restituisce una ServerResponse contenente l'ingrediente inserito nel database, aggiungendo l'id all'ingrediente passato.
 * In caso di errore, resituisce una ServerResponse contenente l'eccezione generata
 */
@WebServlet(description = "Inserisce un nuovo ingrediente nel database", urlPatterns = { "/GestioneIngredienti/InserisciIngrediente" })
public class InserisciIngrediente extends HttpServlet {
	Gson gson;
	ServerResponse<?> sr;
	AnelloAromaticoDb database;
	PrintWriter out;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public InserisciIngrediente() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected synchronized void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		out=response.getWriter();
		//Utilizzo un GsonBuilder per poter serializzare/deserializzare anche valori speciali come Double.NaN
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
			//Controllo che sia stato passato l'ingrediente
			if(request.getParameter("ingrediente")==null) AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 2, "Inserimento non riuscito. Parametri mancanti");
			//Casto l'ingrediente alla classe più specifica per rappresentarlo correttamente
			Ingrediente ing=gson.fromJson(request.getParameter("ingrediente"), Ingrediente.class);
			switch(ing.getTipologia().toLowerCase()){
				case "malto": ing=gson.fromJson(request.getParameter("ingrediente"), Malto.class); break;
				case "lievito": ing=gson.fromJson(request.getParameter("ingrediente"), Lievito.class); break;
				case "luppolo": ing=gson.fromJson(request.getParameter("ingrediente"), Luppolo.class); break;
				default: break;
			}
			//Inserisco l'ingrediente nel database. In caso di esito negativo, restituisco un errore
			try {
				int id=database.insertIngrediente(ing);
				if(id>0){
					ing.setId(id);
					sr=new ServerResponse<Ingrediente>(sessione.getId(), ing);
					out.println(gson.toJson(sr));
					out.close();
				}else AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 3, "Inserimento non riuscito");
			} catch (SQLException | dbException e) {
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 100 , e.getMessage());
			}
			out.close();
		}
		try {
			database.chiudi();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
