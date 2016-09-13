package fasi;

import java.time.LocalDate;

import beer.beerException;

public class Maturazione {
	protected LocalDate dataInizioMaturazione;
	protected LocalDate dataFineMaturazione;
	protected double temperaturaMaturazione;
	protected boolean botte;
	
	protected boolean DIMset;
	protected boolean DFMset;
	protected boolean TMset;
	protected boolean Bset;
	
	protected boolean completato;
	
	public Maturazione() {
		this.DFMset=false;
		this.DIMset=false;
		this.TMset=false;
		this.Bset=false;
		this.dataInizioMaturazione=null;
		this.dataFineMaturazione=null;
		this.temperaturaMaturazione=Double.NaN;
		this.botte=false;
		
	}
	
	public Maturazione(LocalDate inizio, LocalDate fine) throws beerException{
		this();
		if ((inizio==null)||(fine==null)||(inizio.isBefore(fine))||(inizio.isEqual(fine))){
			this.DIMset=true;
			this.DFMset=true;
			this.dataInizioMaturazione=inizio;
			this.dataFineMaturazione=fine;
		}else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}
	
	public Maturazione(LocalDate inizio, LocalDate fine, double temp) throws beerException{
		this(inizio, fine);
		this.TMset=true;
		this.temperaturaMaturazione=temp;
	}

	public Maturazione(LocalDate inizio, LocalDate fine, double temp, boolean botte) throws beerException{
		this(inizio, fine, temp);
		this.botte=botte;
		this.Bset=true;
		this.completa();
	}

	public LocalDate getDataInizioMaturazione() {
		return dataInizioMaturazione;
	}

	public void setDataInizioMaturazione(LocalDate dataInizioMaturazione) throws beerException {
		if ((dataInizioMaturazione==null)||(this.dataFineMaturazione==null)||(this.dataFineMaturazione.isAfter(dataInizioMaturazione))) {
			this.DIMset=true;
			this.dataInizioMaturazione = dataInizioMaturazione;
		}
		else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public LocalDate getDataFineMaturazione() {
		return dataFineMaturazione;
	}

	public void setDataFineMaturazione(LocalDate dataFineMaturazione) throws beerException {
		if ((dataFineMaturazione==null)||(this.dataInizioMaturazione==null)||(this.dataInizioMaturazione.isBefore(dataFineMaturazione))) {
			this.DFMset=true;
			this.dataFineMaturazione = dataFineMaturazione;
		}
		else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public double getTemperaturaMaturazione() {
		return temperaturaMaturazione;
	}

	public void setTemperaturaMaturazione(double temperaturaMaturazione) {
		this.temperaturaMaturazione = temperaturaMaturazione;
		this.TMset=true;
	}

	public int isBotte() {
		if(this.Bset){
			if(this.botte) return 1;
			else return 0;
		}else return -1;
	}

	public void setBotte(boolean botte) {
		this.botte = botte;
		this.Bset=true;
	}

	public boolean isCompletato() {
		return completato;
	}
	
	public void completa() throws beerException{
		if((this.DFMset)&&(this.DIMset)&&(this.TMset)&&(this.Bset)) this.completato=true;
		else {
			this.completato=false;
			throw new beerException("impossibile completare la maturazione. Non sono stati impostati tutti i parametri");
		}
	}
	
}
