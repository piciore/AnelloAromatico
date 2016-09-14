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

/**
 * Servlet implementation class EliminaIngrediente
 * Riceve un solo parametro "id", il quale contiene l'id dell'ingrediente da elimianare
 * Restituisce una ServerResponse contenente un boolean settato a true se l'operazione di eliminazione va a buon fine.
 * In caso contrario, restituisci una ServerResponse contenente l'eccezione generata
 */
@WebServlet(description = "Elimina l'ingrediente corrispondente all'id passato dal database", urlPatterns = { "/GestioneIngredienti/EliminaIngrediente" })
public class EliminaIngrediente extends HttpServlet {
	Gson gson;
	ServerResponse<?> sr;
	AnelloAromaticoDb database;
	PrintWriter out;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EliminaIngrediente() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//Utilizzo un GsonBuilder per poter serializzare/deserializzare anche valori speciali come Double.NaN
		gson=new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
		out=response.getWriter();
		//Ricavo l'ID di sessione. Nel caso la sessione sia nuova, restituisco un errore (non sono loggato)
		HttpSession sessione=request.getSession();
		if(sessione.isNew()) AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 
				4, "Operazione non consentita. Non hai effettuato il login oppure la tua sessione è scaduta");
		//Provo a connettermi al database, altrimenti restituisco un errore
		try {
			database=new AnelloAromaticoDb();
		} catch (SQLException e) {
			AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 
					3, e.getMessage());
		}
		int id;
		//Controllo i parametri...l'id deve essere presente e convertibie in un numero
		if(request.getParameter("id")==null) AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 3, "Eliminazione non riuscita. Parametri mancanti");
		else{
			try{
				id=Integer.valueOf(request.getParameter("id"));
				//Elimino l'ingrediente dal database
				if(database.deleteIngrediente(id)==0) 
					AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 
							10, "Eliminazione non riuscita. Controllare l'id");
				else {
					//Se tutto va liscio genero la risposta
					sr=new ServerResponse<Boolean>(sessione.getId(), Boolean.TRUE);
					out.println(gson.toJson(sr));
				}
			}catch(NumberFormatException | SQLException e) {
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 
						4, "Eliminazione non riuscita. Id non valido");
			}
		}
		out.close();
	}
}
