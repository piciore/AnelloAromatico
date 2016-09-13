package beer;

import anelloAromaticoServerPack.dbException;

public class Utilizzo {
	protected int idCotta;
	protected Ingrediente ingrediente;
	private double quantit�;
	
	public void stampa(){
		System.out.println("--------------------");
		System.out.println("STAMPA UTILIZZO");
		System.out.println("Nome ingrediente: "+this.ingrediente.nome);
		System.out.println("Tipo ingrediente: "+this.getIngrediente().getTipo());
		System.out.println("Quantit�: "+this.getQuantit�());
	}

	/**
	 * Costruttore
	 * Istanzia un nuovo Utilizzo con l'ingrediente passato come parametro e la relativa quantit�
	 * @param ingrediente
	 * @param quantit�
	 * @throws beerException
	 */
	public Utilizzo(Ingrediente ingrediente, double quantit�) throws beerException {
		if(quantit�>=0){
			this.ingrediente=ingrediente;
			this.quantit�=quantit�;
		}else{
			throw new beerException("quantit� must be >=0");
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
	 * @return la quantit� dell'ingrediente utilizzato
	 */
	public double getQuantit�() {
		return quantit�;
	}

	/**
	 * Imposta la quantit� dell'ingrediente utilizzato. Nel caso di malti, luppoli e altri ingredienti solidi � misurata in grammi, nel caso dei lieviti liquidi � misurata in litri
	 * @param quantit� misura della quantit� da settare
	 * @throws beerException
	 */
	public void setQuantit�(double quantit�) throws beerException {
		if(quantit�>=0){
			this.quantit� = quantit�;
		}else throw new beerException("quantit� must be >=0");
	}
	
	public void setIdCotta(int id) throws dbException{
		if(id>0) this.idCotta=id;
		else throw new dbException("Id negativo");
	}
}
