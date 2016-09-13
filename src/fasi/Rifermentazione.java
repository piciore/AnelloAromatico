package fasi;

import java.time.LocalDate;

import beer.beerException;

public class Rifermentazione {
	protected LocalDate dataInizioRifermentazione;
	protected LocalDate dataFineRifermentazione;
	protected double temperaturaRifermentazione;
	
	protected boolean DIRset;
	protected boolean DFRset;
	protected boolean TRset;
	
	protected boolean completato;
	
	public Rifermentazione() {
		this.dataInizioRifermentazione=null;
		this.dataFineRifermentazione=null;
		this.temperaturaRifermentazione=Double.NaN;
		this.DIRset=false;
		this.DFRset=false;
		this.TRset=false;
	}
	
	public Rifermentazione(LocalDate inizio, LocalDate fine) throws beerException{
		this();
		if((inizio==null)||(fine==null)||(inizio.isBefore(fine))||(inizio.equals(fine))){
			this.DIRset=true;
			this.DFRset=true;
			this.dataInizioRifermentazione=inizio;
			this.dataFineRifermentazione=fine;
		}else throw new beerException("Operazione non riuscita. La data di inizio deve essere precedente a quella di fine");
	}
	
	public Rifermentazione(LocalDate inizio, LocalDate fine, double temperatura) throws beerException{
		this(inizio, fine);
		this.TRset=true;
		this.temperaturaRifermentazione=temperatura;
		this.completato=true;
	}

	public LocalDate getDataInizioRifermentazione() {
		return dataInizioRifermentazione;
	}

	public void setDataInizioRifermentazione(LocalDate dataInizioRifermentazione) throws beerException {
		if ((dataInizioRifermentazione==null)||(this.dataFineRifermentazione==null)||(this.dataFineRifermentazione.isAfter(dataInizioRifermentazione))) {
			this.DIRset=true;
			this.dataInizioRifermentazione = dataInizioRifermentazione;
		}else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public LocalDate getDataFineRifermentazione() {
		return dataFineRifermentazione;
	}

	public void setDataFineRifermentazione(LocalDate dataFineRifermentazione) throws beerException {
		if ((dataFineRifermentazione==null)||(this.dataInizioRifermentazione==null)||(this.dataInizioRifermentazione.isAfter(dataFineRifermentazione))) {
			this.DFRset=true;
			this.dataFineRifermentazione = dataFineRifermentazione;
		}
		else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public double getTemperaturaRifermentazione() {
		return temperaturaRifermentazione;
	}

	public void setTemperaturaRifermentazione(double temperaturaRifermentazione) {
		this.TRset=true;
		this.temperaturaRifermentazione = temperaturaRifermentazione;
	}
	
	public void completa() throws beerException{
		if((this.DIRset)&&(this.DFRset)&&(this.TRset)) this.completato=true;
		else {
			this.completato=false;
			throw new beerException("Impossibile completare la rifermentazione. Non sono stati impostati tutti i parametri");
		}
	}
	
	public boolean isCompletato(){
		return this.completato;
	}

}
