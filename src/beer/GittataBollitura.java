package beer;

import java.time.LocalTime;

public class GittataBollitura extends Gittata {
	protected LocalTime orario;
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println(this.orario);
	}

	public GittataBollitura() {
		super();
		this.orario=null;
	}

	public GittataBollitura(int num, Ingrediente ing, double quantità, LocalTime orario) throws beerException {
		super(num, ing, quantità);
		this.orario=orario;
	}

	public LocalTime getOrario() {
		return orario;
	}

	public void setOrario(LocalTime orario) {
		this.orario = orario;
	}

}
