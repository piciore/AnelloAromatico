package fasi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import beer.Gittata;
import beer.GittataRaffreddamento;
import beer.beerException;

public class Raffreddamento {
	protected LocalDateTime orarioInizioRaffreddamento;
	protected LocalDateTime orarioFineRaffreddamento;
	protected double temperaturaFineRaffreddamento;
	protected LocalDateTime orarioInizioWhirlpool;
	protected LocalDateTime orarioFineWhirlpool;
	
	protected ArrayList<GittataRaffreddamento> gittate;
	
	protected boolean OIRset;
	protected boolean OFRset;
	protected boolean TFRset;
	protected boolean OIWset;
	protected boolean OFWset;
	
	protected boolean completato;
	
	private void ordinaGittate(){
		Collections.sort(this.gittate, new Comparator<Gittata>(){
			@Override
			public int compare(Gittata o1, Gittata o2) {
				return Integer.valueOf(o1.getNum_gittata()).compareTo(Integer.valueOf(o2.getNum_gittata()));
			}
		});
	}
	
	public Raffreddamento() {
		this.orarioFineRaffreddamento=null;
		this.orarioInizioRaffreddamento=null;
		this.orarioInizioWhirlpool=null;
		this.orarioFineWhirlpool=null;
		this.temperaturaFineRaffreddamento=Double.NaN;
		this.OFRset=false;
		this.OIRset=false;
		this.OIWset=false;
		this.OFWset=false;
		this.TFRset=false;
		this.gittate=new ArrayList<GittataRaffreddamento>();
	}
	
	public Raffreddamento(LocalDateTime ir, LocalDateTime fr) throws beerException{
		this();
		if((ir==null)||(fr==null)||(ir.isBefore(fr))){
			this.OIRset=true;
			this.OFRset=true;
			this.orarioInizioRaffreddamento=ir;
			this.orarioFineRaffreddamento=fr;
		}else throw new beerException("Operazione non riucita. L'orario di fine deve essere successivo a quello di inizio");
	}
	
	public Raffreddamento(LocalDateTime ir, LocalDateTime fr, LocalDateTime iw, LocalDateTime fw) throws beerException{
		this(ir, fr);
		if((iw==null)||(fw==null)||(iw.isBefore(fw))){
			this.OIWset=true;
			this.OFWset=true;
			this.orarioInizioWhirlpool=iw;
			this.orarioFineWhirlpool=fw;
		}else throw new beerException("Operazione non riuscita. L'orario di fine deve essere successivo a quello di inizio");
	}

	public LocalDateTime getOrarioInizioRaffreddamento() {
		return orarioInizioRaffreddamento;
	}

	public void setOrarioInizioRaffreddamento(LocalDateTime orarioInizioRaffreddamento) throws beerException {
		if((orarioInizioRaffreddamento==null)||(this.orarioFineRaffreddamento==null)||(this.orarioFineRaffreddamento.isAfter(orarioInizioRaffreddamento)))	{
			this.OIRset=true;
			this.orarioInizioRaffreddamento = orarioInizioRaffreddamento;
		}else throw new beerException("Operazione non riuscita. L'orario di fine deve essere successivo a quello di inizio");
	}

	public LocalDateTime getOrarioFineRaffreddamento() {
		return orarioFineRaffreddamento;
	}

	public void setOrarioFineRaffreddamento(LocalDateTime orarioFineRaffreddamento) throws beerException {
		if ((orarioFineRaffreddamento==null)||(this.orarioInizioRaffreddamento==null)||(this.orarioInizioRaffreddamento.isBefore(orarioFineRaffreddamento))) {
			this.OFRset=true;
			this.orarioFineRaffreddamento = orarioFineRaffreddamento;
		}else throw new beerException("Operazione non riuscita. L'orario di fine deve essere successivo a quello di inizio");
	}

	public double getTemperaturaFineRaffreddamento() {
		return temperaturaFineRaffreddamento;
	}

	public void setTemperaturaFineRaffreddamento(double temperaturaFineRaffreddamento) {
		this.TFRset=true;
		this.temperaturaFineRaffreddamento = temperaturaFineRaffreddamento;
	}

	public LocalDateTime getOrarioInizioWhirlpool() {
		return orarioInizioWhirlpool;
	}

	public void setOrarioInizioWhirlpool(LocalDateTime orarioInizioWhirlpool) throws beerException {
		if((orarioInizioWhirlpool==null)||(this.orarioFineWhirlpool==null)||(this.orarioFineWhirlpool.isAfter(orarioInizioWhirlpool)))	{
			this.OIWset=true;
			this.orarioInizioWhirlpool = orarioInizioWhirlpool;
		}else throw new beerException("Operazione non riuscita. L'orario di fine deve essere successivo a quello di inizio");
	}

	public LocalDateTime getOrarioFineWhirlpool() {
		return orarioFineWhirlpool;
	}

	public void setOrarioFineWhirlpool(LocalDateTime orarioFineWhirlpool) throws beerException {
		if((orarioFineWhirlpool==null)||(this.orarioInizioWhirlpool==null)||(this.orarioInizioWhirlpool.isBefore(orarioFineWhirlpool)))	{
			this.OFWset=true;
			this.orarioFineWhirlpool = orarioFineWhirlpool;
		}else throw new beerException("Operazione non riuscita. L'orario di fine deve essere successivo a quello di inizio");
	}

	public ArrayList<GittataRaffreddamento> getGittate() {
		return gittate;
	}

	public void setGittate(ArrayList<GittataRaffreddamento> gittateRaf) {
		this.gittate = gittateRaf;
		this.ordinaGittate();
		try {this.completa();
		} catch (beerException e) {
			e.printStackTrace();
		}
	}
	
	public void addGittata(GittataRaffreddamento g){
		this.gittate.add(g);
		this.ordinaGittate();
	}
	
	public void removeGittata(int index) throws beerException{
		if((index>=0)&&(index<=gittate.size()))	this.gittate.remove(index);
		else throw new beerException("Operazione non riuscita. Indice non valido");
		this.ordinaGittate();
	}

	public void completa() throws beerException{
		if((this.OIRset)&&(this.OFRset)&&(this.OIWset)&&(this.OFWset)&&(this.TFRset)) this.completato=true;
		else {
			this.completato=false;
			throw new beerException("Impossibile completare il raffreddamento. Non sono stati impostati tutti i parametri");
		}
	}
	
	public boolean isCompletato(){
		return this.completato;
	}
}
