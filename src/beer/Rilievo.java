package beer;

import java.util.Arrays;

public class Rilievo {
	protected final static int phMin=0;
	protected final static int phMax=14;
	protected final static String[] possibiliFasi= {"ammostamento", "filtraggio", "bollitura", "fermentazione", "altro"};
	protected int numRilievo;
	private String faseRilievo;
	private double ph;
	protected double temperatura;
	
	public void stampa(){
		System.out.println("---------------");
		System.out.println("STAMPA RILIEVO");
		System.out.println("Numero rilievo: "+this.numRilievo);
		System.out.println("Fase: "+this.faseRilievo);
		System.out.println("Temperatura: "+this.temperatura);
		System.out.println("Ph: "+this.ph);
	}

	public Rilievo() {
		this.numRilievo=-1;
		this.faseRilievo="";
		this.temperatura=Double.NaN;
		this.ph=Double.NaN;
	}
	
	public Rilievo(int numRilievo, String faseRilievo, double temperatura, double ph) throws beerException{
		if (numRilievo>0) this.numRilievo=numRilievo;
		else throw new beerException("Operazione non riuscita. Numero del rilievo negativo");
		if (Arrays.asList(possibiliFasi).contains(faseRilievo)) this.faseRilievo=faseRilievo;
		else throw new beerException("Operazione non riuscita. Fase non valida");
		if ((Double.valueOf(ph).isNaN())||((ph>=(double ) phMin)&&(ph<=(double) phMax))) this.ph=ph;
		else throw new beerException("Operazione non riuscita. Valore di ph non valido"+ph);
		this.temperatura=temperatura;
	}
	
	public Rilievo(int numRilievo, String faseRilievo, double temperatura) throws beerException{
		if (numRilievo>0) this.numRilievo=numRilievo;
		else throw new beerException("Operazione non riuscita. Numero del rilievo negativo");
		if (Arrays.asList(possibiliFasi).contains(faseRilievo)) this.faseRilievo=faseRilievo;
		else throw new beerException("Operazione non riuscita. Fase non valida");
		this.temperatura=temperatura;
	}

	public static String[] getPossibilifasi() {
		return possibiliFasi;
	}
	
	public int getNumRilievo() {
		return numRilievo;
	}
	public String getFaseRilievo() {
		return faseRilievo;
	}
	public double getTemperatura() {
		return temperatura;
	}

	public double getPh() {
		return ph;
	}

	public void setNum_rilievo(int numRilievo) throws beerException {
		if (numRilievo>0) this.numRilievo = numRilievo;
		else throw new beerException("Operazione non riuscita. Numero rilievo negativo");
	}

	public void setFaseRilievo(String faseRilievo) throws beerException {
		if (Arrays.asList(Rilievo.possibiliFasi).contains(faseRilievo)) this.faseRilievo = faseRilievo;
		else throw new beerException("Operazione non riuscita. Fase non valida");
	}

	public void setTemperatura(double temperatura) {
		this.temperatura = temperatura;
	}

	public void setPh(double ph) throws beerException {
		if ((Double.valueOf(ph).isNaN())||((ph>=phMin)&&(ph<=phMax))) this.ph = ph;
		else throw new beerException("Operazione non riuscita. Valore del ph non valido");
	}
}
