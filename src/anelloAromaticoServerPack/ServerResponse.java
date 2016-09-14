package anelloAromaticoServerPack;
import anelloAromaticoServerPack.Utente;

@SuppressWarnings("unused")
public class ServerResponse<Tipo> {
	protected boolean exc;
	protected String sessionId;
	protected int errorNumber;
	protected String excMessage;
	protected Tipo obj;
	
	private ServerResponse(){
		this.sessionId="";
		this.errorNumber=-1;
		this.excMessage="";
		this.obj=null;
	}
	
	/**
	 * Genera una ServerResponse da utilizzarsi in caso di errore o anomalia nell'esecuzione della richiesta.
	 * Il client interpreterà tale risposta come la mancata riuscita dell'operazione richiesta.
	 * Ad esempio, parametri errati, errori del server ecc
	 * @param sessionId id della sessione utente
	 * @param errorNumber numero identificativo del problema verificato
	 * @param excMessage messaggio dell'eccezione generata
	 */
	public ServerResponse(String sessionId, int errorNumber, String excMessage){
		this();
		this.sessionId=sessionId;
		this.errorNumber=errorNumber;
		this.excMessage=excMessage;
		this.exc=Boolean.TRUE;
	}
	
	/**
	 * Genera una ServerResponse da utilizzarsi per le risposte "naturali" di una servlet.
	 * Il client la interpreterà come una risposta affermetiva, la quale potrà contenere un oggetto richiesto
	 * @param sessionId id della sessione utente
	 * @param obj contenuto della risposta (utilizzato solo se il server deve restituire qualcosa)
	 * @throws ClassNotFoundException 
	 */
	public ServerResponse(String sessionId, Tipo obj){
		this();
		this.sessionId=sessionId;
		this.obj=obj;
		this.exc=Boolean.FALSE;
	}
	/**
	 * Restituisci TRUE se la ServerResponse contiene un'eccezione o un errore, false in caso contrario
	 * @return
	 */
	public boolean isExc() {
		return exc;
	}
	/**
	 * 
	 * @return una String contenente l'id di sessione della risposta
	 */
	public String getSessionId() {
		return sessionId;
	}
	/**
	 * 
	 * @return L'identificativo dell'errore verificato che ha causato l'eccezione
	 */
	public int getErrorNumber() {
		return errorNumber;
	}
	/**
	 * 	
	 * @return Una String contenente il testo dell'eccezione generata
	 */
	public String getExcMessage() {
		return excMessage;
	}
	/**
	 * Restituisce l'oggetto della risposta senza effettuare alcun cast
	 * @return
	 */
	public Object getObj() {
		return obj;
	}
	/**
	 * Restituisce l'oggetto della risposta sotto forma di oggetto del tipo con cui è stato passato dal server
	 * @return
	 */
	public Tipo getCastObj(){
		return this.obj;
	}
}
