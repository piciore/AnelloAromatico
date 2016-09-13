package beer;

public class AnalisiFermentazione extends Analisi {
	protected double grado_alcolico;
	protected double grado_amaro;
	
	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Grado alcolico: "+this.grado_alcolico);
		System.out.println("Grado amaro: "+this.grado_amaro);
	}
	
	@Override
	protected boolean isCompleta() {
		if((super.isCompleta())&&(this.grado_alcolico!=-1)&&(this.grado_amaro!=-1)) return true;
		else return false;
	}

	public AnalisiFermentazione() {
		super();
		this.grado_alcolico=-1;
		this.grado_alcolico=-1;
		this.ultimato=false;
	}

	public AnalisiFermentazione(String fase, double litri, double densità, double gradi_plato, double gradi_brix,
			String note, double grado_alcolico, double grado_amaro) throws beerException {
		super(fase, litri, densità, gradi_plato, gradi_brix, note);
		if (((Double.valueOf(grado_alcolico).equals(Double.NaN))||(grado_alcolico>0))&&
				((Double.valueOf(grado_amaro).equals(Double.NaN))||(grado_amaro>0))){
			this.grado_alcolico=grado_alcolico;
			this.grado_amaro=grado_amaro;
			this.ultimato=true;
		}else throw new beerException("Operazione non riuscita. Parametro negativo");
	}
	
	public AnalisiFermentazione(String fase, double litri, double densità, double gradi_plato, double gradi_brix, double grado_alcolico, double grado_amaro)
			throws beerException {
		super(fase, litri, densità, gradi_plato, gradi_brix);
		if (((Double.valueOf(grado_alcolico).equals(Double.NaN))||(grado_alcolico>0))&&
				((Double.valueOf(grado_amaro).equals(Double.NaN))||(grado_amaro>0))){
			this.grado_alcolico=grado_alcolico;
			this.grado_amaro=grado_amaro;
			this.ultimato=true;
		}else throw new beerException("Operazione non riuscita. Parametro negativo");
	}

	public double getGrado_alcolico() {
		return grado_alcolico;
	}
	public double getGrado_amaro() {
		return grado_amaro;
	}

	public void setGrado_alcolico(double grado_alcolico) throws beerException {
		if (grado_alcolico>0){
			this.grado_alcolico = grado_alcolico;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Il grado alcolico deve essere positivo");
		
	}

	public void setGrado_amaro(double grado_amaro) throws beerException {
		if(grado_amaro>0){
			this.grado_amaro = grado_amaro;
			if (this.isCompleta()) ultimato=true;
		}else throw new beerException("Il grado amaro deve essere positivo");
	}

	
}
