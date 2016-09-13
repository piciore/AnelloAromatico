package beer;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import anelloAromaticoServerPack.dbException;
import fasi.Ammostamento;
import fasi.Bollitura;
import fasi.Fermentazione;
import fasi.Filtraggio;
import fasi.Maturazione;
import fasi.Raffreddamento;
import fasi.Rifermentazione;

public class Cotta {
	/*Dati*/
	protected int id;
	protected String nomeRicetta;
	protected Ricetta ricetta;
	protected LocalDate data;
	protected String luogo;
	protected int efficienza;
	
	/*Acqua*/
	protected double quantit‡Acqua;
	protected String luogoAcqua;
	protected double saliMinerali;
	
	/*Fasi*/
	protected static final String[] possibiliFasi={"0_inizializzazione", "1_ammostamento", "2_filtraggio", "3_bollitura", "4_raffreddamento", "5_fermentazione", "6_rifermentazione", "7_maturazione", "8_completa", "altro"};
	protected String faseCorrente;
	
	protected Ammostamento ammostamento;
	protected Filtraggio filtraggio;
	protected Bollitura bollitura;
	protected Raffreddamento raffreddamento;
	protected Fermentazione fermentazione;
	protected Rifermentazione rifermentazione;
	protected Maturazione maturazione;
	protected String note;
	
	/*Elenchi*/
	protected ArrayList<Utilizzo> ingredienti;
	protected ArrayList<Analisi> analisi;
	protected ArrayList<Giudizio> giudizi;
	
	/*Operazioni*/
	protected boolean QAcquaSet;
	protected boolean LAcquaSet;
	protected boolean SMSet;
	
	public void stampa(PrintWriter w) throws IOException{
		/*System.out.println("--------------------");
		System.out.println("STAMPA COTTA");
		System.out.println("Id: "+this.id);
		System.out.println("Nome: "+this.nomeRicetta);
		System.out.println("Data: "+this.data);
		System.out.println("Luogo: "+this.luogo);
		System.out.println("Fase: "+this.getFaseCorrente());
		System.out.println("Acqua: "+this.quantit‡Acqua);
		System.out.println("Luogo acqua: "+this.luogoAcqua);
		System.out.println("Sali minerali: "+this.saliMinerali);
		System.out.println("Note: "+this.note);*/
		PrintWriter out=new PrintWriter(w);
		out.println("--------------------");
		out.println("STAMPA COTTA");
		out.println("Id: "+this.id);
		out.println("Nome: "+this.nomeRicetta);
		out.println("Data: "+this.data);
		out.println("Luogo: "+this.luogo);
		//out.println("Fase: "+this.getFaseCorrente());
		out.println("Acqua: "+this.quantit‡Acqua);
		out.println("Luogo acqua: "+this.luogoAcqua);
		out.println("Sali minerali: "+this.saliMinerali);
		out.println("Note: "+this.note);
		out.flush();
	}
	
	/*Costruttori*/
	public Cotta(String nome) {
		this.nomeRicetta=nome;
		try {
			this.setFaseCorrente("inizializzazione");
		} catch (beerException e) {
			e.printStackTrace();
			this.faseCorrente="0_inizializzazione";
		}
		this.data=null;
		this.ricetta=null;
		this.luogo="";
		this.efficienza=-1;
		
		this.luogoAcqua="";
		this.quantit‡Acqua=Double.NaN;
		this.saliMinerali=Double.NaN;
		this.note="";
		
		this.ammostamento=new Ammostamento();
		this.filtraggio=new Filtraggio();
		this.bollitura=new Bollitura();
		this.raffreddamento=new Raffreddamento();
		this.fermentazione=new Fermentazione();
		this.rifermentazione=new Rifermentazione();
		this.maturazione=new Maturazione();
		
		this.ingredienti=new ArrayList<Utilizzo>();
		this.analisi=new ArrayList<Analisi>();
		this.giudizi=new ArrayList<Giudizio>();
		
		this.QAcquaSet=false;
		this.LAcquaSet=false;
		this.SMSet=false;
	}
	public Cotta(String nome, Ricetta r){
		this(nome);
		this.setRicetta(r);
	}
	public Cotta(String nome, LocalDate d) {
		this(nome);
		this.data=d;
	}
	public Cotta(String nome, LocalDate d, String l){
		this(nome, d);
		this.luogo=l;
	}
	private boolean controlloCompletamentoDati(){
		if((this.nomeRicetta!="")&&(this.data!=null)&&(this.luogo!="")) return true;
		else return false;
	}
	private boolean controlloCompletamentoAcqua(){
		if((this.QAcquaSet)&&(this.LAcquaSet)&&(this.SMSet)) return true;
		else return false;
	}
	private void aggiornaFase(){
		try{
			if((this.controlloCompletamentoDati())&&(this.controlloCompletamentoAcqua())){
				this.setFaseCorrente(1);
				if((this.ammostamento!=null)&&(this.ammostamento.isCompletato())){
					this.setFaseCorrente(2);
					if((this.filtraggio!=null)&&(this.filtraggio.isCompletato())){
						this.setFaseCorrente(3);
						if((this.bollitura!=null)&&(this.bollitura.isCompletato())){
							this.setFaseCorrente(4);
							if((this.raffreddamento!=null)&&(this.raffreddamento.isCompletato())){
								this.setFaseCorrente(5);
								if((this.fermentazione!=null)&&(this.fermentazione.isCompletato())){
									this.setFaseCorrente(6);
									if((this.rifermentazione!=null)&&(this.rifermentazione.isCompletato())){
										this.setFaseCorrente(7);
										if((this.maturazione!=null)&&(this.maturazione.isCompletato())){
											this.setFaseCorrente(8);
										}
									}
								}
							}
						}
					}
				}
			}else this.setFaseCorrente(0);
			//System.out.println("Fase aggiornata a "+this.getFaseCorrente());
		}catch(beerException e){
			e.printStackTrace();
		}
	}
	
	/*GETTERS*/
	public int getId(){
		return id;
	}
	public String getNomeRicetta() {
		return nomeRicetta;
	}
	public Ricetta getRicetta() {
		return ricetta;
	}
	public LocalDate getData() {
		return data;
	}
	public String getLuogo() {
		return luogo;
	}
	public int getEfficienza() {
		return efficienza;
	}
	public double getQuantit‡Acqua() {
		return quantit‡Acqua;
	}
	public String getLuogoAcqua() {
		return luogoAcqua;
	}
	public double getSaliMinerali() {
		return saliMinerali;
	}
	public String getNote() {
		return note;
	}
	public String getFaseCorrente(){
		return this.faseCorrente.substring(2);
	}
	public ArrayList<Utilizzo> getIngredienti() {
		return ingredienti;
	}
	public ArrayList<Analisi> getAnalisi() {
		return analisi;
	}
	public ArrayList<Giudizio> getGiudizi() {
		return giudizi;
	}
	public Ammostamento getAmmostamento() {
		return ammostamento;
	}
	public Filtraggio getFiltraggio() {
		return filtraggio;
	}
	public Bollitura getBollitura() {
		return bollitura;
	}
	public Raffreddamento getRaffreddamento() {
		return raffreddamento;
	}
	public Fermentazione getFermentazione() {
		return fermentazione;
	}
	public Rifermentazione getRifermentazione() {
		return rifermentazione;
	}
	public Maturazione getMaturazione() {
		return maturazione;
	}

	/*SETTERS*/
	public void setId(int id) throws dbException{
		if (id>0) this.id=id;
		else throw new dbException("Id negativo");
	}
	public void setNomeRicetta(String nomeRicetta) {
		this.nomeRicetta = nomeRicetta;
		this.aggiornaFase();
	}
	public void setData(LocalDate data) throws beerException {
		if((this.faseCorrente.compareToIgnoreCase("0")<0)&&(data.isAfter(LocalDate.now()))) throw new beerException("La cotta Ë gi‡ in fase di "+this.getFaseCorrente()+". Impossibile impostare una data d'inizio futura");
		else this.data = data;
		this.aggiornaFase();
	}
	public void setLuogo(String luogo) {
		this.luogo = luogo;
		this.aggiornaFase();
	}
	public void setEfficienza(int efficienza) throws beerException {
		if(this.faseCorrente.compareTo("7")>0) {
			this.efficienza=-1;
			throw new beerException("Impossibile impostare l'efficienza in fase di "+this.getFaseCorrente());
		}else this.efficienza = efficienza;
		this.setFaseCorrente("completa");
	}
	public void setQuantit‡Acqua(double quantit‡Acqua) throws beerException {
		if((Double.valueOf(quantit‡Acqua).equals(Double.NaN))||(quantit‡Acqua>0)) {
			this.quantit‡Acqua = quantit‡Acqua;
			this.QAcquaSet=true;
		}
		else throw new beerException("Operazione non riuscita. Impostare una quantit‡ d'acqua positiva");
		this.aggiornaFase();
	}
	public void setLuogoAcqua(String luogoAcqua) {
		this.luogoAcqua = luogoAcqua;
		this.LAcquaSet=true;
		this.aggiornaFase();
	}
	public void setSaliMinerali(double saliMinerali) throws beerException {
		if((Double.valueOf(saliMinerali).equals(Double.NaN))||(saliMinerali>0)) {
			this.saliMinerali = saliMinerali;
			this.SMSet=true;
		}
		else throw new beerException("Operazione non riuscita. Impostare una quantit‡ di sali minerali positiva");
		this.aggiornaFase();
	}
	public void setNote(String note) {
		this.note = note;
		this.aggiornaFase();
	}
	private void setFaseCorrente(String faseCorrente) throws beerException {
		switch (faseCorrente.toLowerCase()){
		case "inizializzazione": this.faseCorrente="0_inizializzazione"; break;
		case "ammostamento": this.faseCorrente="1_ammostamento"; break;
		case "filtraggio": this.faseCorrente="2_filtraggio"; break;
		case "bollitura": this.faseCorrente="3_bollitura"; break;
		case "raffreddamento": this.faseCorrente="4_raffreddamento"; break;
		case "fermentazione": this.faseCorrente="5_fermentazione"; break;
		case "rifermentazione": this.faseCorrente="6_rifermentazione"; break;
		case "maturazione": this.faseCorrente="7_maturazione"; break;
		case "completa": this.faseCorrente="8_completa"; break;
		case "altro": this.faseCorrente="altro"; break;
		default: throw new beerException("Operazione non riuscita. Impostare una fase valida");
		}
	}
	private void setFaseCorrente(int f) throws beerException{
			switch(f){
			case 0: this.setFaseCorrente("inizializzazione"); break;
			case 1: this.setFaseCorrente("ammostamento"); break;
			case 2: this.setFaseCorrente("filtraggio"); break;
			case 3: this.setFaseCorrente("bollitura"); break;
			case 4: this.setFaseCorrente("raffreddamento"); break;
			case 5: this.setFaseCorrente("fermentazione"); break;
			case 6: this.setFaseCorrente("rifermentazione"); break;
			case 7: this.setFaseCorrente("maturazione"); break;
			case 8: this.setFaseCorrente("completa"); break;
			case 9: this.setFaseCorrente("altro"); break;
			default: this.setFaseCorrente("altro"); break;
			}
	}
	public static int numFase(String fase){
		switch(fase.toLowerCase()){
		case "inizializzazione": return 0;
		case "ammostamento": return 1;
		case "filtraggio": return 2;
		case "bollitura": return 3;
		case "raffreddamento": return 4;
		case "fermentazione": return 5;
		case "rifermentazione": return 6;
		case "maturazione": return 7;
		case "completa": return 8;
		default: return -1;
		}
	}
	public void setIngredienti(ArrayList<Utilizzo> ingredienti) {
		this.ingredienti = ingredienti;
	}
	public void setRicetta(Ricetta ricetta) {
		this.ricetta = ricetta;
	}
	public void setAnalisi(ArrayList<Analisi> analisi) {
		this.analisi = analisi;
	}
	public void setGiudizi(ArrayList<Giudizio> giudizi) {
		this.giudizi = giudizi;
	}
	public void setAmmostamento(Ammostamento ammostamento) throws beerException {
		this.ammostamento = ammostamento;
		System.out.println("Ho messo l'ammostamento");
		//this.ammostamento.completa();
		this.aggiornaFase();
	}
	public void setFiltraggio(Filtraggio filtraggio) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("1")>0) {
			this.filtraggio = filtraggio;
			//this.filtraggio.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void setBollitura(Bollitura bollitura) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("2")>0) {
			this.bollitura = bollitura;
			//this.bollitura.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void setRaffreddamento(Raffreddamento raffreddamento) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("3")>0) {
			this.raffreddamento = raffreddamento;
			//this.raffreddamento.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void setFermentazione(Fermentazione fermentazione) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("4")>0) {
			this.fermentazione = fermentazione;
			//this.fermentazione.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void setRifermentazione(Rifermentazione rifermentazione) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("5")>0) {
			this.rifermentazione = rifermentazione;
			//this.rifermentazione.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void setMaturazione(Maturazione maturazione) throws beerException {
		if (this.faseCorrente.compareToIgnoreCase("6")>0) {
			this.maturazione = maturazione;
			//this.maturazione.completa();
			this.aggiornaFase();
		}else throw new beerException("Operazione non valida. Conrollare la fase della cotta");
	}
	public void completa() throws beerException{
		this.aggiornaFase();
		if(!this.getFaseCorrente().equalsIgnoreCase("completa")) throw new beerException("Impossibile completare la cotta. Impostare tutti i parametri");
	}
}
