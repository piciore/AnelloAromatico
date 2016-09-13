/**
	Sottoclasse di Ingrediente.
	Aggiunge il colore_ebc, indicativo numerico del colore del malto, il quale può essere convertito in scala srm.
 */


package beer;

public class Malto extends Ingrediente {
	private final static double ebc2srm=0.508;
	private final static double srm2ebc=1.97;
	protected int colore_ebc;
	
	

	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Colore ebc: "+this.colore_ebc);
	}

	public int getColore_ebc() {
		return colore_ebc;
	}

	public void setColore_ebc(int colore_ebc2) throws beerException {
		if (colore_ebc>=0){
			this.colore_ebc = colore_ebc2;
		}else throw new beerException("colore_ebc must be >=0");
	}
	
	public double SRM(){
		return this.colore_ebc*ebc2srm;
	}

	public static double fromEbcToSrm(double colore_ebc){
		return colore_ebc*ebc2srm;
	}

	public static double fromSrmToEbc(double colore_srm){
		return colore_srm*srm2ebc;
	}
	
	public Malto(String nome) {
		super(nome);
		this.tipologia="malto";
	}

}
