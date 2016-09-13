package fasi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import beer.Gittata;
import beer.GittataFermentazione;
import beer.Rilievo;
import beer.RilievoFermentazione;
import beer.beerException;

public class Fermentazione {
	protected double temperaturaMinFermentazione;
	protected double temperaturaMaxFermentazione;
	protected LocalDate dataInizioFermentazione;
	protected LocalDate dataFineFermentazione;
	protected boolean travaso;
	
	protected boolean TMinFset;
	protected boolean TMaxFset;
	protected boolean DIFset;
	protected boolean DFFset;
	protected boolean travasoSet;
	
	protected ArrayList<GittataFermentazione> gittate;
	protected ArrayList<RilievoFermentazione> rilievi;
	
	protected boolean completato;
	
	private void ordinaRilievi(){
		Collections.sort(this.rilievi, new Comparator<Rilievo>(){
			@Override
			public int compare(Rilievo o1, Rilievo o2) {
				return Integer.valueOf(o1.getNumRilievo()).compareTo(Integer.valueOf(o2.getNumRilievo()));
			}
		});
	}
	
	private void ordinaGittate(){
		Collections.sort(this.gittate, new Comparator<Gittata>(){
			@Override
			public int compare(Gittata o1, Gittata o2) {
				return Integer.valueOf(o1.getNum_gittata()).compareTo(Integer.valueOf(o2.getNum_gittata()));
			}
		});
	}
	
	public Fermentazione() {
		this.temperaturaMinFermentazione=Double.NaN;
		this.temperaturaMaxFermentazione=Double.NaN;
		this.dataInizioFermentazione=null;
		this.dataFineFermentazione=null;
		
		this.travasoSet=false;
		this.TMinFset=false;
		this.TMaxFset=false;
		this.DIFset=false;
		this.DFFset=false;
		rilievi= new ArrayList<RilievoFermentazione>();
		gittate= new ArrayList<GittataFermentazione>();
	}

	public double getTemperaturaMinFermentazione() {
		return temperaturaMinFermentazione;
	}

	public void setTemperaturaMinFermentazione(double temperaturaMinFermentazione) throws beerException {
		if((temperaturaMinFermentazione==Double.NaN)||(Double.valueOf(this.temperaturaMaxFermentazione).equals(Double.NaN))||(this.temperaturaMaxFermentazione>=temperaturaMinFermentazione)) {
			this.TMinFset=true;
			this.temperaturaMinFermentazione = temperaturaMinFermentazione;
		}else throw new beerException("Operazione non riuscita. La temperatura massima deve essere superiore o uguale a quella minima");
	}

	public double getTemperaturaMaxFermentazione() {
		return temperaturaMaxFermentazione;
	}

	public void setTemperaturaMaxFermentazione(double temperaturaMaxFermentazione) throws beerException {
		if ((temperaturaMaxFermentazione==Double.NaN)||(Double.valueOf(this.temperaturaMinFermentazione).equals(Double.NaN))||(this.temperaturaMinFermentazione<=temperaturaMaxFermentazione)) {
			this.TMaxFset=true;
			this.temperaturaMaxFermentazione = temperaturaMaxFermentazione;
		}else throw new beerException("Operazione non riuscita. La temperatura massima deve essere superiore o uguale a quella minima");
	}

	public LocalDate getDataInizioFermentazione() {
		return dataInizioFermentazione;
	}

	public void setDataInizioFermentazione(LocalDate dataInizioFermentazione) throws beerException {
		if ((dataInizioFermentazione==null)||(this.dataFineFermentazione==null)||(this.dataFineFermentazione.isAfter(dataInizioFermentazione))) {
			this.DIFset=true;
			this.dataInizioFermentazione = dataInizioFermentazione;
		}else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public LocalDate getDataFineFermentazione() {
		return dataFineFermentazione;
	}

	public void setDataFineFermentazione(LocalDate dataFineFermentazione) throws beerException {
		if ((dataFineFermentazione==null)||(this.dataInizioFermentazione==null)||(this.dataInizioFermentazione.isBefore(dataFineFermentazione))) {
			this.DFFset=true;
			this.dataFineFermentazione = dataFineFermentazione;
		}else throw new beerException("Operazione non riuscita. La data d'inizio deve essere precedente a quella di fine");
	}

	public int isTravaso() {
		if(this.travasoSet){
			if(this.travaso)return 1;
			else return 0;
		}else return -1;	
	}

	public void setTravaso(boolean travaso) {
		this.travasoSet=true;
		this.travaso = travaso;
	}

	public ArrayList<GittataFermentazione> getGittate() {
		return gittate;
	}

	public void setGittate(ArrayList<GittataFermentazione> gittate) {
		this.gittate = gittate;
		this.ordinaGittate();
		if(!this.rilievi.isEmpty()){
			try { this.completa();
			} catch (beerException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void addGittata(GittataFermentazione g){
		this.gittate.add(g);
		this.ordinaGittate();
	}
	
	public void removeGittata(int index) throws beerException{
		if((index>=0)&&(index<=gittate.size()))	this.gittate.remove(index);
		else throw new beerException("Operazione non riuscita. Indice non valido");
		this.ordinaGittate();
	}

	public ArrayList<RilievoFermentazione> getRilievi() {
		return rilievi;
	}

	public void setRilievi(ArrayList<RilievoFermentazione> rilievi) {
		this.rilievi = rilievi;
		this.ordinaRilievi();
		if(!this.gittate.isEmpty()){
			try { this.completa();
			} catch (beerException e) { e.printStackTrace();
			}
		}
	}
	
	public void addRilievo(RilievoFermentazione r){
		this.rilievi.add(r);
		this.ordinaRilievi();
	}
	
	public void removeRilievo(int index) throws beerException{
		if((index>=0)&&(index<=rilievi.size()))	this.rilievi.remove(index);
		else throw new beerException("Operazione non riuscita. Indice non valido");
		this.ordinaRilievi();
	}
	
	public void completa() throws beerException{
		if((this.DIFset)&&(this.DFFset)&&(this.TMinFset)&&(this.TMaxFset)&&(this.travasoSet)) this.completato=true;
		else {
			this.completato=false;
			throw new beerException("Impossibile completare la fermentazione. Non sono stati impostati tutti i parametri");
		}
	}
	
	public boolean isCompletato(){
		return this.completato;
	}
	
}
