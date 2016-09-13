package beer;

import java.time.LocalDate;

public class RilievoFermentazione extends Rilievo {
	protected LocalDate dataInizio;
	protected LocalDate dataFine;
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Data inizio: "+this.dataInizio.toString());
		System.out.println("Data fine: "+this.dataFine.toString());
	}
	
	public RilievoFermentazione() {
		super();
		this.dataInizio=null;
		this.dataFine=null;
	}

	public RilievoFermentazione(int num_rilievo, double temperatura, double ph, LocalDate dataInizio, LocalDate dataFine) throws beerException {
		super(num_rilievo, "fermentazione", temperatura, ph);
		if((dataInizio==null)||(dataFine==null)||(dataInizio.isBefore(dataFine))||(dataInizio.isEqual(dataFine))){
			this.dataInizio=dataInizio;
			this.dataFine=dataFine;
		}else throw new beerException("Operazine non riuscita. La data iniziale è più recente di quella finale");
	}
	
	public LocalDate getDataInizio() {
		return dataInizio;
	}
	public LocalDate getdataFine() {
		return dataFine;
	}
	public void setDataFine(LocalDate dataFine) throws beerException {
		if ((dataFine==null)||(dataInizio==null)||(dataFine.isAfter(this.dataInizio))||(dataFine.isEqual(this.dataInizio)))this.dataFine = dataFine;
		else throw new beerException("Operazione non riuscita. La data iniziale è più recente di quella finale");
	}
	public void setDataInizio(LocalDate dataInizio) throws beerException {
		if ((dataInizio==null)||(dataFine==null)||(dataInizio.isAfter(LocalDate.now()))||(dataInizio.isAfter(this.dataFine))) throw new beerException("Operazione non riuscita. La data inserita è nel futuro");
		else this.dataInizio = dataInizio;
	}
	public void setData(LocalDate dataInizio, LocalDate dataFine) throws beerException{
		if((dataInizio==null)||(dataFine==null)||(dataInizio.isBefore(dataFine))||(dataInizio.isEqual(dataFine))){
			this.dataInizio=dataInizio; this.dataFine=dataFine;
		}else throw new beerException("Operazione non riuscita. La data iniziale è più recente di quella finale");
	}
}
