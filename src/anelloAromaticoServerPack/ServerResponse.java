package anelloAromaticoServerPack;

public class ServerResponse {
	protected boolean exc;
	protected int errorNum;
	protected Object obj;
	protected String info;
	protected Class<?> objClass;

	private ServerResponse() {
		this.exc=Boolean.FALSE;
		this.errorNum=-1;
		this.obj=null;
		this.objClass=null;
		this.info="";
	}
	/**
	 * Genera una nuova risposta da utilizzare in caso di esito postivo dell'operazione
	 * @param o oggetto da restituire al client
	 */
	public ServerResponse(Object o, Class<?> classe){
		this();
		this.obj= o;
		this.objClass=classe;
		
	}
	/**
	 * Genera una nuova risposta da utilizzare in caso di eccezione
	 * @param errorNum numero identificativo del problema
	 * @param message Messaggio da passare al client (da visualizzare all'utente)
	 * @param info Messaggio per ulteriori informazioni
	 */
	public ServerResponse(int errorNum, Object message, Class<?> classe, String info){
		this();
		this.exc=Boolean.TRUE;
		this.errorNum=errorNum;
		this.obj=message;
		this.objClass=classe;
		this.info=info;
	}

	public Object getMessage(){
		return this.objClass.cast(this.obj);
	}
	
	public Class<?> getObjClass(){
		return this.objClass;
	}
}
