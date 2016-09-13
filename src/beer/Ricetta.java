package beer;

import java.sql.Date;
import java.time.Instant;
import java.util.ArrayList;

import anelloAromaticoServerPack.dbException;

public class Ricetta {
	protected int id;
	protected String nome;
	protected String note;
	protected Date data_creazione;
	protected Stile stile;

	protected ArrayList<Utilizzo> listaIngredienti;
	
	public void stampa(){
		System.out.println("--------------------------");
		System.out.println("STAMPA RICETTA");
		System.out.println("Id: "+this.id);
		System.out.println("Nome :"+this.nome);
		System.out.println("Note :"+this.note);
		System.out.println("Data creazione: "+this.data_creazione.toString());
		System.out.println("Stile :"+this.stile.getNome());
		System.out.println("Ingredienti :");
		for(int i=0; i<this.listaIngredienti.size(); i++){
			System.out.println("    - "+this.listaIngredienti.get(i).getIngrediente().getNome()+" | "+this.listaIngredienti.get(i).getQuantità());
		}
	}
	
	public int getId(){
		return id;
	}
	
	public void setId(int id) throws dbException{
		if(id>0) this.id=id;
		else throw new dbException("Id negativo");
	}
	
	public Stile getStile() {
		return stile;
	}

	public void setStile(Stile stile) {
		this.stile = stile;
	}
	
	/**
	 * 
	 * @return il nome della ricetta
	 */
	
	public String getNome() {
		return nome;
	}

	/**
	 * Imposta il nome della ricetta
	 * @param nome il nome che si vuole dare alla ricetta
	 */
	public void setNome(String nome) {
		this.nome = nome;
	}

	/**
	 * 
	 * @return il valore del campo note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * Allega una nota alla ricetta
	 * @param note la nota che si vuole allegare
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * 
	 * @return la data di creazione della ricetta
	 */
	public Date getData_creazione() {
		return data_creazione;
	}

	/**
	 * Imposta la data di creazione della ricetta
	 * @param data_creazione la data di creazione che si vuole impostare
	 * @throws beerException
	 */
	public void setData_creazione(Date data_creazione) throws beerException {
		if(!data_creazione.after(Date.from(Instant.now()))){
			this.data_creazione = data_creazione;
		}else throw new beerException("data_creazione is in the future..invalid");
	}

	/**
	 * Aggiunge un Utilizzo (ingrediente + quantità) alla ricetta
	 * @param u
	 */
	public void addIngrediente(Utilizzo u){
		this.listaIngredienti.add(u);
	}
	
	/**
	 * Crea un utilizzo a partire dall'ingrediente e dalla quantità passate come parametri e lo aggiunge alla ricetta
	 * @param i ingrediente da utilizzare
	 * @param quantità quantità utilizzata
	 * @throws beerException
	 * @throws dbException 
	 */
	public void addIngrediente(Ingrediente i, double quantità) throws beerException, dbException{
		if(i instanceof Lievito){
			this.addIngrediente(new UtilizzoLievito((Lievito) i, (int)quantità));
		}else this.addIngrediente(new Utilizzo(i, quantità));
	}

	/**
	 * 
	 * @return un ArrayList di Utilizzo, contenenti gli ingredienti e le relative quantità utilizzate nella ricetta
	 */
	public ArrayList<Utilizzo> getListaIngredienti(){
		return this.listaIngredienti;
	}

	/**
	 * Cerca l'utilizzo passato come parametro nella lista degli ingredienti e ritorna l'indice della prima occorrenza.
	 * Il primo elemento si trova alla posizione 0
	 * @param u l'utilizzo da cercare
	 * @return
	 * @throws beerException se l'utilizzo passato non è presente nella lista
	 */
	public int getPosizioneUtilizzo(Utilizzo u) throws beerException{
		int x=this.listaIngredienti.indexOf(u);
		if (x!=(-1)){
			return x;
		}else throw new beerException("Utilizzo non presente");
	}
	
	/**
	 * Elimina dalla lista degli ingredienti l'utilizzo corrispondente alla posizione passata come parametro
	 * @param index
	 */
	public void removeIngrediente(int index){
		this.listaIngredienti.remove(index);
	}
	
	/**
	 * Elimina dagli ingredienti della ricetta tutte le occorrenza dell'ingrediente passato come parametro
	 * @param i ingrediente da eliminare
	 */
	public void removeIngrediente(Ingrediente i){
		int index;
		for (index=0; index<this.listaIngredienti.size(); index++){
			if(this.listaIngredienti.get(index).getIngrediente().equalTo(i)){
				this.listaIngredienti.remove(index);
				index--;
			}
		}
	}
	
	
	/**
	 * Costruttore
	 * @param nome
	 * @param note
	 * @param data_creazione
	 * @throws beerException
	 */
	public Ricetta(String nome, String note, Date data_creazione) throws beerException {
		if((data_creazione==null)||(data_creazione.before(Date.from(Instant.now())))){
			this.nome=nome;
			this.note=note;
			this.data_creazione=data_creazione;
			this.listaIngredienti=new ArrayList<Utilizzo>();
		}else throw new beerException("data_creazione is in the future..invalid");
	}
	
	/**
	 * Costruttore
	 * @param nome
	 * @param data_creazione
	 * @throws beerException
	 */
	public Ricetta(String nome, Date data_creazione) throws beerException{
		if(data_creazione.before(Date.from(Instant.now()))){
			this.nome=nome;
			this.data_creazione=data_creazione;
			this.listaIngredienti=new ArrayList<Utilizzo>();
		}else throw new beerException("data_creazione is in the future..invalid");
	}
	
	/**
	 * Costruttore
	 * @param nome
	 * @param data_creazione
	 * @throws beerException
	 */
	public Ricetta(String nome) throws beerException{
		this.nome=nome;
		this.listaIngredienti=new ArrayList<Utilizzo>();
	}

}
