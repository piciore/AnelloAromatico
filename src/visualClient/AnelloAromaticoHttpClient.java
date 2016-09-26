package visualClient;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

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
import com.google.gson.reflect.TypeToken;

import anelloAromaticoServerPack.ServerResponse;
import anelloAromaticoServerPack.Utente;
import beer.Ingrediente;
import beer.Lievito;
import beer.Luppolo;
import beer.Malto;

public class AnelloAromaticoHttpClient {
	protected CloseableHttpClient mainHttpClient;
	protected HttpPost httpPost;
	protected ResponseHandler<String> responseHandler;
	protected String request;
	protected List<NameValuePair> nameValuePairs;
	private static final String serverAddress="localhost";
	private static final int serverPort=8081;
	private static final String serverRoot="AnelloAromatico";
	protected Gson gson;

	private synchronized void svuotaParametri(){
		while(!nameValuePairs.isEmpty()){
			nameValuePairs.remove(0);
		}
	}
	private synchronized void aggiungiParametri() throws UnsupportedEncodingException{
		this.httpPost.setEntity(new UrlEncodedFormEntity(this.nameValuePairs));
	}
	public synchronized boolean logout(){
		try {
			this.mainHttpClient.close();
			this.mainHttpClient= HttpClients.createDefault();
			return Boolean.TRUE;
		} catch (IOException e) {
			e.printStackTrace();
			return Boolean.FALSE;
		}
	}
	
	public AnelloAromaticoHttpClient() {
		mainHttpClient=HttpClients.createDefault();
		this.request="http://"+serverAddress+":"+serverPort+"/"+serverRoot;
		this.nameValuePairs = new ArrayList<NameValuePair>();
		this.responseHandler= new ResponseHandler<String>() {
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
        this.gson=new GsonBuilder().serializeNulls().serializeSpecialFloatingPointValues().create();
	}
	
	public synchronized ServerResponse<Utente> Login(String username, String password){
		httpPost=new HttpPost(this.request.concat("/Login"));
		this.svuotaParametri();
		this.nameValuePairs.add(new BasicNameValuePair("username", username));
		this.nameValuePairs.add(new BasicNameValuePair("password", password));
		ServerResponse<Utente> sr;
		try {
			this.aggiungiParametri();
			String response=this.mainHttpClient.execute(this.httpPost, this.responseHandler);
			Type responseType = new TypeToken<ServerResponse<Utente>>(){}.getType();
			sr=gson.fromJson(response, responseType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public synchronized ServerResponse<ArrayList<Ingrediente>> getIngredientiTipologia(String tipologia) throws UnsupportedEncodingException{
		httpPost=new HttpPost(this.request.concat("/GestioneIngredienti/ScaricaIngredientiTipologia"));
		this.svuotaParametri();
		this.nameValuePairs.add(new BasicNameValuePair("tipologia", tipologia));
		ServerResponse<ArrayList<Ingrediente>> sr;
		try {
			this.aggiungiParametri();
			String response=this.mainHttpClient.execute(this.httpPost, this.responseHandler);
			Type responseType=null;
			switch(tipologia){
			case "malto": responseType=new TypeToken<ServerResponse<ArrayList<Malto>>>(){}.getType(); break;
			case "lievito": responseType=new TypeToken<ServerResponse<ArrayList<Lievito>>>(){}.getType(); break;
			case "luppolo": responseType=new TypeToken<ServerResponse<ArrayList<Luppolo>>>(){}.getType(); break;
			case "spezia": responseType=new TypeToken<ServerResponse<ArrayList<Ingrediente>>>(){}.getType(); break;
			case "altro": responseType=new TypeToken<ServerResponse<ArrayList<Ingrediente>>>(){}.getType(); break;
			default: responseType=new TypeToken<ServerResponse<ArrayList<Ingrediente>>>(){}.getType(); break;
			}
			sr=gson.fromJson(response, responseType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	/**
	 * Inserisce l'ingrediente passato nel database con una richiesta alla servlet inserisciIngrediente.java
	 * @param ing Ingrediente da inserire nel database
	 * @return Una ServerResponse<Ingrediente> contenente la risposta alla richiesta, null in caso di errore nella formulazione della richiesta
	 */
	public synchronized ServerResponse<Ingrediente> insertIngrediente(Ingrediente ing){
		httpPost=new HttpPost(this.request.concat("/GestioneIngredienti/InserisciIngrediente"));
		this.svuotaParametri();
		this.nameValuePairs.add(new BasicNameValuePair("ingrediente", this.gson.toJson(ing)));
		try {
			this.aggiungiParametri();
			String response=this.mainHttpClient.execute(this.httpPost, this.responseHandler);
			Type responseType=null;
			switch(ing.getTipologia().toLowerCase()){
			case "malto": responseType=new TypeToken<ServerResponse<Malto>>(){}.getType(); break;
			case "lievito": responseType=new TypeToken<ServerResponse<Lievito>>(){}.getType(); break;
			case "luppolo": responseType=new TypeToken<ServerResponse<Luppolo>>(){}.getType(); break;
			case "spezia": responseType=new TypeToken<ServerResponse<Ingrediente>>(){}.getType(); break;
			case "altro": responseType=new TypeToken<ServerResponse<Ingrediente>>(){}.getType(); break;
			default: responseType=new TypeToken<ServerResponse<Ingrediente>>(){}.getType(); break;
			}
			ServerResponse<Ingrediente> sr=gson.fromJson(response, responseType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public synchronized ServerResponse<Boolean> updateIngrediente(Ingrediente ing){
		httpPost=new HttpPost(this.request.concat("/GestioneIngredienti/UpdateIngrediente"));
		this.svuotaParametri();
		this.nameValuePairs.add(new BasicNameValuePair("ingrediente", this.gson.toJson(ing)));
		try {
			this.aggiungiParametri();
			String response=this.mainHttpClient.execute(this.httpPost, this.responseHandler);
			Type responseType=null;
			responseType=new TypeToken<ServerResponse<Boolean>>(){}.getType();
			ServerResponse<Boolean> sr=gson.fromJson(response, responseType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	public synchronized ServerResponse<Boolean> eliminaIngrediente(int id){
		httpPost=new HttpPost(this.request.concat("/GestioneIngredienti/EliminaIngrediente"));
		this.svuotaParametri();
		this.nameValuePairs.add(new BasicNameValuePair("id", this.gson.toJson(id)));
		try {
			this.aggiungiParametri();
			String response=this.mainHttpClient.execute(this.httpPost, this.responseHandler);
			Type responseType=null;
			responseType=new TypeToken<ServerResponse<Boolean>>(){}.getType();
			ServerResponse<Boolean> sr=gson.fromJson(response, responseType);
			return sr;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
}
