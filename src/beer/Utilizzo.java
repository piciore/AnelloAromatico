package beer;

import anelloAromaticoServerPack.dbException;

public class Utilizzo {
	protected int idCotta;
	protected Ingrediente ingrediente;
	private double quantità;
	
	public void stampa(){
		System.out.println("--------------------");
		System.out.println("STAMPA UTILIZZO");
		System.out.println("Nome ingrediente: "+this.ingrediente.nome);
		System.out.println("Tipo ingrediente: "+this.getIngrediente().getTipo());
		System.out.println("Quantità: "+this.getQuantità());
	}

	/**
	 * Costruttore
	 * Istanzia un nuovo Utilizzo con l'ingrediente passato come parametro e la relativa quantità
	 * @param ingrediente
	 * @param quantità
	 * @throws beerException
	 */
	public Utilizzo(Ingrediente ingrediente, double quantità) throws beerException {
		if(quantità>=0){
			this.ingrediente=ingrediente;
			this.quantità=quantità;
		}else{
			throw new beerException("quantità must be >=0");
		}	
	}
	
	public int getIdCotta(){
		return idCotta;
	}

	/**
	 * 
	 * @return l'ingrediente relativo all'utilizzo
	 */
	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	/**
	 * Imposta l'ingrediente dell'utilizzo
	 * @param ingrediente
	 * @throws beerException
	 */
	public void setIngrediente(Ingrediente ingrediente) throws beerException {
		this.ingrediente = ingrediente;
	}

	/**
	 * 
	 * @return la quantità dell'ingrediente utilizzato
	 */
	public double getQuantità() {
		return quantità;
	}

	/**
	 * Imposta la quantità dell'ingrediente utilizzato. Nel caso di malti, luppoli e altri ingredienti solidi è misurata in grammi, nel caso dei lieviti liquidi è misurata in litri
	 * @param quantità misura della quantità da settare
	 * @throws beerException
	 */
	public void setQuantità(double quantità) throws beerException {
		if(quantità>=0){
			this.quantità = quantità;
		}else throw new beerException("quantità must be >=0");
	}
	
	public void setIdCotta(int id) throws dbException{
		if(id>0) this.idCotta=id;
		else throw new dbException("Id negativo");
	}
}
