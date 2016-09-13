package beer;

import java.util.Arrays;

import anelloAromaticoServerPack.dbException;

public class Analisi {
	protected final static String[] possibiliFasi ={"filtraggio", "raffreddamento", "fermentazione", "altro"}; 
	protected int idCotta;
	protected String fase;
	protected double litri;
	protected double densità;
	protected double gradi_plato;
	protected double gradi_brix;
	protected String note;
	
	protected boolean ultimato;
	
	public void stampa(){
		System.out.println("--------------------------------");
		System.out.println("STAMPA ANALISI");
		System.out.println("idCotta: "+this.idCotta);
		System.out.println("Fase: "+this.fase);
		System.out.println("Litri: "+this.litri);
		System.out.println("Densità: "+this.densità);
		System.out.println("Gradi plato: "+this.gradi_plato);
		System.out.println("Gradi brix: "+this.gradi_brix);
		System.out.println("Note: "+this.note);
	}
	
	protected boolean isCompleta(){
		if((!this.fase.equals(""))&&(this.idCotta!=-1)&&(this.litri!=-1)&&(this.densità!=-1)&&(this.gradi_plato!=-1)&&(this.gradi_brix!=-1)) return true;
		else return false;
	}
	
	public Analisi(){
		this.fase="";
		this.idCotta=-1;
		this.litri=-1;
		this.gradi_plato=-1;
		this.gradi_brix=-1;
		this.densità=-1;
		this.note="";
		this.ultimato=false;
	}
	
	public Analisi(String fase, double litri, double densità, double gradi_plato, double gradi_brix, String note) throws beerException {
		if((Arrays.asList(possibiliFasi).contains(fase.toLowerCase()))&&(litri>0)&&(densità>0)&&(gradi_plato>0)&&(gradi_brix>0)){
			this.fase=fase;
			this.litri=litri;
			this.densità=densità;
			this.gradi_plato=gradi_plato;
			this.gradi_brix=gradi_brix;
			this.note=note;
			this.ultimato=true;
		}else throw new beerException("Operazione non riuscita. Fase non valida o parametro negativo");
	}
	
	public Analisi(String fase, double litri, double densità, double gradi_plato, double gradi_brix) throws beerException {
		if((Arrays.asList(possibiliFasi).contains(fase.toLowerCase()))&&(litri>0)&&(densità>0)&&(gradi_plato>0)&&(gradi_brix>0)){
		this.fase=fase;
		this.litri=litri;
		this.densità=densità;
		this.gradi_plato=gradi_plato;
		this.gradi_brix=gradi_brix;
		this.ultimato=true;
		}else throw new beerException("Operazione non riuscita. Fase non valida o parametro negativo");
	}
	
	@SuppressWarnings("finally")
	public AnalisiFermentazione toAnalisiFermentazione(double grado_alcolico, double grado_amaro) throws beerException{
		if (((Double.valueOf(grado_alcolico).equals(Double.NaN))||(grado_alcolico>0))&&
				((Double.valueOf(grado_amaro).equals(Double.NaN))||(grado_amaro>0))){
			AnalisiFermentazione a= new AnalisiFermentazione(this.fase, this.litri, this.densità, this.gradi_plato, this.gradi_brix, this.note, grado_alcolico, grado_amaro);
			try {a.setIdCotta(this.idCotta);
			} catch (dbException e) {
				e.printStackTrace();
				a.idCotta=-1;
			}finally{ return a;
			}
		}else throw new beerException("Operazione non riuscita. Parametro non validi");	
	}

	public static String[] getPossibilifasi() {
		return possibiliFasi;
	}
	
	public int getIdCotta() {
		return idCotta;
	}

	public String getFase() {
		return fase;
	}
	public double getLitri() {
		return litri;
	}
	public double getDensità() {
		return densità;
	}
	public double getGradi_plato() {
		return gradi_plato;
	}
	public double getGradi_brix() {
		return gradi_brix;
	}
	public String getNote() {
		return note;
	}
	public boolean isUltimato() {
		return ultimato;
	}

	public void setFase(String fase) throws beerException {
		if (Arrays.asList(possibiliFasi).contains(fase.toLowerCase())){
			this.fase = fase;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Fase non valida");
		
	}

	public void setIdCotta(int idCotta) throws dbException {
		if(idCotta>0) {
			this.idCotta = idCotta;
			if (this.isCompleta()) ultimato=true;
		}
		else throw new dbException("Id negativo");
	}

	public void setLitri(double litri) throws beerException {
		if (litri>0){
			this.litri = litri;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Operazione non riuscita. Inserire un numero di litri positivo");
	}

	public void setDensità(double densità) throws beerException {
		if (densità>0){
			this.densità = densità;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Operazione non riuscita. Inserire una densità positiva");
	}

	public void setGradi_plato(double gradi_plato) throws beerException {
		if (gradi_plato>0){
			this.gradi_plato = gradi_plato;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Operazione non riuscita. Inserire un numero di grado positivo");
	}

	public void setGradi_brix(double gradi_brix) throws beerException {
		if (gradi_brix>0){
			this.gradi_brix = gradi_brix;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Operazione non riuscita. Inserire un numero di gradi positivo");
	}

	public void setNote(String note) {
		this.note = note;
	}
}
