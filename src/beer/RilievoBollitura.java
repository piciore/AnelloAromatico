package beer;

import java.time.LocalTime;

public class RilievoBollitura extends Rilievo {
	protected LocalTime orarioInizio;
	protected LocalTime orarioFine;
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Orario inizio: "+this.orarioInizio);
		System.out.println("Orario fine: "+this.orarioFine);
	}
	
	public RilievoBollitura() {
		super();
		this.orarioInizio=null;
		this.orarioFine=null;
	}

	public RilievoBollitura(int num_rilievo, double temperatura, double ph, LocalTime orarioInizio, LocalTime orarioFine) throws beerException {
		super(num_rilievo, "bollitura", temperatura, ph);
		this.orarioInizio=orarioInizio;
		this.orarioFine=orarioFine;
	}

	public LocalTime getOrarioInizio() {
		return orarioInizio;
	}
	public void setOrarioInizio(LocalTime orarioInizio) {
		this.orarioInizio = orarioInizio;
	}
	public LocalTime getOrarioFine() {
		return orarioFine;
	}
	public void setOrarioFine(LocalTime orarioFine) {
		this.orarioFine = orarioFine;
	}
}
