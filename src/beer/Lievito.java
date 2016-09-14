/**
	Sottoclasse di Ingrediente.
	Aggiunge lo stato materiale e la quantità di base della busta.
	I possibili valori di stato_materiale sono "liquido" e "solido"
 */

package beer;

import java.util.Arrays;

public class Lievito extends Ingrediente {
	private final static String[] possibiliStati={"liquido", "solido", ""};
	protected String stato_materiale;
	protected double quantità_busta;
	
	
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Stato materiale: "+this.stato_materiale);
		System.out.println("Quantità busta: "+this.quantità_busta);
	}

	public String getStato_materiale() {
		return stato_materiale;
	}

	public void setStato_materiale(String stato_materiale) throws beerException {
		if (!Arrays.asList(possibiliStati).contains(stato_materiale)){
			throw new beerException("stato_materiale not valid");
		}else	this.stato_materiale = stato_materiale;
	}

	public double getQuantità_busta() {
		return quantità_busta;
	}

	public void setQuantità_busta(double quantità_busta) throws beerException {
		if((Double.valueOf(quantità_busta).isNaN())||(quantità_busta>0)) this.quantità_busta = quantità_busta;
		else throw new beerException("Quantità_busta must be >0");
	}

	public Lievito(String nome) {
		super(nome);
		this.tipologia="lievito";
		this.stato_materiale="";
		//this.quantità_busta=0;
	}
	
	public Lievito(String nome, String stato_materiale, double quantità_busta) {
		super(nome);
		this.tipologia="lievito";
		this.stato_materiale=stato_materiale;
		this.quantità_busta=quantità_busta;
	}

}
