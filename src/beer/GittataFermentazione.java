package beer;

import java.time.LocalDate;

public class GittataFermentazione extends Gittata {
	protected LocalDate data;
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Data: "+this.data);
	}
	
	public GittataFermentazione() {
		super();
		this.data=null;
	}

	public GittataFermentazione(int num, Ingrediente ing, double quantità, LocalDate data) throws beerException {
		super(num, ing, quantità);
		this.data=data;
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}
}
