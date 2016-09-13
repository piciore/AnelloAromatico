package beer;

import anelloAromaticoServerPack.dbException;

public class UtilizzoLievito extends Utilizzo {
	protected double numeroBuste;
	protected int giorni_starter;
	protected double litri_starter;
	
	
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Giorni starter: "+this.giorni_starter);
		System.out.println("Litri starter: "+this.litri_starter);
	}

	/**
	 * 
	 * @return il lievito relativo all'utilizzo corrente
	 */
	public Lievito getLievito() {
		return (Lievito) ingrediente;
	}

	/**
	 * Imposta il lievito dell'utilizzo corrente
	 * @param lievito
	 */
	public void setLievito(Lievito lievito) {
		this.ingrediente = lievito;
	}

	/**
	 * Imposta il lievito dell'utilizzo corrente. Se l'ingrediente passato come parametro non è un lievito. 
	 * @param lievito
	 * @throws beerException se l'ingrediente passato non è un lievito
	 */
	public void setLievito(Ingrediente lievito) throws beerException{
		if (lievito instanceof Lievito){
			this.ingrediente=(Lievito) lievito;
		}else throw new beerException("invalid lievito type");
	}
	
	/**
	 * 
	 * @return il numero di buste di lievito utilizzate
	 */
	public double getNumeroBuste() {
		return numeroBuste;
	}

	/**
	 * Imposta il numero di buste utilizzate. 
	 * @param numeroBuste
	 * @throws beerException se il numero di buste è negativo
	 */
	public void setNumeroBuste(double numeroBuste) throws beerException {
		if (numeroBuste>=0){
			this.numeroBuste = numeroBuste;
		}else throw new beerException("numeroBuste must be >=0");
	}

	/**
	 * 
	 * @return un intero contenente i giorni dello starter
	 */
	public int getGiorni_starter() {
		return giorni_starter;
	}

	/**
	 * Imposta il numero di giorni dello starter
	 * @param giorni_starter
	 * @throws beerException se il numero di giorni è negativo
	 */
	public void setGiorni_starter(int giorni_starter) throws beerException {
		if(giorni_starter>=0){
			this.giorni_starter = giorni_starter;
		}else throw new beerException("giorni_starter must be >=0");
	}

	/**
	 * 
	 * @return un double contenente il numero di litri dello starter
	 */
	public double getLitri_starter() {
		return litri_starter;
	}

	/**
	 * Imposta il numero di litri dello starter
	 * @param litri_starter
	 * @throws beerException se il numero dei litri è negativo
	 */
	public void setLitri_starter(double litri_starter) throws beerException {
		if(litri_starter>=0){
			this.litri_starter = litri_starter;
		}else throw new beerException("litri_starter must be >=0");
	}

	/**
	 * Costruttore
	 * Istanzia un nuovo UtilizzoLievito con il lievito ingrediente e un numero di buste passate come parametri 
	 * @param ingrediente
	 * @param numBuste
	 * @throws beerException 
	 * @throws dbException 
	 */
	public UtilizzoLievito(Lievito ingrediente, double numBuste) throws beerException, dbException {
		super(ingrediente, numBuste*ingrediente.toLievito().quantità_busta);
		this.giorni_starter=-1;
		this.litri_starter=Double.NaN;
		this.numeroBuste=numBuste;
	}
	
	/**
	 * Costruttore
	 * Istanzia un nuovo UtilizzoLievito con il lievito ingrediente e un numero di buste passate come parametri
	 * @param ingrediente
	 * @param numBuste
	 * @throws beerException se l'ingrediente passato non è di tipo Lievito
	 * @throws dbException 
	 */
	public UtilizzoLievito(Ingrediente ingrediente, double numBuste) throws beerException, dbException {
		super(ingrediente.toLievito(), numBuste*ingrediente.toLievito().quantità_busta);
		this.giorni_starter=-1;
		this.litri_starter=Double.NaN;
		this.numeroBuste=numBuste;
		if (!(ingrediente instanceof Lievito)){
			throw new beerException("Ingrediente must be a lievito type");
		}
	}

	@Override
	public void setIngrediente(Ingrediente ingrediente) throws beerException {
		if(ingrediente instanceof Lievito){
			this.ingrediente=(Lievito) ingrediente;
		}else throw new beerException("ingrediente is not lievito...nedd a Lievito type");
	}	

	/**
	 * Ritorna la quantità di lievito utilizzata
	 */
	@Override
	public double getQuantità() {
		return ((Lievito)this.ingrediente).quantità_busta*this.numeroBuste ;
	}

	/**
	 * Ritorna un Ingrediente contenente il lievito utilizzato
	 */
	@Override
	public Ingrediente getIngrediente() {
		return (Lievito) super.getIngrediente();
	}
}
