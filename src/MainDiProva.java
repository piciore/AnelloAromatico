import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.Socket;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAccessor;
import java.time.temporal.TemporalAmount;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import anelloAromaticoServerPack.AnelloAromaticoDb;
import anelloAromaticoServerPack.ServerResponse;
import anelloAromaticoServerPack.Utente;
import anelloAromaticoServerPack.dbException;
import beer.Analisi;
import beer.AnalisiFermentazione;
import beer.Categoria;
import beer.Cotta;
import beer.GittataBollitura;
import beer.GittataFermentazione;
import beer.GittataRaffreddamento;
import beer.Giudizio;
import beer.Ingrediente;
import beer.Lievito;
import beer.Luppolo;
import beer.Malto;
import beer.Ricetta;
import beer.Rilievo;
import beer.RilievoAmmostamento;
import beer.RilievoBollitura;
import beer.RilievoFermentazione;
import beer.RilievoFiltraggio;
import beer.Stile;
import beer.UtilizzoLievito;
import beer.beerException;
import fasi.Fermentazione;
import fasi.Maturazione;
import fasi.Rifermentazione;
import visualClient.AnelloAromaticoHttpClient;
import visualClient.AnelloAromaticoVisualClient;

@SuppressWarnings({ "unused" })
public class MainDiProva {

	public static void main(String[] args) throws Exception {
		
		AnelloAromaticoDb db=new AnelloAromaticoDb();
		Gson gson=new GsonBuilder().serializeSpecialFloatingPointValues().serializeNulls().create();
		/*CloseableHttpClient httpclient = HttpClients.createDefault();
		HttpPost httppost = new HttpPost("http://localhost:8081/AnelloAromatico/Login");
		// Add your data
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
		nameValuePairs.add(new BasicNameValuePair("username", "piciore"));
		nameValuePairs.add(new BasicNameValuePair("password", "lasolita"));
		httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
		
		ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
            @Override
            public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
                int status = response.getStatusLine().getStatusCode();
                if (status >= 200 && status < 300) {
                    HttpEntity entity = response.getEntity();
                    return entity != null ? EntityUtils.toString(entity) : null;
                } else {
                    throw new ClientProtocolException("Unexpected response status: " + status);
                }
            }
        };

		// Execute HTTP Post Request
        String response=httpclient.execute(httppost, responseHandler);
        System.out.println(response);
        Type utenteType = new TypeToken<ServerResponse<Utente>>(){}.getType();
        ServerResponse<Utente> sr=gson.fromJson(response, utenteType);
        if(sr.isExc()){
			System.out.println("Eccezione!!!!");
			System.out.println(sr.getErrorNumber()+": "+sr.getExcMessage());
		}else{
			System.out.println("--------- LOGIN EFFETTUATO ------------------");
			System.out.println("Sei loggato nel sistema con l'utente seguente");
			sr.getCastObj().stampa(new PrintWriter(System.out));
		}
        httpclient.close();*/
		AnelloAromaticoVisualClient finestra=new AnelloAromaticoVisualClient();
        AnelloAromaticoHttpClient client=new AnelloAromaticoHttpClient();
		ServerResponse<Utente> sr=finestra.getHttpClient().Login("piciore", "lasolita");
        //Type utenteType = new TypeToken<ServerResponse<Utente>>(){}.getType();
        if(sr.isExc()){
			System.out.println("Eccezione!!!!");
			System.out.println(sr.getErrorNumber()+": "+sr.getExcMessage());
		}else{
			System.out.println("--------- LOGIN EFFETTUATO ------------------");
			System.out.println("Sei loggato nel sistema con l'utente seguente");
			sr.getCastObj().stampa(new PrintWriter(System.out));
		}
        
        
        ServerResponse<ArrayList<Ingrediente>> sr2=client.getIngredientiTipologia("lievito");
        if(sr2.isExc()){
        	System.out.println("Eccezione!!!!!");
        	System.out.println("---------"+sr2.getErrorNumber()+" "+sr2.getExcMessage());
        }else{
        	for(int i=0; i<sr2.getCastObj().size(); i++){
        		sr2.getCastObj().get(i).stampa();
        	}
        }
        

		Ingrediente ing=db.getIngredienteFromId(5);
		ing.setNome(ing.getNome().concat("___"));
		System.out.println("Modifica"+db.updateIngrediente(ing));
		
        /*ServerResponse<Utente> sr=gson.fromJson(response, responseType);
		System.out.println("Json letto: "+ response);
		if(sr.isExc()){
			System.out.println("Si Ë verificato un errore!!!");
			System.out.println("Sessione "+sr.getSessionId());
			System.out.println(sr.getErrorNumber()+": "+sr.getExcMessage());
		}else{
			//sr=gson.fromJson(response, responseType);
			Utente u=sr.getCastObj();
			System.out.println("la classe di u Ë "+sr.getObj().getClass());
			u.stampa(new PrintWriter(System.out));
		}
		httpclient.close();*/		
		/*URL obj = new URL("http://localhost:8081/AnelloAromatico/Login");
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", "Mozilla/5.0");

		con.setDoOutput(true);
		con.addRequestProperty("password", "lasolita");
		OutputStream os = con.getOutputStream();
		os.write("username=piciore".getBytes());
		//os.write("password=lasolita".getBytes());
		os.flush();
		os.close();
		
		int responseCode = con.getResponseCode();
		System.out.println("POST Response Code :: " + responseCode);
		
		BufferedReader reader=new BufferedReader(new InputStreamReader(con.getInputStream()));
		String line;
		while((line=reader.readLine())!=null){
			System.out.println(line);
			/*Gson gson=new Gson();
			ServerResponse sr=gson.fromJson(line, ServerResponse.class);
			Utente u=(Utente) sr.getObjClass().cast(sr.getMessage());
			u.stampa(new PrintWriter(System.out));
		}*/
		
		/*if (responseCode == HttpURLConnection.HTTP_OK) { //success
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();
			while((inputLine=in.readLine())!= null) {
				response.append(inputLine);
			}
			in.close();

			// print result
			System.out.println(response.toString());*/
		
		/*BufferedReader errorReader=new BufferedReader(new InputStreamReader(connessione.getErrorStream()));
		String errorLine;
		while((errorLine=errorReader.readLine())!=null){
			System.out.println(errorLine);
		}*/
		
		/*while((line=reader.readLine())!=null){
			Gson gson=new Gson();
			Utente u=gson.fromJson(line, Utente.class);
			u.stampa(new PrintWriter(System.out));
		}*/
		
		
		/*AnelloAromaticoDb db=new AnelloAromaticoDb();
		//db.deleteCotta(45);
		Cotta c=db.getCottaFromId(3);
		Fermentazione fer=c.getFermentazione();
		fer.completa();
		c.setFermentazione(fer);
		c.setRifermentazione(db.getCottaFromId(47).getRifermentazione());
		db.updateRifermentazioneCotta(c.getId(), c.getRifermentazione().getDataInizioRifermentazione(), c.getRifermentazione().getDataFineRifermentazione(), c.getRifermentazione().getTemperaturaRifermentazione());
		db.updateDatiCotta(c.getId(), c.getRicetta(), c.getData(), c.getLuogo(), c.getQuantit‡Acqua(), c.getLuogoAcqua(), c.getSaliMinerali(), c.getNote(), c.getFaseCorrente());
		//db.updateMaturazioneCotta(c.getId(), c.getMaturazione().getDataInizioMaturazione(), c.getMaturazione().getDataFineMaturazione(), c.getMaturazione().getTemperaturaMaturazione(), c.getMaturazione().isBotte());
		//db.updateRifermentazioneCotta(c.getId(), c.getRifermentazione().getDataInizioRifermentazione().plusDays(1), c.getRifermentazione().getDataFineRifermentazione().plusDays(1), c.getRifermentazione().getTemperaturaRifermentazione());
		//db.updateFermentazioneCotta(c.getId(), c.getFermentazione().getTemperaturaMinFermentazione(), c.getFermentazione().getTemperaturaMaxFermentazione(), c.getFermentazione().getDataInizioFermentazione().plusDays(1), c.getFermentazione().getDataFineFermentazione().plusDays(1), c.getFermentazione().isTravaso());
		//db.updateRaffreddamentoCotta(c.getId(), c.getRaffreddamento().getOrarioInizioRaffreddamento().plusDays(1), c.getRaffreddamento().getOrarioFineRaffreddamento().plusDays(1), c.getRaffreddamento().getTemperaturaFineRaffreddamento()+2, c.getRaffreddamento().getOrarioInizioWhirlpool().plusDays(1), c.getRaffreddamento().getOrarioFineWhirlpool().plusDays(1));
		//System.out.println(db.updateBollituraCotta(c.getId(), c.getBollitura().getOrarioInizioRiscaldamento().plusHours(1), c.getBollitura().getOrarioInizioBollitura().plusHours(1)));
		System.out.println(c.getId());*/
		
		/*db.deleteIngrediente(10);
		
		Cotta c=db.getCottaFromId(2);
		c.stampa(new PrintWriter(System.out));
		ArrayList<RilievoFermentazione> r=new ArrayList<RilievoFermentazione>();
		System.out.println(c.getFaseCorrente());
		System.out.println(c.getMaturazione().getDataInizioMaturazione());
		r=c.getFermentazione().getRilievi();
		for(int i=0; i<r.size(); i++){
			System.out.println("----------");
			System.out.println(r.get(i).getNumRilievo());
			System.out.println(r.get(i).getTemperatura());
			System.out.println(r.get(i).getPh());
			System.out.println(r.get(i).getFaseRilievo());
			System.out.println(r.get(i).getDataInizio());
			System.out.println(r.get(i).getdataFine());
		}
		
		ArrayList<GittataFermentazione> g=new ArrayList<GittataFermentazione>();
		g=c.getFermentazione().getGittate();
		for(int j=0; j<g.size(); j++){
			System.out.println("----------");
			System.out.println(g.get(j).getNum_gittata());
			System.out.println(g.get(j).getIngrediente().getNome());
			System.out.println(g.get(j).getQuantit‡());
			System.out.println(g.get(j).getNumBuste());
			System.out.println(g.get(j).getData());
		}*/
		//UtilizzoLievito ur=(UtilizzoLievito) db.getUtilizzoRicettaFromId(2, 4);
		//System.out.println(db.updateGittata(7, 3, "fermentazione", 5, null, null, 10));
		
		/*Giudizio g=db.getGiudizioFromId(11);
		g.setData(Date.valueOf(LocalDate.parse(g.getData().toString()).plusDays(2)));
		g.setAcido(true);
		g.setColpoDiLuce(true);
		g.setNome_giudice("Ermenegildo");
		System.out.println(db.updateGiudizio(11, 3, g));*/
		
		//System.out.println(db.deleteGiudizio(10));
		/*Giudizio z=db.getGiudizioFromId(3);
		System.out.println(z.isDiacetile());
		System.out.println(z.isAstringenza());
		String s=z.getData().toString();
		z.setData(Date.valueOf(LocalDate.parse(s).plusDays(2)));
		z.setVotoGenerale(z.getVotoGenerale()+1);
		z.setVotoVisivo(z.getVotoVisivo()-1);
		Giudizio z2=new Giudizio("V", 3, "O", 3, "G", 16, "sb", 4, "ge", 8);
		System.out.println(z2.getEsameVisivo());
		db.insertGiudizio(z2, 2);
		db.insertGiudizio(z, 3);*/
		
		/*GittataBollitura g1=new GittataBollitura(7, ing, 250, LocalTime.of(13, 40));
		System.out.println(db.insertGittataBollitura(g1.getNum_gittata(), 3, g1.getIngrediente().getId(), null, g1.getQuantit‡()));
		GittataFermentazione g2=new GittataFermentazione(7, ing, 270, LocalDate.of(2016, 8, 30));
		System.out.println(db.insertGittataFermentazione(g2.getNum_gittata(), 3, ing.getId(),  LocalDate.now(), g2.getQuantit‡()));
		GittataRaffreddamento g3=new GittataRaffreddamento(5, db.getIngredienteFromId(4), 3, LocalTime.of(15, 30));
		System.out.println(db.insertGittataRaffreddamento(11, 3, ing.getId(), g3.getOrario(), g3.getQuantit‡()));
		System.out.println(db.deleteGittateCotta(3, "bollitura"));
		System.out.println(db.deleteGittateCotta(3, "fermentazione"));
		System.out.println(db.deleteGittateCotta(3, "raffreddamento"));*/
				
		
		//System.out.println(db.updateRicetta(13, "eccolo", "", null, -1));
		//System.out.println(db.deleteAnalisi(3, "fermentazione"));
		//System.out.println(db.updateCategoria(23, "mdificata", "nota"));
		//System.out.println(db.updateAnalisi("filtraggio", 2, Double.NaN, 5, 6, 5, 7, 8, ""));
		/*Analisi a1=db.getAnalisiFromId("fermentazione", 2);
		Analisi a2=db.getAnalisiFromId("filtraggio", 2);
		a1.setNote("Inserita dopo");
		a1.setIdCotta(3);
		a2.setIdCotta(3);
		a2.setNote("Inserita dopo");
		System.out.println("Inserita analisi con id "+db.insertAnalisi(a1));
		System.out.println("Inserita analisi con id "+db.insertAnalisi(a2));*/
		//db.updateRicetta(14, "tra3", "nessuna nota", null, db.getRicettaFromId(14).getStile().getId());
		//db.deleteRicetta(14);
		/*Stile s=db.getStileFromId(2);
		s.setNome(s.getNome().concat("_2"));
		int i=db.insertStile(s);
		db.deleteStile(i);*/
		//System.out.println("Inserita una categoria con id="+db.insertCategoria("Questa Ë una prova", "ciao a tutti"));
		//int i=db.insertCategoria("altra prova", null);
		//System.out.println(db.insertCategoria("prova", null));
		//System.out.println(db.updateCategoria(23, "prova1", "Adesso c'Ë una nota"));
		//Stile s=db.getStileFromId(3);
		//Categoria c= db.getCategoriaFromId(s.getCategoria().getId());
		//c.setId(51);
			
		
		//int i=db.insertRicetta("tra1", "nessuna nota", Date.valueOf(LocalDate.of(2016, 8, 29)), 2);
		//System.out.println("Inserita ricetta con id"+ i);
		//db.getRicettaFromId(i).stampa();
		//db.insertUtilizzoRicettaIngrediente(13, 6, 300);
		//db.insertUtilizzoRicettaLievito(13, "LI01", 1.5, 3.4, 2);
		
		/*Ingrediente ing=db.getIngredienteFromId(6);
		Ingrediente l=db.getIngredienteFromId(4);
		Ricetta r=new Ricetta("tra2");
		r.setNote("nessuna nota");
		r.setData_creazione(Date.valueOf(LocalDate.of(2016, 9, 4)));
		r.setStile(db.getStileFromId(3));
		r.addIngrediente(ing, 300);
		r.addIngrediente(l, 3);
		db.insertRicetta(r);*/
		
		
	
		//SERVLET TEST
		/*URL url=new URL("http://localhost:8081/AnelloAromatico/ServletProva");
		BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
		String line;
		while((line=reader.readLine())!=null){
			System.out.println(line);
			Gson g=new Gson();
			Cotta c=g.fromJson(line, Cotta.class);
			c.stampa(new PrintWriter(System.out));
		}
		reader.close();*/
		
		
		/*try {
			AnelloAromaticoDb db=new AnelloAromaticoDb();
			db.stampaResultSet(db.getAllCategoria());
			
		} catch (SQLException e) {
			e.printStackTrace();
		}*/		
		
		/*AnalisiFermentazione af = new AnalisiFermentazione();
		af.setGrado_alcolico(4.5);
		Analisi a = af; 
		AnalisiOnDb aod= (AnalisiOnDb) a;
		System.out.println(aod.getGrado_alcolico());*/
		
		/*Cotta c = new Cotta("CottaDiProva");
		System.out.println(c.getNomeRicetta());
		System.out.println(c.getData());
		System.out.println(c.getClass());
		
		System.out.println("-----");
		
		c= new Cotta("Cotta2", LocalDate.of(2016, 8, 10));
		System.out.println(c.getNomeRicetta());
		System.out.println(c.getData());
		System.out.println(c.getClass());*/
		
		/*Ingrediente ing = new Ingrediente("lievito1", "AB04", "lievito", "").toLievito();
		((Lievito) ing).setQuantit‡_busta(3);
		Gittata g= new Gittata(1, ing, 2);
		System.out.println(g.getNum_gittata());
		System.out.println(g.getIngrediente().getNome());
		System.out.println(g.getIngrediente().getTipo());
		System.out.println(g.getQuantit‡());
		System.out.println(g.getNumBuste());*/
		
		/*Rilievo r=new Rilievo();
		System.out.println(r.getNum_rilievo());
		System.out.println(r.getFaseRilievo());
		System.out.println(r.getTemperatura());
		System.out.println("------------");
		Rilievo r1=null;
		try{
			r1=new RilievoFermentazione(1, 13, 14, LocalDate.of(2016, 5, 3), LocalDate.of(2016, 7, 14));
		}catch (Exception e){
			e.printStackTrace();
		}
		System.out.println();
		System.out.println(r1.getNum_rilievo());
		System.out.println(r1.getFaseRilievo());
		System.out.println(r1.getTemperatura());*/
		
		/*try{
			Analisi a1= new Analisi("filtraggio", 2, 3.5, 4.2, 5.6, "Nessuna nota");
			System.out.println(a1.getFase());
			System.out.println(a1.getLitri());
			System.out.println(a1.getDensit‡());
			System.out.println(a1.getGradi_plato());
			System.out.println(a1.getGradi_brix());
		}catch (beerException e){
			e.printStackTrace();
		}
		System.out.println("------");
		AnalisiFermentazione a2=null;;
		try{
			a2=new AnalisiFermentazione("Raffreddamento", 2, 3.5, 0.1, 5.6, "Nessuna nota", 10, 11);
		}catch (beerException e){
			e.printStackTrace();
		}	
		System.out.println(a2.getFase());
		System.out.println(a2.getLitri());
		System.out.println(a2.getDensit‡());
		System.out.println(a2.getGradi_plato());
		System.out.println(a2.getGradi_brix());
		System.out.println(a2.getGrado_alcolico());
		System.out.println(a2.getGrado_amaro());*/
		
		
		
		/*Giudizio g=new Giudizio("", 3, "", 8, "", 10, "", 3, "", 5);
		g.setDifetti(false, false, false, false, false, false, false, false, false, false, true, false, false, false, false, false, false, false, null);
		System.out.println(g.giudizioTotale());*/
		
		
		/*Ricetta r=new Ricetta("RicettaDiProva", Date.valueOf(LocalDate.of(2010, 10, 20)));
		Utilizzo u=new Utilizzo(new Ingrediente("malto1", "M01", "malto", ""), 6);
		r.addIngrediente(u);
		Ingrediente ing=new Ingrediente("luppolo1", "L01", "malto", "");
		r.addIngrediente(new Lievito("lievito1",  "liquido", 3), 4);
		r.addIngrediente(new Ingrediente("malto1", "M02", "malto", ""), 5);
		r.addIngrediente(new Ingrediente("malto1", "M02", "malto", ""), 6);
		r.addIngrediente(new Ingrediente("luppolo1", "L01", "luppolo", ""), 4);
		ArrayList<Utilizzo> lista=new ArrayList<Utilizzo>();
		lista = r.getListaIngredienti();
		for(int i=0; i<lista.size(); i++){
			System.out.println(lista.get(i).getIngrediente().getNome());
			System.out.println(lista.get(i).getQuantit‡());
			//System.out.println(lista.get(i).getClass());
		}
		r.removeIngrediente(ing);
		System.out.println(r.getPosizioneUtilizzo(u));
		lista=r.getListaIngredienti();
		System.out.println("--------");
		for(int i=0; i<lista.size(); i++){
			System.out.println(lista.get(i).getIngrediente().getNome());
			System.out.println(lista.get(i).getQuantit‡());
		}*/
		
		
		
		/*try {
			Ingrediente ing=new Ingrediente("lievito1", "AB04", "lievito", "");
			UtilizzoLievito ul=new UtilizzoLievito(ing.toLievito("solido", 3), 5);
		} catch (beerException e) {
			e.printStackTrace();
		}*/
		/*try {
			ing.setTipologia("lievito");
			ing.setNome("Lievito 01");
			l = ing.toLievito("solido", 5);
			System.out.println("Nome: "+l.getNome());
			System.out.println("Note: "+l.getNote());
			System.out.println("Tipologia: "+l.getTipologia());
			System.out.println("Tipo: "+l.getTipo());
			System.out.println("Quantit‡_busta: "+l.getQuantit‡_busta());
			System.out.println("Stato_materiale: "+l.getStato_materiale());
			l = ing.toLievito();
			l.setStato_materiale("solido");
			l.setQuantit‡_busta(5);
			System.out.println("Nome: "+l.getNome());
			System.out.println("Note: "+l.getNote());
			System.out.println("Tipologia: "+l.getTipologia());
			System.out.println("Tipo: "+l.getTipo());
			System.out.println("Quantit‡_busta: "+l.getQuantit‡_busta());
			System.out.println("Stato_materiale: "+l.getStato_materiale());
		} catch (beerException e) {
			e.printStackTrace();
		}*/
	}

}
