package fasi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import beer.Rilievo;
import beer.RilievoAmmostamento;
import beer.beerException;

public class Ammostamento {
	protected double litriAcqua;
	protected LocalDate data;
	protected ArrayList<RilievoAmmostamento> rilievi;
	protected boolean completato;
	protected boolean Aset;
	
	private void ordinaRilievi(){
		Collections.sort(this.rilievi, new Comparator<Rilievo>(){
			@Override
			public int compare(Rilievo o1, Rilievo o2) {
				return Integer.valueOf(o1.getNumRilievo()).compareTo(Integer.valueOf(o2.getNumRilievo()));
			}
		});
	}
	
	public Ammostamento(){
		this.litriAcqua=Double.NaN;
		this.data=null;
		this.Aset=false;
		rilievi = new ArrayList<RilievoAmmostamento>();
	}
	
	public Ammostamento(double l) throws beerException {
		this();
		if ((Double.valueOf(l).equals(Double.NaN))||(l>0)) {
			this.Aset=true;
			this.data=null;
			this.litriAcqua=l;
		}
		else throw new beerException("Operazione non riuscita. Impostare un numero di litri positivo");
		this.completato=false;
	}
	
	public Ammostamento(double l, LocalDate d) throws beerException{
		this(l);
		this.setData(d);
	}

	public double getLitriAcqua() {
		return litriAcqua;
	}

	public void setLitriAcqua(double litriAcqua) throws beerException {
		if ((Double.valueOf(litriAcqua).equals(Double.NaN))||(litriAcqua>0)) this.litriAcqua = litriAcqua;
		else throw new beerException("Operazione non riuscita. Impostare un numero di litri positivo");
	}

	public LocalDate getData() {
		return data;
	}

	public void setData(LocalDate data) {
		this.data = data;
	}

	public ArrayList<RilievoAmmostamento> getRilievi() {
		return rilievi;
	}

	public void setRilievi(ArrayList<RilievoAmmostamento> rilievi) {
		this.rilievi = rilievi;
		this.ordinaRilievi();
		try {this.completa();
		} catch (beerException e) {	e.printStackTrace();
		}
	}
	
	public void addRilievo(RilievoAmmostamento r){
		this.rilievi.add(r);
		this.ordinaRilievi();
	}

	public boolean isCompletato() {
		return completato;
	}

	public void completa() throws beerException {
		if(this.Aset){
			this.completato=Boolean.TRUE;
		}else {
			this.completato=false;
			throw new beerException("Impossibile completare l'ammostamento. Litri = "+this.litriAcqua);
		}
	}
	
	public boolean inGiornata(){
		ArrayList<RilievoAmmostamento> r=this.rilievi;
		if(r.size()==0) return true;
		if(r.size()==1){
			if(r.get(0).getOrarioInizio().isAfter(r.get(0).getOrarioFine())) return false;
			else return true;
		}else{
			for(int i=1; i<this.rilievi.size(); i++){
				if(r.get(0).getOrarioInizio().isAfter(r.get(0).getOrarioFine())) return false;
				if(r.get(i).getOrarioInizio().isAfter(r.get(i).getOrarioFine())) return false;
				if(r.get(i).getOrarioInizio().isBefore(r.get(i-1).getOrarioFine())) return false;
			}
		}
		return true;
	}

}
