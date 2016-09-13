package beer;

import java.sql.Date;
import java.time.Instant;

import anelloAromaticoServerPack.dbException;

public class Giudizio {
	protected static final int limiteProblematica=13;
	protected static final int limiteMediocre=20;
	protected static final int limiteBuona=29;
	protected static final int limiteMoltoBuona=37;
	protected static final int limiteEccellente=44;
	protected static final int limiteEccezionale=50;
	protected static final int votoMaxVisivo=3;
	protected static final int votoMaxOlfattivo=12;
	protected static final int votoMaxGustativo=20;
	protected static final int votoMaxSensazioniBoccali=5;
	protected static final int votoMaxGenerale=10;
	protected int id;
	public int getId() {
		return id;
	}
	public void setId(int id) throws dbException {
		if (id>0) this.id = id;
		else throw new dbException("Id negativo");
	}

	protected String nomeGiudice;
	protected Date data;
	protected String esameVisivo;
	protected int votoVisivo;
	protected String esameOlfattivo;
	protected int votoOlfattivo;
	protected String esameGustativo;
	protected int votoGustativo;
	protected String esameSensazioniBoccali;
	protected int votoSensazioniBoccali;
	protected String esameGenerale;
	protected int votoGenerale;
	protected boolean acetaldeide;
	protected boolean alcoliSuperiori;
	protected boolean astringenza;
	protected boolean diacetile;
	protected boolean dms;
	protected boolean esteriFruttati;
	protected boolean erbaTagliata;
	protected boolean colpoDiLuce;
	protected boolean metallico;
	protected boolean fenolico;
	protected boolean solvente;
	protected boolean muffaStantio;
	protected boolean acido;
	protected boolean sulfureo;
	protected boolean vegetale;
	protected boolean lievito;
	protected boolean ossidazione;
	protected boolean autolisi;
	protected String altro;
	protected String città;
	protected String note;
	
	protected boolean difettiUltimati;
	
	public void stampa(){
		System.out.println("-------------------------");
		System.out.println("STAMPA GIUDIZIO");
		System.out.println("Nome giudice: "+this.nomeGiudice);
		System.out.println("data: "+this.data);
		System.out.println("Voto visivo: "+this.votoVisivo+" | Esame visivo: "+this.esameVisivo);
		System.out.println("Voto olfattivo: "+this.votoOlfattivo+" | Esame olfattivo: "+this.esameOlfattivo);
		System.out.println("Voto gustativo: "+this.votoGustativo+" | Esame gustativo: "+this.esameGustativo);
		System.out.println("Voto sens boccali: "+this.votoSensazioniBoccali+" | Esame sens boccali: "+this.esameSensazioniBoccali);
		System.out.println("Voto generale: "+this.votoGenerale+" | Esame generale: "+this.esameGenerale);
		System.out.println("Città: "+this.getCittà());
		String difetti="";
		if(this.acetaldeide) difetti=difetti.concat("acetaldeide ");
		if(this.alcoliSuperiori) difetti=difetti.concat("alcoli_superiori ");
		if(this.acido) difetti=difetti.concat("acido ");
		if(this.astringenza) difetti=difetti.concat("astringenza ");
		if(this.autolisi) difetti=difetti.concat("autolisi ");
		if(this.colpoDiLuce) difetti=difetti.concat("colpo_di_luce ");
		if(this.diacetile) difetti=difetti.concat("diacetile ");
		if(this.dms) difetti=difetti.concat("dms ");
		if(this.erbaTagliata) difetti=difetti.concat("erba_tagliata ");
		if(this.esteriFruttati) difetti=difetti.concat("esteri_fruttati ");
		if(this.fenolico) difetti=difetti.concat("fenolico ");
		if(this.lievito) difetti=difetti.concat("lievito ");
		if(this.metallico) difetti=difetti.concat("metallico ");
		if(this.muffaStantio) difetti=difetti.concat("muffa_stantio ");
		if(this.ossidazione) difetti=difetti.concat("ossidazione ");
		if(this.solvente) difetti=difetti.concat("solvente ");
		if(this.sulfureo) difetti=difetti.concat("sulfureo ");
		if(this.vegetale) difetti=difetti.concat("vegetale ");
		if(this.altro!=null) difetti=difetti.concat(this.altro);
		while(difetti.endsWith(" ")){
			difetti=difetti.substring(0, difetti.length()-1);
		}
		System.out.println("Difetti :"+difetti);
		System.out.println("Note: "+this.note);
	}
	
	private boolean votiUltimati(){
		if ((this.votoGenerale>-1)&&(this.votoGustativo>-1)&&(this.votoOlfattivo>-1)&&(this.votoSensazioniBoccali>-1)&&(this.votoVisivo>-1)){
			return true;
		}else return false;
	}
	/**
	 * Constructor
	 * Costruisce un giudizio vuoto. Prima di qualsiasi altra operazione di inserimento, occorre inserire i voti
	 */
 	public Giudizio() {
		this.votoGenerale=-1;
		this.votoGustativo=-1;
		this.votoOlfattivo=-1;
		this.votoSensazioniBoccali=-1;
		this.votoVisivo=-1;
		this.esameGenerale="";
		this.esameGustativo="";
		this.esameOlfattivo="";
		this.esameSensazioniBoccali="";
		this.esameVisivo="";
		this.altro="";
		this.difettiUltimati=false;
	}

 	/**
 	 * Constructor
 	 * Costruisce un nuovo giudizi con i voti e le valutazioni passate come parametri
 	 * @param esame_visivo
 	 * @param voto_visivo
 	 * @param esame_olfattivo
 	 * @param voto_olfattivo
 	 * @param esame_gustativo
 	 * @param voto_gustativo
 	 * @param esame_sensazioni_boccali
 	 * @param voto_sensazioni_boccali
 	 * @param esame_generale
 	 * @param voto_generale
 	 * @throws beerException 
 	 */
	public Giudizio(String esameVisivo, int votoVisivo, String esameOlfattivo, int votoOlfattivo, 
			String esameGustativo, int votoGustativo, String esameSensazioniBoccali, int votoSensazioniBoccali, 
			String esameGenerale, int votoGenerale) throws beerException{
		if ((votoVisivo>=0)&&(votoVisivo<=votoMaxVisivo)&&(votoOlfattivo>=0)&&(votoOlfattivo<=votoMaxOlfattivo)&&(votoGustativo>=0)&&(votoGustativo<=votoMaxGustativo)&&(votoSensazioniBoccali>=0)&&(votoSensazioniBoccali<=votoMaxSensazioniBoccali)&&(votoGenerale>=0)&&(votoGenerale<=votoMaxGenerale)){
			this.esameVisivo=esameVisivo;
			this.votoVisivo=votoVisivo;
			this.esameOlfattivo=esameOlfattivo;
			this.votoOlfattivo=votoOlfattivo;
			this.esameGustativo=esameGustativo;
			this.votoGustativo=votoGustativo;
			this.esameSensazioniBoccali=esameSensazioniBoccali;
			this.votoSensazioniBoccali=votoSensazioniBoccali;
			this.esameGenerale=esameGenerale;
			this.votoGenerale=votoGenerale;
			this.difettiUltimati=false;
		}else throw new beerException("Voti non validi");
	}
	/**
	 * Imposta tutti i possibili difetti della cotta analizzata
	 * @param acetaldeide
	 * @param alcoli_superiori
	 * @param astringenza
	 * @param diacetile
	 * @param dms
	 * @param esteri_fruttati
	 * @param erba_tagliata
	 * @param colpo_di_luce
	 * @param metallico
	 * @param fenolico
	 * @param solvente
	 * @param muffa_stantio
	 * @param acido
	 * @param sulfureo
	 * @param vegetale
	 * @param lievito
	 * @param ossidazione
	 * @param autolisi
	 * @param altro
	 * @throws beerException
	 */
	public void setDifetti(	boolean acetaldeide, boolean alcoliSuperiori, boolean astringenza,	boolean diacetile,
			boolean dms, boolean esteriFruttati, boolean erbaTagliata, boolean colpoDiLuce, boolean metallico,
			boolean fenolico, boolean solvente, boolean muffaStantio, boolean acido, boolean sulfureo,	boolean vegetale, 
			boolean lievito, boolean ossidazione, boolean autolisi, String altro) throws beerException{
		if(this.votiUltimati()){
			this.acetaldeide=acetaldeide;
			this.alcoliSuperiori=alcoliSuperiori;
			this.astringenza=astringenza;
			this.diacetile=diacetile;
			this.dms=dms;
			this.esteriFruttati=esteriFruttati;
			this.erbaTagliata=erbaTagliata;
			this.colpoDiLuce=colpoDiLuce;
			this.metallico=metallico;
			this.fenolico=fenolico;
			this.solvente=solvente;
			this.muffaStantio=muffaStantio;
			this.acido=acido;
			this.sulfureo=sulfureo;
			this.vegetale=vegetale;
			this.lievito=lievito;
			this.ossidazione=ossidazione;
			this.autolisi=autolisi;
			if((altro==null)||(altro=="")) this.altro=null;
			else this.altro=altro;
			this.difettiUltimati=true;
		}else throw new beerException("Voti non validi");
	}
	/**
	 * Converte il numero passato come parametro in un giudizio finale in base ai parametri delle linee guida
	 * @param num somma dei voti che si vuole convertire in giudizio finale
	 * @return una stringa indicante la valutazione finale in lingua italiana
	 */
 	public static String giudizioTotale(int num){
		if (num<0) return null;
		else if (num<=limiteProblematica){
			return "Problematica";
		}else if (num<=limiteMediocre){
			return "Mediocre";
		}else if(num<=limiteBuona){
			return "Buona";
		}else if (num<=limiteMoltoBuona){
			return "Molto buona";
		}else if (num<=limiteEccellente){
			return "Eccellente";
		}else if (num<=limiteEccezionale){
			return "Eccezionale";
		}
		return null;
	}
	/**
	 * Ritorna il totale dei voti ricevuti su un massimo di 50 punti
	 * @return il totale dei voti ricevuti
	 * @throws beerException
	 */
 	public int votoTotale() throws beerException{
 		if(this.votiUltimati()){
 			return this.votoVisivo+this.votoOlfattivo+this.votoGustativo+this.votoSensazioniBoccali+this.votoVisivo;
 		}else throw new beerException("Operazione non valida. Inserire i voti");
 	}
 	/**
 	 * Ritorna il giudizio totale
 	 * @return il giudizio totale
 	 * @throws beerException
 	 */
	public String giudizioTotale() throws beerException{
		if (this.votiUltimati()){
			int somma= this.votoVisivo+this.votoOlfattivo+this.votoGustativo+this.votoSensazioniBoccali+this.votoVisivo;
			return giudizioTotale(somma);
		}else throw new beerException("I voti non sono validi");
	}

	
	public String getNomeGiudice() {
		return nomeGiudice;
	}

	public void setNome_giudice(String nomeGiudice) {
		if (!nomeGiudice.isEmpty()) this.nomeGiudice = nomeGiudice;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		if (!data.after(Date.from(Instant.now()))) this.data = data;
	}

	public String getEsameVisivo() {
		return esameVisivo;
	}

	public void setEsameVisivo(String esameVisivo) {
		this.esameVisivo = esameVisivo;
	}

	public int getVotoVisivo() {
		return votoVisivo;
	}

	public void setVotoVisivo(int votoVisivo) throws beerException {
		if ((votoVisivo>=0)&&(votoVisivo<=votoMaxVisivo))this.votoVisivo = votoVisivo;
		else throw new beerException("Il voto inserito non è valido");
	}

	public String getEsameOlfattivo() {
		return esameOlfattivo;
	}

	public void setEsameOlfattivo(String esameOlfattivo) {
		this.esameOlfattivo = esameOlfattivo;
	}

	public int getVotoOlfattivo() {
		return votoOlfattivo;
	}

	public void setVotoOlfattivo(int votoOlfattivo) throws beerException {
		if ((votoOlfattivo>=0)&&(votoOlfattivo<=votoMaxOlfattivo))this.votoOlfattivo = votoOlfattivo;
		else throw new beerException("Il voto inserito non è valido");
	}

	public String getEsameGustativo() {
		return esameGustativo;
	}

	public void setEsameGustativo(String esameGustativo) {
		this.esameGustativo = esameGustativo;
	}

	public int getVotoGustativo() {
		return votoGustativo;
	}

	public void setVotoGustativo(int votoGustativo) throws beerException {
		if ((votoGustativo>=0)&&(votoGustativo<=votoMaxGustativo))this.votoGustativo = votoGustativo;
		else throw new beerException("Il voto inserito non è valido");
	}

	public String getEsameSensazioniBoccali() {
		return esameSensazioniBoccali;
	}

	public void setEsameSensazioniBoccali(String esameSensazioniBoccali) {
		this.esameSensazioniBoccali = esameSensazioniBoccali;
	}

	public int getVotoSensazioniBoccali() {
		return votoSensazioniBoccali;
	}

	public void setVotoSensazioniBoccali(int votoSensazioniBoccali) throws beerException {
		if ((votoSensazioniBoccali>=0)&&(votoSensazioniBoccali<=votoMaxSensazioniBoccali))this.votoSensazioniBoccali = votoSensazioniBoccali;
		else throw new beerException("Il voto inserito non è valido");
	}

	public String getEsameGenerale() {
		return esameGenerale;
	}

	public void setEsameGenerale(String esameGenerale) {
		this.esameGenerale = esameGenerale;
	}

	public int getVotoGenerale() {
		return votoGenerale;
	}

	public void setVotoGenerale(int votoGenerale) throws beerException {
		if ((votoGenerale>=0)&&(votoGenerale<votoMaxGenerale))this.votoGenerale = votoGenerale;
		else throw new beerException("Il voto inserito non è valido");
	}

	public boolean isAcetaldeide() {
		return acetaldeide;
	}

	public void setAcetaldeide(boolean acetaldeide) throws beerException {
		if (this.votiUltimati()) this.acetaldeide = acetaldeide;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isAlcoliSuperiori() {
		return alcoliSuperiori;
	}

	public void setAlcoliSuperiori(boolean alcoliSuperiori) throws beerException {
		if (this.votiUltimati()) this.alcoliSuperiori = alcoliSuperiori;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isAstringenza() {
		return astringenza;
	}

	public void setAstringenza(boolean astringenza) throws beerException {
		if (this.votiUltimati()) this.astringenza = astringenza;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isDiacetile() {
		return diacetile;
	}

	public void setDiacetile(boolean diacetile) throws beerException {
		if (this.votiUltimati()) this.diacetile = diacetile;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isDms() {
		return dms;
	}

	public void setDms(boolean dms) throws beerException {
		if (this.votiUltimati()) this.dms = dms;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isEsteriFruttati() {
		return esteriFruttati;
	}

	public void setEsteriFruttati(boolean esteriFruttati) throws beerException {
		if (this.votiUltimati()) this.esteriFruttati = esteriFruttati;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isErbaTagliata() {
		return erbaTagliata;
	}

	public void setErba_Tagliata(boolean erbaTagliata) throws beerException {
		if (this.votiUltimati()) this.erbaTagliata = erbaTagliata;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isColpoDiLuce() {
		return colpoDiLuce;
	}

	public void setColpoDiLuce(boolean colpoDiLuce) throws beerException {
		if (this.votiUltimati()) this.colpoDiLuce = colpoDiLuce;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isMetallico() {
		return metallico;
	}

	public void setMetallico(boolean metallico) throws beerException {
		if (this.votiUltimati()) this.metallico = metallico;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isFenolico() {
		return fenolico;
	}

	public void setFenolico(boolean fenolico) throws beerException {
		if (this.votiUltimati()) this.fenolico = fenolico;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isSolvente() {
		return solvente;
	}

	public void setSolvente(boolean solvente) throws beerException {
		if (this.votiUltimati()) this.solvente = solvente;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isMuffaStantio() {
		return muffaStantio;
	}

	public void setMuffaStantio(boolean muffaStantio) throws beerException {
		if (this.votiUltimati()) this.muffaStantio = muffaStantio;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isAcido() {
		return acido;
	}

	public void setAcido(boolean acido) throws beerException {
		if (this.votiUltimati()) this.acido = acido;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isSulfureo() {
		return sulfureo;
	}

	public void setSulfureo(boolean sulfureo) throws beerException {
		if (this.votiUltimati()) this.sulfureo = sulfureo;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isVegetale() {
		return vegetale;
	}

	public void setVegetale(boolean vegetale) throws beerException {
		if (this.votiUltimati()) this.vegetale = vegetale;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isLievito() {
		return lievito;
	}

	public void setLievito(boolean lievito) throws beerException {
		if (this.votiUltimati()) this.lievito = lievito;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isOssidazione() {
		return ossidazione;
	}

	public void setOssidazione(boolean ossidazione) throws beerException {
		if (this.votiUltimati()) this.ossidazione = ossidazione;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public boolean isAutolisi() {
		return autolisi;
	}

	public void setAutolisi(boolean autolisi) throws beerException {
		if (this.votiUltimati()) this.autolisi = autolisi;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public String getAltro() {
		return altro;
	}

	public void setAltro(String altro) throws beerException {
		if (this.votiUltimati()) this.altro = altro;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}

	public String getCittà() {
		return città;
	}

	public void setCittà(String città) {
		this.città = città;
	}

	public String getNote() {
		return note;
	}

	public void setNote(String note) throws beerException {
		if (this.votiUltimati()) this.note = note;
		else throw new beerException("Operazione non valida. Inserire prima i voti");
	}



	public static int getLimiteProblematica() {
		return limiteProblematica;
	}

	public static int getLimiteMediocre() {
		return limiteMediocre;
	}

	public static int getLimiteBuona() {
		return limiteBuona;
	}

	public static int getLimiteMoltobuona() {
		return limiteMoltoBuona;
	}

	public static int getLimiteEccellente() {
		return limiteEccellente;
	}

	public static int getLimiteEccezionale() {
		return limiteEccezionale;
	}

	public static int getVotoMaxVisivo() {
		return votoMaxVisivo;
	}

	public static int getVotoMaxOlfattivo() {
		return votoMaxOlfattivo;
	}

	public static int getVotoMaxGustativo() {
		return votoMaxGustativo;
	}

	public static int getVotoMaxSensazioniBoccali() {
		return votoMaxSensazioniBoccali;
	}

	public static int getVotoMaxGenerale() {
		return votoMaxGenerale;
	}
}
