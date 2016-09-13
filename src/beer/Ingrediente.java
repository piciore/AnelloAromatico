package beer;

import java.util.Arrays;

import anelloAromaticoServerPack.dbException;

public class Ingrediente {
	private final static String[] possibiliTipologie ={"malto", "luppolo", "lievito", "spezia", "altro"};
	protected int id;
	protected String nome;
	protected String note="";
	protected String tipo="";
	protected String tipologia;
	
	public void stampa(){
		System.out.println("---------------------");
		System.out.println("STAMPA INGREDIENTE");
		System.out.println("Id: "+this.id);
		System.out.println("Nome: "+this.nome);
		System.out.println("Note: "+this.note);
		System.out.println("Tipo: "+this.tipo);
		System.out.println("Tipologia: "+this.tipologia);
	}

	public Ingrediente(String nome){
		this.id=-1;
		this.nome=nome;
		this.tipologia="";
		this.tipo="";
	}

	public Ingrediente(String nome, String tipo, String tipologia, String note) throws beerException, dbException{
		if(Arrays.asList(possibiliTipologie).contains(tipologia)){
			this.nome=nome;
			this.id=-1;
			this.tipo=tipo;
			this.note=note;
			this.tipologia=tipologia;
		}else throw new beerException("tipologia not valid");
	}

	public int getId(){
		return id;
	}
	
	public void setId(int id) throws dbException{
		if(id>0) this.id=id;
		else throw new dbException("Id negativo");
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return the tipologia
	 */
	public String getTipologia() {
		return tipologia;
	}

	/**
	 * @param tipologia the tipologia to set
	 * @throws Exception 
	 */
	public void setTipologia(String tipologia) throws beerException {
		if (!Arrays.asList(possibiliTipologie).contains(tipologia)){
			throw new beerException("tipologia not valid");
		}
		this.tipologia = tipologia;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Lievito toLievito() throws beerException, dbException{
		if (this.getTipologia().equals("lievito")){
			Lievito l= new Lievito(nome);
			l.id=this.id;
			l.setNote(this.note);
			l.setTipo(this.tipo);
			return l;
		}else throw new beerException("This is not a lievito ingredient");
	}

	public Lievito toLievito(String stato_materiale, double quantità_busta) throws beerException, dbException{
		Lievito l= this.toLievito();
		l.setTipologia("lievito");
		l.setStato_materiale(stato_materiale);
		l.setQuantità_busta(quantità_busta);
		return l;
	}

	public Luppolo toLuppolo() throws beerException{
		if (this.tipologia.equals("luppolo")){
			Luppolo l= new Luppolo(nome);
			l.id=this.id;
			l.setNote(this.note);
			l.setTipo(this.tipo);
			l.setTipologia("luppolo");
			return l;
		}else throw new beerException("This is not a luppolo ingredient");
	}

	public Luppolo toLuppolo(double alfa_acidi) throws beerException{
		Luppolo l= this.toLuppolo();
		l.setAlfa_acidi(alfa_acidi);
		return l;
	}

	public Malto toMalto() throws beerException{
		if (this.tipologia.equals("malto")){
			Malto l= new Malto(nome);
			l.id=this.id;
			l.setNote(this.note);
			l.setTipo(this.tipo);
			l.setTipologia("malto");
			return l;
		}else throw new beerException("This is not a malto ingredient");
	}

	public Malto toMalto(int colore_ebc) throws beerException{
		Malto l= this.toMalto();
		l.setColore_ebc(colore_ebc);
		return l;
	}
	
	/**
	 * Confronta l'ingrediente passato con l'oggetto corrente
	 * @param i
	 * @return true se l'ingrediente passato è uguale all'oggetto corrente, false altrimenti
	 */
	public boolean equalTo(Ingrediente i){
		if ((this.nome==i.nome)&&(this.tipo==i.tipo)&&(this.tipologia==i.tipologia)){
			return true;
		}else{
			return false;
		}
	}
}
