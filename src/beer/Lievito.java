/**
	Sottoclasse di Ingrediente.
	Aggiunge lo stato materiale e la quantit� di base della busta.
	I possibili valori di stato_materiale sono "liquido" e "solido"
 */

package beer;

import java.util.Arrays;

public class Lievito extends Ingrediente {
	private final static String[] possibiliStati={"liquido", "solido", ""};
	protected String stato_materiale;
	protected double quantit�_busta;
	
	
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Stato materiale: "+this.stato_materiale);
		System.out.println("Quantit� busta: "+this.quantit�_busta);
	}

	public String getStato_materiale() {
		return stato_materiale;
	}

	public void setStato_materiale(String stato_materiale) throws beerException {
		if (!Arrays.asList(possibiliStati).contains(stato_materiale)){
			throw new beerException("stato_materiale not valid");
		}else	this.stato_materiale = stato_materiale;
	}

	public double getQuantit�_busta() {
		return quantit�_busta;
	}

	public void setQuantit�_busta(double quantit�_busta) throws beerException {
		if((Double.valueOf(quantit�_busta).isNaN())||(quantit�_busta>0)) this.quantit�_busta = quantit�_busta;
		else throw new beerException("Quantit�_busta must be >0");
	}

	public Lievito(String nome) {
		super(nome);
		this.tipologia="lievito";
		this.stato_materiale="";
		//this.quantit�_busta=0;
	}
	
	public Lievito(String nome, String stato_materiale, double quantit�_busta) {
		super(nome);
		this.tipologia="lievito";
		this.stato_materiale=stato_materiale;
		this.quantit�_busta=quantit�_busta;
	}

}
