package fasi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import beer.Rilievo;
import beer.RilievoFiltraggio;
import beer.beerException;

public class Filtraggio {
	protected double litriAcqua;
	protected LocalDate data;
	protected ArrayList<RilievoFiltraggio> rilievi;
	
	
	protected boolean Aset;
	protected boolean completato;
	
	private void ordinaRilievi(){
		Collections.sort(this.rilievi, new Comparator<Rilievo>(){
			@Override
			public int compare(Rilievo o1, Rilievo o2) {
				return Integer.valueOf(o1.getNumRilievo()).compareTo(Integer.valueOf(o2.getNumRilievo()));
			}
		});
	}
	
	public Filtraggio() {
		this.litriAcqua=Double.NaN;
		this.data=null;
		this.Aset=false;
		this.rilievi = new ArrayList<RilievoFiltraggio>();
	}
	
	public Filtraggio(double l) throws beerException{
		this();
		if ((Double.valueOf(l).equals(Double.NaN))||(l>0)) {
			this.Aset=true;
			this.data=null;
			this.litriAcqua=l;
		}
		else throw new beerException("Operazione non riuscita. Impostare un numero di litri positivo");
		this.completato=false;
	}
	
	public Filtraggio(double l, LocalDate d) throws beerException{
		this(l);
		this.setData(d);
	}
	
	public double getLitriAcqua() {
		return litriAcqua;
	}

	public void setLitriAcqua(double litriAcqua) throws beerException {
		if ((Double.valueOf(litriAcqua).equals(Double.NaN))||(litriAcqua>0)) {
			this.Aset=true;
			this.litriAcqua = litriAcqua;
		}
		else throw new beerException("Operazione non riuscita. Impostare un numero di litri positivo");
	}
	
	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public ArrayList<RilievoFiltraggio> getRilievi() {
		return rilievi;
	}

	public void setRilievi(ArrayList<RilievoFiltraggio> rilievi) {
		this.rilievi = rilievi;
		this.ordinaRilievi();
		try { this.completa();
		} catch (beerException e) {
			e.printStackTrace();
		}
	}
	
	public void addRilievo(RilievoFiltraggio r){
		this.rilievi.add(r);
		this.ordinaRilievi();
	}

	public boolean isCompletato() {
		return completato;
	}

	public void completa() throws beerException {
		if(this.Aset) this.completato=Boolean.TRUE;
		else {
			this.completato=false;
			throw new beerException("Impossibile completare il filtraggio. Litri = "+this.litriAcqua);
		}
	}

}
