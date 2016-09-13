package beer;

import java.time.LocalDate;
import java.time.LocalTime;

public class Gittata {
	protected int num_gittata;
	protected Ingrediente ingrediente;
	protected double quantità;
	protected double numBuste;
	
	public Gittata() {
		this.num_gittata=-1;
		this.ingrediente=null;
		this.quantità=Double.NaN;
		this.numBuste=Double.NaN;
	}
	
	public void stampa(){
		System.out.println("--------------------");
		System.out.println("STAMPA GITTATA");
		System.out.println("Numero gittata: "+this.num_gittata);
		System.out.println("Nome ingrediente: "+this.getIngrediente().getNome());
		System.out.println("Tipo ingrediente: "+this.getIngrediente().getTipo());
		System.out.println("Tipologia ingrediente: "+this.getIngrediente().getTipologia());
		System.out.println("Quantità: "+this.getQuantità());
	}

	public Gittata(int num, Ingrediente ing, double quantità) throws beerException{
		if (num>0) this.num_gittata=num;
		else throw new beerException("Operazione non riuscita. Numero di gittata negativo");
		this.ingrediente=ing;
		if (quantità>0) {
			if (ing instanceof Lievito){
				this.numBuste=quantità;
				this.quantità=this.numBuste*((Lievito) ing).quantità_busta;
			}else this.quantità=quantità;
		}else throw new beerException("Operazione non riuscita. Quantità negativa");
	}

	public int getNum_gittata() {
		return num_gittata;
	}

	public void setNum_gittata(int num_gittata) throws beerException {
		if (num_gittata>0) this.num_gittata = num_gittata;
		else throw new beerException("Operazione non riuscita. Numero di gittata negativo");
	}

	public Ingrediente getIngrediente() {
		return ingrediente;
	}

	public void setIngrediente(Ingrediente ingrediente, double quantità) throws beerException {
		if(quantità>0){
			if((ingrediente instanceof Lievito)){
				this.numBuste=quantità;
			}else this.quantità=quantità;
			this.ingrediente = ingrediente;
		}else throw new beerException("Operazione non riuscita. Quantità negativa");
	}

	public double getQuantità() {
		return quantità;
	}

	public void setQuantità(double quantità) throws beerException {
		if(!(this.ingrediente instanceof Lievito)) this.quantità = quantità;
		else throw new beerException("L'ingrediente è un lievito. Impostare il numero di buste");
	}

	public double getNumBuste() {
		return numBuste;
	}

	public void setNumBuste(double numBuste) throws beerException {
		if (this.ingrediente instanceof Lievito) this.numBuste = numBuste;
		else throw new beerException("L'ingrediente non è un lievito. Impostare la quantità");
	}
	
	public GittataBollitura toGittataBollitura(LocalTime orario) throws beerException{
		GittataBollitura gb=new GittataBollitura(this.num_gittata, this.ingrediente, this.getQuantità(), null);
		gb.setOrario(orario);
		return gb;
	}
	
	public GittataFermentazione toGittataFermentazione(LocalDate data) throws beerException{
		GittataFermentazione gf=new GittataFermentazione(this.num_gittata, this.ingrediente, this.getQuantità(), null);
		gf.setData(data);
		return gf;
	}
	
	public GittataRaffreddamento toGittataRaffreddamento(LocalTime orario) throws beerException{
		GittataRaffreddamento gr=new GittataRaffreddamento(this.num_gittata, this.ingrediente, this.getQuantità(), null);
		gr.setOrario(orario);
		return gr;
	}
}
