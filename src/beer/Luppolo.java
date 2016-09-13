/**
	Sottoclasse di Ingrediente.
	Aggiunge gli alfa_acidi, che devono essere una quantità positiva.
 */

package beer;

public class Luppolo extends Ingrediente {
	protected double alfa_acidi;
	
	

	@Override
	public void stampa() {
		super.stampa();
		System.out.println("Alfa acidi: "+this.alfa_acidi);
	}

	public double getAlfa_acidi() {
		return alfa_acidi;
	}

	public void setAlfa_acidi(double alfa_acidi) throws beerException {
		if (alfa_acidi>=0){
			this.alfa_acidi = alfa_acidi;
		}else throw new beerException("alfa_acidi must be >=0");
	}

	public Luppolo(String nome) {
		super(nome);
		this.tipologia="luppolo";
	}
}
