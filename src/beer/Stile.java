package beer;

import anelloAromaticoServerPack.dbException;

public class Stile {
	protected int id;
	protected String nome;
	protected String aroma;
	protected String aspetto;
	protected String sapore;
	protected String sensazioni_boccali;
	protected String impressioni_generali;
	protected String storia;
	protected String commenti;
	protected String ingredienti;
	protected String note;
	protected int IBU_min;
	protected int IBU_max;
	protected int SRM_min;
	protected int SRM_max;
	protected int OG_min;
	protected int OG_max;
	protected int FG_min;
	protected int FG_max;
	protected float ABV_min;
	protected float ABV_max;
	protected Categoria categoria;

	public void stampa(){
		System.out.println("-------------------------");
		System.out.println("STAMPA STILE");
		System.out.println("Nome: "+this.nome);
		System.out.println("Note: "+this.note);
		System.out.println("Aroma: "+this.aroma);
		System.out.println("Aspetto: "+this.aspetto);
		System.out.println("Commenti: "+this.commenti);
		System.out.println("Impressioni: "+this.impressioni_generali);
		System.out.println("ingredienti: "+this.ingredienti);
		System.out.println("Id: "+this.id);
		System.out.println("Sapore: "+this.sapore);
		System.out.println("Sensazioni boccali: "+this.sensazioni_boccali);
		System.out.println("Storia: "+this.storia);
		System.out.println("ABV max: "+this.ABV_max+" ABV min: "+this.ABV_min);
		System.out.println("FG max: "+this.FG_max+" FG min: "+this.ABV_min);
		System.out.println("IBU max: "+this.IBU_max+" IBU min: "+this.IBU_min);
		System.out.println("OG max: "+this.OG_max+" OG min: "+this.OG_min);
		System.out.println("SRM max: "+this.SRM_max+" SRM min: "+this.SRM_min);
	}
	
	public Stile(String name) {
		this.nome=name;
	}
	
	public int getId(){
		return id;
	}

	public String getNome() {
		return nome;
	}

	public String getAroma() {
		return aroma;
	}

	public String getAspetto() {
		return aspetto;
	}

	public String getSapore() {
		return sapore;
	}

	public String getSensazioni_boccali() {
		return sensazioni_boccali;
	}

	public String getImpressioni_generali() {
		return impressioni_generali;
	}

	public String getStoria() {
		return storia;
	}

	public String getCommenti() {
		return commenti;
	}

	public String getIngredienti() {
		return ingredienti;
	}

	public String getNote() {
		return note;
	}

	public int getIBU_min() {
		return IBU_min;
	}

	public int getIBU_max() {
		return IBU_max;
	}

	public int getSRM_min() {
		return SRM_min;
	}

	public int getSRM_max() {
		return SRM_max;
	}

	public int getOG_min() {
		return OG_min;
	}

	public int getOG_max() {
		return OG_max;
	}

	public int getFG_min() {
		return FG_min;
	}

	public int getFG_max() {
		return FG_max;
	}

	public float getABV_min() {
		return ABV_min;
	}

	public float getABV_max() {
		return ABV_max;
	}

	public Categoria getCategoria() {
		return categoria;
	}

	public void setId(int id) throws dbException{
		if(id>0) this.id=id;
		else throw new dbException("Id negativo");
	}
	
	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setAroma(String aroma) {
		this.aroma = aroma;
	}

	public void setAspetto(String aspetto) {
		this.aspetto = aspetto;
	}

	public void setSapore(String sapore) {
		this.sapore = sapore;
	}

	public void setSensazioni_boccali(String sensazioni_boccali) {
		this.sensazioni_boccali = sensazioni_boccali;
	}

	public void setImpressioni_generali(String impressioni_generali) {
		this.impressioni_generali = impressioni_generali;
	}

	public void setStoria(String storia) {
		this.storia = storia;
	}

	public void setCommenti(String commenti) {
		this.commenti = commenti;
	}

	public void setIngredienti(String ingredienti) {
		this.ingredienti = ingredienti;
	}

	public void setNote(String note) {
		this.note = note;
	}
	
	public void setIBU(int min, int max) throws beerException{
		if(min<=max){
			if(min<0){
				throw new beerException("min must be >=0");
			}else{
				this.IBU_min=min;
				this.IBU_max=max;
			}		
		}else throw new beerException("min is greater than max");
	}
	
	public void setSRM(int min, int max) throws beerException{
		if(min<=max){
			if(min<0){
				throw new beerException("min must be >=0");
			}else{
				this.SRM_min=min;
				this.SRM_max=max;
			}
		}else throw new beerException("min is greater than max");
	}
	
	public void setOG(int min, int max) throws beerException{
		if(min<=max){
			if(min<0){
				throw new beerException("min must be >=0");
			}else{
				this.OG_min=min;
				this.OG_max=max;
			}
		}else throw new beerException("min is greater than max");
	}
	
	public void setFG(int min, int max) throws beerException{
		if(min<=max){
			if(min<0){
				throw new beerException("min must be >=0");
			}else{
				this.FG_min=min;
				this.FG_max=max;
			}
		}else throw new beerException("min is greater than max");
	}
	
	public void setABV(float min, float max) throws beerException{
		if(min<=max){
			if(min<0){
				throw new beerException("min must be >=0");
			}else{
				this.ABV_min=min;
				this.ABV_max=max;
			}
		}else throw new beerException("min is greater than max");
	}

	public void setCategoria(Categoria categoria) {
		this.categoria = categoria;
	}
	
}
