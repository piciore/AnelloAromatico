package fasi;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import beer.Gittata;
import beer.GittataBollitura;
import beer.Rilievo;
import beer.RilievoBollitura;
import beer.beerException;

public class Bollitura {
	protected LocalDateTime orarioInizioRiscaldamento;
	protected LocalDateTime orarioInizioBollitura;
	protected ArrayList<RilievoBollitura> rilievi;
	protected ArrayList<GittataBollitura> gittate;
	
	protected boolean OIRSet;
	protected boolean OIBSet;
	
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
	
	public Bollitura() {
		this.orarioInizioBollitura=null;
		this.orarioInizioRiscaldamento=null;
		this.OIRSet=false;
		this.OIBSet=false;
		rilievi = new ArrayList<RilievoBollitura>();
		gittate = new ArrayList<GittataBollitura>();
		this.completato=false;
	}
	
	public Bollitura(LocalDateTime ris, LocalDateTime bol) throws beerException{
		if(ris.isBefore(bol)){
			this.orarioInizioRiscaldamento=ris;
			this.orarioInizioBollitura=bol;
			this.OIBSet=true;
			this.OIRSet=true;
			rilievi = new ArrayList<RilievoBollitura>();
			gittate = new ArrayList<GittataBollitura>();
			this.completato=false;
		}else throw new beerException("Operazione non riuscita. Orario di bollitura precedente all'orario di riscaldamento");
	}

	public LocalDateTime getOrarioInizioRiscaldamento() {
		return orarioInizioRiscaldamento;
	}

	public void setOrarioInizioRiscaldamento(LocalDateTime orarioInizioRiscaldamento) throws beerException {
		if((this.orarioInizioBollitura==null)||(orarioInizioRiscaldamento==null)||(this.orarioInizioBollitura.isAfter(orarioInizioRiscaldamento))){
			this.orarioInizioRiscaldamento = orarioInizioRiscaldamento;
			this.OIRSet=true;
		}
		else throw new beerException("Operazione non riuscita. Il riscaldamento non può iniziare dopo la bollitura");
	}

	public LocalDateTime getOrarioInizioBollitura() {
		return orarioInizioBollitura;
	}

	public void setOrarioInizioBollitura(LocalDateTime orarioInizioBollitura) throws beerException {
		if ((this.orarioInizioRiscaldamento==null)||(orarioInizioBollitura==null)||(this.orarioInizioRiscaldamento.isBefore(orarioInizioBollitura))) {
			this.orarioInizioBollitura = orarioInizioBollitura;
			this.OIBSet=true;
		}
		else throw new beerException("Operazione non riuscita. Il riscaldamento non può iniziare dopo la bollitura");
	}

	public ArrayList<RilievoBollitura> getRilievi() {
		return rilievi;
	}

	public void setRilievi(ArrayList<RilievoBollitura> rilievi) {
		this.rilievi = rilievi;
		this.ordinaRilievi();
		if(!this.gittate.isEmpty()){
			try {this.completa();
			} catch (beerException e) {e.printStackTrace();
			}
		}
	}
	
	public void addRilievo(RilievoBollitura r){
		this.rilievi.add(r);
		this.ordinaRilievi();
	}
	
	public void removeRilievo(int index) throws beerException{
		if((index>=0)&&(index<=rilievi.size()))	this.rilievi.remove(index);
		else throw new beerException("Operazione non riuscita. Indice non valido");
		this.ordinaRilievi();
	}

	public ArrayList<GittataBollitura> getGittate() {
		return gittate;
	}

	public void setGittate(ArrayList<GittataBollitura> gittate) {
		this.gittate = gittate;
		this.ordinaGittate();
		if(!this.rilievi.isEmpty()){
			try {	this.completa();
			} catch (beerException e) {	e.printStackTrace();
			}
		}
	}
	
	public void addGittata(GittataBollitura g){
		this.gittate.add(g);
		this.ordinaGittate();
	}
	
	public void removeGittata(int index) throws beerException{
		if((index>=0)&&(index<=gittate.size()))	this.gittate.remove(index);
		else throw new beerException("Operazione non riuscita. Indice non valido");
		this.ordinaGittate();
	}

	public boolean isCompletato() {
		return completato;
	}
	
	public void completa() throws beerException{
		if ((this.OIRSet)&&(this.OIBSet)){
			this.completato=Boolean.TRUE;
		}else {
			this.completato=false;
			throw new beerException("Impossibile completare la bollitura. Non sono stati impostati tutti i parametri");
		}
	}
	
}
