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
 * Servlet implementation class UpdateIngrediente
 * Riceve un solo parametro "ingrediente", il cui valore deve essere scritto in Json (accettando parametri floating speciali).
 * L'ingrediente stesso contiene l'id da eliminare
 * Restituisce una ServerResponse contenente un boolean settato a true se l'operazione di eliminazione va a buon fine.
 * In caso contrario, restituisci una ServerResponse contenente l'eccezione generata
 */
@WebServlet(description = "Aggiorna un ingrediente già presente nel database, identificabile tramite il suo id", urlPatterns = { "/GestioneIngredienti/UpdateIngrediente" })
public class UpdateIngrediente extends HttpServlet {
	Gson gson;
	ServerResponse<?> sr;
	AnelloAromaticoDb database;
	PrintWriter out;
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateIngrediente() {
        super();
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		out=response.getWriter();
		//Utilizzo un GsonBuilder per poter serializzare/deserializzare anche valori speciali come Double.NaN
		gson=new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
		//Ricavo l'ID di sessione. Nel caso la sessione sia nuova, restituisco un errore (non sono loggato)
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
			//Controllo che ci sia il campo ingrediente
			if(request.getParameter("ingrediente")==null) AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 4, "Aggiornamento non riuscito. Parametri mancanti");
			String ingText=request.getParameter("ingrediente");
			//Leggo l'ingrediente e lo converto nel tipo dato più adatto a rappresentarlo
			Ingrediente ing=null;
			try{
				ing=gson.fromJson(ingText, Ingrediente.class);
			}catch(Exception e){
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 2, "Aggiornamento non riuscito. Errore nel trasferimento dei dati");
			}
			switch(ing.getTipologia().toLowerCase()){
			case "malto": ing=gson.fromJson(ingText, Malto.class); break;
			case "lievito": ing=gson.fromJson(ingText, Lievito.class); break;
			case "luppolo": ing=gson.fromJson(ingText, Luppolo.class); break;
			default: break;
			}
			//Aggiorno l'ingrediente sul database
			try {
				if(database.updateIngrediente(ing)==0) 
					AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 11, "Aggiornamento non riuscito. Controllare l'id");
				else{
					sr=new ServerResponse<Boolean>(sessione.getId(), Boolean.TRUE);
					out.println(gson.toJson(sr));
				}
			} catch (SQLException e) {
				AnelloAromaticoServerUtility.sendError(out, gson, sessione.getId(), 10, e.getMessage());
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
