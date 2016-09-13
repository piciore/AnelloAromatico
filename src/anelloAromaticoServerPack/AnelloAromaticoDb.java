package anelloAromaticoServerPack;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;

import javax.naming.NamingException;

import beer.Analisi;
import beer.AnalisiFermentazione;
import beer.Categoria;
import beer.Cotta;
import beer.Gittata;
import beer.GittataBollitura;
import beer.GittataFermentazione;
import beer.GittataRaffreddamento;
import beer.Giudizio;
import beer.Ingrediente;
import beer.Lievito;
import beer.Luppolo;
import beer.Malto;
import beer.Ricetta;
import beer.Rilievo;
import beer.RilievoAmmostamento;
import beer.RilievoBollitura;
import beer.RilievoFermentazione;
import beer.RilievoFiltraggio;
import beer.Stile;
import beer.Utilizzo;
import beer.UtilizzoLievito;
import beer.beerException;
import fasi.Ammostamento;
import fasi.Bollitura;
import fasi.Fermentazione;
import fasi.Filtraggio;
import fasi.Maturazione;
import fasi.Raffreddamento;
import fasi.Rifermentazione;


public class AnelloAromaticoDb {
	
	private static final int mySqlPort=3306;
	private static final String dbName="anelloaromatico";
	private static final String dbAddress="localhost";
	private static final String dbUser="tomcatserver";
	private static final String dbPass="t0mc@t23";
	
	private Connection connessione;
	private DatabaseMetaData DBdata;
	private ArrayList<String> nomiTabelle;
	
	/**
	 * Costruttore
	 * Stabilisce una nuova connessione al database fisico, prima tramite il pool di connessioni di tomcat, altrimenti manualmente utilizzando jdbc.
	 * Estrae inoltre i dati del DB e riempi l'ArrayList nomiTabelle con l'elenco dei nomi delle varie tabelle
	 * @throws SQLException 
	 */
	public AnelloAromaticoDb() throws SQLException{
		throw new SQLException("Facciamo una prova");
		/*this.connessione=null;
		this.nomiTabelle=new ArrayList<String>();
		try{
			this.connessione=AnelloAromaticoServerUtility.getAnelloAromaticoDBConnection();
			System.out.println("mi connetto subito");
		}catch(NamingException e){
			e.printStackTrace();
			System.out.println("Metodo di connessione alternativo");
			String connectionString="jdbc:mysql://"+dbAddress+":"+mySqlPort+"/"+dbName+"?user="+dbUser+"&password="+dbPass;
			try {
				Class.forName("com.mysql.jdbc.Driver");
				connessione=DriverManager.getConnection(connectionString);
				System.out.println("mi connetto");
			} catch (ClassNotFoundException | SQLException e1) {
				e1.printStackTrace();
			}
		}
		this.DBdata=connessione.getMetaData();
		
		/*Estrae l'elenco dei nomi delle tabelle e riempie l'ArrayList nomiTabelle*/
		//this.aggiornaElencoTabelle();
	}
	
	public void chiudi() throws SQLException{
		this.connessione.close();
	}
	
	/**
	 * Aggiorna l'ArrayList nomiTabelle con l'elenco dei nomi delle tabella presenti sul database, 
	 * utilizzando le informazioni presenti in DBdata
	 * @throws SQLException
	 */
	private void aggiornaElencoTabelle() throws SQLException{
		ResultSet rs=this.DBdata.getTables(null, null, null, null);
		ArrayList<String> tableList=new ArrayList<String>();
		while(rs.next()){
			tableList.add(rs.getString("TABLE_NAME"));
		}
		this.nomiTabelle=tableList;
	}
	
	/**
	 * Restituisce un ResultSet contenente tutte le righe della tabella il cui nome è passato come parametro
	 * @param tableName nome della tabella di cui si vuole visualizzare tutte le righe
	 * @return
	 * @throws SQLException
	 * @throws dbException 
	 */
	private ResultSet getEntireTable(String tableName) throws SQLException, dbException{
		if(this.nomiTabelle.contains(tableName)){
			PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM "+tableName);
			ResultSet rs=stmt.executeQuery();
			return rs;
		}else throw new dbException("Tabella non esistente");
	}
	
	/**
	 * Stampa un qualsiasi resultset passato affiancando i vari campi, separati dal carattere "|"
	 * Utilizzato a scopi di test
	 * @param rs
	 * @throws SQLException
	 */
	public void stampaResultSet(ResultSet rs) throws SQLException{
		while (rs.next()){
			for(int i=0; i<rs.getMetaData().getColumnCount(); i++){
				System.out.print(rs.getString(i+1)+"  |  ");
			}
			System.out.println();
		}
		rs.beforeFirst();
	}
	
	/**
	 * Converte il tipo di un ingrediente (univoco) nel relativo id. Se il tipo non è presente nel db, restituisce -1
	 * @param tipo
	 * @return un intero contenente l'id dell'ingrediente tipo
	 * @throws SQLException
	 */
	private int fromTipoToId(String tipo) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("SELECT id_ingrediente FROM ingrediente WHERE tipo=?");
		stmt.setString(1, tipo);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()) return rs.getInt(1);
		else return -1;
	}
	
	/*Table getters
	 * Restituiscono un ResultSet contenente l'intera tabella specificata grazie al nome del metodo*/
	/**
	 * Restituisce tutti i campi della tabella Analisi in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella Analisi
	 * @throws SQLException
	 */
	public ResultSet getAllAnalisi() throws SQLException{
		ResultSet rs=null;
		try {
			rs=this.getEntireTable("analisi");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella Categoria in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella Categoria
	 * @throws SQLException
	 */
	public ResultSet getAllCategoria() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("categoria");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella Cotta in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella Cotta
	 * @throws SQLException
	 */
	public ResultSet getAllCotta() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("cotta");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella gittata_bollitura in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella gittata_bollitura
	 * @throws SQLException
	 */
	public ResultSet getAllGittataBollitura() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("gittata_bollitura");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella gittata_fermentazione in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella gittata_fermentazione
	 * @throws SQLException
	 */
	public ResultSet getAllGittataFermentazione() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("gittata_fermentazione");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella gittata_raffreddamento in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella gittata_raffreddamento
	 * @throws SQLException
	 */
	public ResultSet getAllGittataRaffreddamento() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("gittata_raffreddamento");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella giudizio in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella giudizio
	 * @throws SQLException
	 */
	public ResultSet getAllGiudizio() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("giudizio");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella ingrediente in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella ingrediente
	 * @throws SQLException
	 */
	public ResultSet getAllIngrediente() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("ingrediente");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella ricetta in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella ricetta
	 * @throws SQLException
	 */
	public ResultSet getAllRicetta() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("ricetta");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella rilievo_ammostamento in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella rilievo_ammostamento
	 * @throws SQLException
	 */
	public ResultSet getAllRilievoAmmostamento() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("rilievo_ammostamento");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella rilievo_bollitura in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella rilievo_bollitura
	 * @throws SQLException
	 */
	public ResultSet getAllRilievoBollitura() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("rilievo_bollitura");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella rilievo_fermentazione in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella rilievo_fermentazione
	 * @throws SQLException
	 */
	public ResultSet getAllRilievoFermentazione() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("rilievo_fermentazione");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella rilievo_filtraggio in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella rilievo_filtraggio
	 * @throws SQLException
	 */
	public ResultSet getAllRilievoFiltraggio() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("filtraggio");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella stile in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella stile
	 * @throws SQLException
	 */
	public ResultSet getAllStile() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("stile");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella utilizzo_cotta_ingrediente in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella utilizzo_cotta_ingrediente
	 * @throws SQLException
	 */
	public ResultSet getAllUtilizzoCottaIngrediente() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("utilizzo_cotta_ingrediente");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella utilizzo_cotta_lievito in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella utilizzo_cotta_lievito
	 * @throws SQLException
	 */
	public ResultSet getAllUtilizzoCottaLievito() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("utilizzo_cotta_lievito");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella utilizzo_ricetta_ingrediente in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella utilizzo_ricetta_ingrediente
	 * @throws SQLException
	 */
	public ResultSet getAllUtilizzoRicettaIngrediente() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("utilizzo_ricetta_ingrediente");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}
	/**
	 * Restituisce tutti i campi della tabella utilizzo_ricetta_lievito in un unico ResultSet
	 * @return Un ResultSet contenente l'intera tabella utiizzo_ricetta_lievito
	 * @throws SQLException
	 */
	public ResultSet getAllUtilizzoRicettaLievito() throws SQLException{
		ResultSet rs=null;
		try {
			rs= this.getEntireTable("utilizzo_ricetta_lievito");
		} catch (dbException e) {
			e.printStackTrace();
		}
		return rs;
	}

	/*Id getters
	 * Ognuno di questi metodi restituisce un'istanza della relativa tabella corrispondente all'identificativo passato come parametro
	 * Lancia un'eccezione in ogni caso di errore SQL sena gestirla in alcun modo*/
	/**
	 * Estrae dal database la riga della tabella analisi corrispondente all'id passato. L'id è composto dalla stringa fase e dall'id della cotta
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param fase
	 * @param idCotta
	 * @return Un ResultSet contenente l'analisi corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getAnalisiById(String fase, int idCotta) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM analisi WHERE fase_analisi=? AND FK_id_cotta=?");
		stmt.setString(1, fase);
		stmt.setInt(2, idCotta);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella categoria corrispondente all'id passato. L'id è un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param idCotta
	 * @return Un ResultSet contenente la categoria corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getCategoriaById(int id)throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM categoria WHERE id_categoria= ?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella cotta corrispondente all'id passato. L'id è un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param id
	 * @return Un ResultSet contenente la cotta corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getCottaById(int id)throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM cotta WHERE id_cotta=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	
	private ResultSet getGittataById(int num, int idCotta, String table)throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM "+table+" WHERE num_gittata=? AND FK_id_cotta=?");
		stmt.setInt(1, num);
		stmt.setInt(2, idCotta);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella gittata_bollitura corrispondente all'id passato.
	 * L'id è composto dall'id della cotta e dal numero della gittata (intero positivo)
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente la gittata corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getGittataBollituraById(int num, int idCotta)throws SQLException{
		return this.getGittataById(num, idCotta, "gittata_bollitura");
	}
	/**
	 * Estrae dal database la riga della tabella gittata_fermentazione corrispondente all'id passato.
	 * L'id è composto dall'id della cotta e dal numero della gittata (intero positivo)
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente la gittata corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getGittataFermentazioneById(int num, int idCotta) throws SQLException{
		return this.getGittataById(num, idCotta, "gittata_fermentazione");
	}
	/**
	 * Estrae dal database la riga della tabella gittata_raffreddamento corrispondente all'id passato.
	 * L'id è composto dall'id della cotta e dal numero della gittata (intero positivo)
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente la gittata corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getGittataRaffreddamentoById(int num, int idCotta) throws SQLException{
		return this.getGittataById(num, idCotta, "gittata_raffreddamento");
	}
	/**
	 * Estrae dal database la riga della tabella giudizio corrispondente all'id passato.
	 * L'id è composto da un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param id
	 * @return Un ResultSet contenente il giudizio corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getGiudizioById(int id) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM giudizio WHERE id_giudizio=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella ingrediente corrispondente all'id passato.
	 * L'id è composto da un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param id
	 * @return Un ResultSet contenente l'ingrediente corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getIngredienteById(int id) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM ingrediente WHERE id_ingrediente=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella ricetta corrispondente all'id passato.
	 * L'id è composto da un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param id
	 * @return Un ResultSet contenente la ricetta corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getRicettaById(int id)throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM ricetta WHERE id_ricetta=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	
	private ResultSet getRilievoById(int num, int idCotta, String table) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM "+table+" WHERE num_rilievo=? AND FK_id_cotta=?");
		stmt.setInt(1, num);
		stmt.setInt(2, idCotta);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database la riga della tabella rilievo_ammostamento corrispondente all'id passato.
	 * L'id è composto da un intero positivo indicante il numero del rilievo e dall'id della cotta
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente il rilievo corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getRilievoAmmostamentoById(int num, int idCotta) throws SQLException{
		return this.getRilievoById(num, idCotta, "rilievo_ammostamento");
	}
	/**
	 * Estrae dal database la riga della tabella rilievo_bollitura corrispondente all'id passato.
	 * L'id è composto da un intero positivo indicante il numero del rilievo e dall'id della cotta
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente il rilievo corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getRilievoBollituraById(int num, int idCotta)throws SQLException{
		return this.getRilievoById(num, idCotta, "rilievo_bollitura");
	}
	/**
	 * Estrae dal database la riga della tabella rilievo_fermentazione corrispondente all'id passato.
	 * L'id è composto da un intero positivo indicante il numero del rilievo e dall'id della cotta
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente il rilievo corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getRilievoFermentazioneById(int num, int idCotta)throws SQLException{
		return this.getRilievoById(num, idCotta, "rilievo_fermentazione");
	}
	/**
	 * Estrae dal database la riga della tabella rilievo_filtraggio corrispondente all'id passato.
	 * L'id è composto da un intero positivo indicante il numero del rilievo e dall'id della cotta
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param num
	 * @param idCotta
	 * @return Un ResultSet contenente il rilievo corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getRilievoFiltraggioById(int num, int idCotta)throws SQLException{
		return this.getRilievoById(num, idCotta, "rilievo_filtraggio");
	}
	/**
	 * Estrae dal database la riga della tabela stile corrispondente all'id passato
	 * L'id è un intero positivo
	 * Se l'id non è presente, restituisce un ResultSet vuoto
	 * @param id
	 * @return Un ResultSet contenente il rilievo corrispondente all'id passato
	 * @throws SQLException
	 */
	public ResultSet getStileById(int id) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM stile WHERE id_stile=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae l'utilizzo di un ingrediente in una cotta dati l'id dell'ingrediente e della cotta
	 * L'id dell'ingrediente e della cotta sono due interi positivi
	 * @param idCotta 
	 * @param idIngrediente
	 * @return Un ResultSet contenente l'utilizzo
	 * @throws SQLException
	 */
	public ResultSet getUtilizzoCottaIngredienteById(int idCotta, int idIngrediente) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM utilizzo_cotta_ingrediente WHERE FK_id_cotta=? AND FK_id_ingrediente=?");
		stmt.setInt(1, idCotta);
		stmt.setInt(2, idIngrediente);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae l'utilizzo di un lievito in una cotta dati  il tipo del lievito e l'id della cotta
	 * Il tipo del lievito è una stringa alfanumerica e l'id della cotta è un intero positivo
	 * @param idCotta 
	 * @param tipoLievito
	 * @return Un ResultSet contenente l'utilizzo
	 * @throws SQLException
	 */
	public ResultSet getUtilizzoCottaLievitoById(int idCotta, String tipoLievito) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM utilizzo_cotta_lievito WHERE FK_id_cotta=? AND FK_tipo_lievito=?");
		stmt.setInt(1, idCotta);
		stmt.setString(2, tipoLievito);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae l'utilizzo di un ingrediente in una ricetta dati l'id dell'ingrediente e della ricetta
	 * L'id dell'ingrediente e della ricetta sono due interi positivi
	 * @param idRicetta 
	 * @param idIngrediente
	 * @return Un ResultSet contenente l'utilizzo
	 * @throws SQLException
	 */
	public ResultSet getUtilizzoRicettaIngredienteById(int idRicetta, int idIngrediente) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM utilizzo_ricetta_ingrediente WHERE FK_id_ricetta=? AND FK_id_ingrediente=?");
		stmt.setInt(1, idRicetta);
		stmt.setInt(2, idIngrediente);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae l'utilizzo di un lievito in una cotta dati  il tipo del lievito e l'id della ricetta
	 * Il tipo del lievito è una stringa alfanumerica e l'id della ricetta è un intero positivo
	 * @param idCotta 
	 * @param tipoLievito
	 * @return Un ResultSet contenente l'utilizzo
	 * @throws SQLException
	 */
	public ResultSet getUtilizzoRicettaLievitoById(int idRicetta, String tipoLievito) throws SQLException{
		PreparedStatement stmt = this.connessione.prepareStatement("SELECT * FROM utilizzo_ricetta_lievito WHERE FK_id_ricetta=? AND FK_tipo_lievito=?");
		stmt.setInt(1, idRicetta);
		stmt.setString(2, tipoLievito);
		ResultSet rs=stmt.executeQuery();
		return rs;
	}
	/**
	 * Estrae dal database l'analisi corrispondente all'id passato
	 * L'id è composto da una stringa contenente la fase e da un intero positivo contenente l'id della cotta
	 * @param fase
	 * @param idCotta
	 * @return L'Analisi corrispondente all'id specificato
	 * @throws SQLException
	 * @throws beerException in caso di dati errati memorizzati sul database
	 * @throws dbException se l'id passato non esiste sul database
	 */
	public Analisi getAnalisiFromId(String fase, int idCotta) throws SQLException, beerException, dbException{
		ResultSet rs=this.getAnalisiById(fase, idCotta);
		Analisi a=new Analisi();
		if(rs.next()){
			a.setFase(fase);
			a.setIdCotta(idCotta);
			if(rs.getObject("litri")!=null) a.setLitri(rs.getDouble("litri"));
			if(rs.getObject("densità")!=null) a.setDensità(rs.getDouble("densità"));
			if(rs.getObject("gradi_plato")!=null) a.setGradi_plato(rs.getDouble("gradi_plato"));
			if(rs.getObject("gradi_brix")!=null) a.setGradi_brix(rs.getDouble("gradi_brix"));
			if(rs.getObject("note")!=null) a.setNote(rs.getString("note"));
			if (fase=="fermentazione"){
				double gradoAlc;
				double gradoAm;
				if(rs.getObject("grado_alcolico")!=null) gradoAlc=rs.getDouble("grado_alcolico");
				else gradoAlc=Double.NaN;
				if(rs.getObject("grado_amaro")!=null) gradoAm=rs.getDouble("grado_amaro");
				else gradoAm=Double.NaN;
				return a.toAnalisiFermentazione(gradoAlc, gradoAm);
			}
		}else throw new dbException("Id non trovato");
		return a;
	}
	/**
	 * Estrae dal database la categoria corrispondente all'id passato
	 * L'id è un intero positivo
	 * @param id
	 * @return La Categoria corrispondente all'id specificato
	 * @throws SQLException
	 * @throws dbException se l'id passato non esiste sul database
	 */
	public Categoria getCategoriaFromId(int id) throws SQLException, dbException{
		PreparedStatement stmt=this.connessione.prepareStatement("SELECT * FROM categoria WHERE id_categoria=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()){
			Categoria c=new Categoria(rs.getString("nome"), rs.getString("note"));
			c.setId(rs.getInt("id_categoria"));
			return c;
		}else throw new dbException("Id non trovato");
	}
	
	/**
	 * Estrae dal database la cotta corrispondetne all'id specificato
	 * La cotta avrà sarà completa di tutte le informazioni presenti sul database fino alla sua fase.
	 * Ad esempio, se una cotta è in fase di fermentazione, saranno ricavate tutte le informazioni fino alla fase di fermentazione.
	 * Si assume che tutte le fasi precedenti a quella indicata sul database siano state concluse e in caso di campi nulli, 
	 * questi vengono considerati come operazioni effettuate senza la relativa misura 
	 * @param id
	 * @return Una Cotta corrispondente all'id specificato 
	 * @throws SQLException
	 * @throws dbException
	 * @throws beerException
	 */
	public Cotta getCottaFromId(int id) throws SQLException, dbException, beerException{
		//Cerco l'id e, se lo trovo, preparo la cotta
		PreparedStatement stmt=this.connessione.prepareStatement("SELECT * FROM cotta WHERE id_cotta=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()){
			
			Ricetta ric=this.getRicettaFromId(rs.getInt("FK_id_ricetta"));
			//Preparo la cotta da restituire con nome della ricetta, data e luogo (NOT NULL sul DB)
			Cotta c=new Cotta(ric.getNome(), rs.getDate("data").toLocalDate(), rs.getString("luogo"));
			c.setId(rs.getInt("id_cotta"));
			c.setRicetta(ric);
			//Ricavo la fase della cotta in modo da sapere quante informazioni aggiungere
			int numFase=Cotta.numFase(rs.getString("fase"));
			if(numFase>=0){
			
				//Aggiungo le informazioni relative all'acqua
			double quantitàAcqua;
			if(rs.getObject("quantità_acqua")==null) quantitàAcqua=Double.NaN;
			else quantitàAcqua=rs.getDouble("quantità_acqua");
			c.setQuantitàAcqua(quantitàAcqua);
			c.setLuogoAcqua(rs.getString("luogo_acqua"));
			double saliMinerali;
			if(rs.getObject("sali_minerali")==null) saliMinerali=Double.NaN;
			else saliMinerali=rs.getDouble("sali_minerali");
			c.setSaliMinerali(saliMinerali);
			c.setNote(rs.getString("note"));
			if(numFase>=1){
			
				//Aggiungo l'ammostamento
			double litriAcquaAmmostamento;
			if(rs.getObject("litri_acqua_ammostamento")==null) litriAcquaAmmostamento=Double.NaN;
			else litriAcquaAmmostamento=rs.getDouble("litri_acqua_ammostamento");
			Ammostamento a=new Ammostamento(litriAcquaAmmostamento);
				//Aggiungo i rilievi_ammostamento all'ammostamento
				PreparedStatement sRilievi=this.connessione.prepareStatement("SELECT num_rilievo, FK_id_cotta FROM rilievo_ammostamento WHERE FK_id_cotta=?");
				sRilievi.setInt(1, id);
				ResultSet rsRilievi=sRilievi.executeQuery();
				while(rsRilievi.next()){
					a.addRilievo(this.getRilievoAmmostamentoFromId(rsRilievi.getInt(1), rsRilievi.getInt(2)));
				}
			if(numFase!=1) a.completa();
			a.setData(this.fromIdToDataCotta(id));
			c.setAmmostamento(a);
			if(numFase>=2){
			
				//Aggiungo il filtraggio	
			double litriAcquaFiltraggio;
			if(rs.getObject("litri_acqua_filtraggio")==null) litriAcquaFiltraggio=Double.NaN;
			else litriAcquaFiltraggio=rs.getDouble("litri_acqua_filtraggio");
			Filtraggio fil=new Filtraggio(litriAcquaFiltraggio);
				//Aggiungo i rilievi_filtraggio al filtraggio
				sRilievi=this.connessione.prepareStatement("SELECT num_rilievo, FK_id_cotta FROM rilievo_filtraggio WHERE FK_id_cotta=?");
				sRilievi.setInt(1, id);
				rsRilievi=sRilievi.executeQuery();
				while(rsRilievi.next()){
					fil.addRilievo(this.getRilievoFiltraggioFromId(rsRilievi.getInt(1), rsRilievi.getInt(2)));
				}
			if(numFase!=2) fil.completa();
			fil.setData(this.fromIdToDataCotta(id));
			if((!c.getAmmostamento().inGiornata())||((fil.getRilievi().size()!=0)&&(c.getAmmostamento().getRilievi().size()!=0)&&(fil.getRilievi().get(0).getOrarioInizio().isBefore(c.getAmmostamento().getRilievi().get(0).getOrarioFine())))) 
				fil.setData(fil.getData().plusDays(1));
			c.setFiltraggio(fil);
			if(numFase>=3){
			
				//Aggiungo la bollitura	
			LocalDateTime inizioRiscaldamento;
			if(rs.getObject("orario_inizio_riscaldamento")==null) inizioRiscaldamento=null;
			else inizioRiscaldamento=rs.getTimestamp("orario_inizio_riscaldamento").toLocalDateTime();
			LocalDateTime inizioBollitura;
			if(rs.getObject("orario_inizio_bollitura")==null) inizioBollitura=null;
			else inizioBollitura=rs.getTimestamp("orario_inizio_bollitura").toLocalDateTime();
			Bollitura bol=new Bollitura(inizioRiscaldamento, inizioBollitura);
				//Aggiungo i rilievi_bollitura alla bollitura
				sRilievi=this.connessione.prepareStatement("SELECT num_rilievo, FK_id_cotta FROM rilievo_filtraggio WHERE FK_id_cotta=?");
				sRilievi.setInt(1, id);
				rsRilievi=sRilievi.executeQuery();
				while(rsRilievi.next()){
					bol.addRilievo(this.getRilievoBollituraFromId(rsRilievi.getInt(1), rsRilievi.getInt(2)));
				}
			
				//Aggiungo le gittate_bollitura alla bollitura
				PreparedStatement sGittate=this.connessione.prepareStatement("SELECT num_gittata FROM gittata_bollitura WHERE FK_id_cotta=?");
				sGittate.setInt(1, id);
				ResultSet rsGittate=sGittate.executeQuery();
				while(rsGittate.next()){
					bol.addGittata((GittataBollitura) this.getGittataFromId(rsGittate.getInt(1), "bollitura", id));
				}
			if(numFase!=3) bol.completa();
			c.setBollitura(bol);
			if(numFase>=4){
			
				//Aggiungo il raffreddamento
			LocalDateTime inizioRaffreddamento;
			if(rs.getObject("orario_inizio_raffreddamento")==null) inizioRaffreddamento=null;
			else inizioRaffreddamento=rs.getTimestamp("orario_inizio_raffreddamento").toLocalDateTime();
			LocalDateTime fineRaffreddamento;
			if(rs.getObject("orario_fine_raffreddamento")==null) fineRaffreddamento=null;
			else fineRaffreddamento=rs.getTimestamp("orario_fine_raffreddamento").toLocalDateTime();
			LocalDateTime inizioWhirlpool;
			if(rs.getObject("orario_inizio_whirlpool")==null) inizioWhirlpool=null;
			else inizioWhirlpool=rs.getTimestamp("orario_inizio_whirlpool").toLocalDateTime();
			LocalDateTime fineWhirlpool;
			if(rs.getObject("orario_fine_whirlpool")==null) fineWhirlpool=null;
			else fineWhirlpool=rs.getTimestamp("orario_fine_whirlpool").toLocalDateTime();
			Raffreddamento raf=new Raffreddamento(inizioRaffreddamento, fineRaffreddamento, inizioWhirlpool, fineWhirlpool);
			double tempRaf;
			if(rs.getObject("temperatura_fine_raffreddamento")==null) tempRaf=Double.NaN;
			else tempRaf=rs.getDouble("temperatura_fine_raffreddamento");
			raf.setTemperaturaFineRaffreddamento(tempRaf);
				//Aggiungo le gittate_raffreddamento al raffreddamento
				sGittate=this.connessione.prepareStatement("SELECT num_gittata FROM gittata_raffreddamento WHERE FK_id_cotta=?");
				sGittate.setInt(1, id);
				rsGittate=sGittate.executeQuery();
				while(rsGittate.next()){
					raf.addGittata((GittataRaffreddamento) this.getGittataFromId(rsGittate.getInt(1), "raffreddamento", id));
				}
			if(numFase!=4) raf.completa();
			c.setRaffreddamento(raf);
			if(numFase>=5){
			
				//Aggiungo la fermentazione
			Fermentazione fer=new Fermentazione();
			LocalDate dataInizioFermentazione;
			if(rs.getObject("data_inizio_fermentazione")==null) dataInizioFermentazione=null;
			else dataInizioFermentazione=rs.getDate("data_inizio_fermentazione").toLocalDate();
			LocalDate dataFineFermentazione;
			if(rs.getObject("data_fine_fermentazione")==null) dataFineFermentazione=null;
			else dataFineFermentazione=rs.getDate("data_fine_fermentazione").toLocalDate();
			double tempMinFermentazione;
			if(rs.getObject("temperatura_min_fermentazione")==null) tempMinFermentazione=Double.NaN;
			else tempMinFermentazione=rs.getDouble("temperatura_min_fermentazione");
			double tempMaxFermentazione;
			if(rs.getObject("temperatura_max_fermentazione")==null) tempMaxFermentazione=Double.NaN;
			else tempMaxFermentazione=rs.getDouble("temperatura_max_fermentazione");
			fer.setDataInizioFermentazione(dataInizioFermentazione);
			fer.setDataFineFermentazione(dataFineFermentazione);
			fer.setTemperaturaMinFermentazione(tempMinFermentazione);
			fer.setTemperaturaMaxFermentazione(tempMaxFermentazione);
			fer.setTravaso(rs.getBoolean("travaso"));
				//Aggiungere gittate
				sGittate=this.connessione.prepareStatement("SELECT num_gittata FROM gittata_fermentazione WHERE FK_id_cotta=?");
				sGittate.setInt(1, id);
				rsGittate=sGittate.executeQuery();
				while(rsGittate.next()){
					fer.addGittata((GittataFermentazione) this.getGittataFromId(rsGittate.getInt(1), "fermentazione", id));
				}
				
				//Aggiungere rilievi
				sRilievi=this.connessione.prepareStatement("SELECT num_rilievo, FK_id_cotta FROM rilievo_fermentazione WHERE FK_id_cotta=?");
				sRilievi.setInt(1, id);
				rsRilievi=sRilievi.executeQuery();
				while(rsRilievi.next()){
					fer.addRilievo(this.getRilievoFermentazioneFromId(rsRilievi.getInt(1), rsRilievi.getInt(2)));
				}
			if(numFase!=5) {fer.completa();}
			c.setFermentazione(fer);
			if(numFase>=6){
			
				//Aggiungo la rifermentazione
			LocalDate inizioRif;
			if(rs.getObject("data_inizio_rifermentazione")==null) inizioRif=null;
			else inizioRif=rs.getDate("data_inizio_rifermentazione").toLocalDate();
			LocalDate fineRif;
			if(rs.getObject("data_fine_rifermentazione")==null) fineRif=null;
			else fineRif=rs.getDate("data_fine_rifermentazione").toLocalDate();
			double tempRif;
			if(rs.getObject("temperatura_rifermentazione")==null) tempRif=Double.NaN;
			else tempRif=rs.getDouble("temperatura_rifermentazione");
			Rifermentazione rif=new Rifermentazione(inizioRif, fineRif, tempRif);
			if(numFase!=6) rif.completa();
			c.setRifermentazione(rif);
			if(numFase>=7){
			
				//Aggiungo la maturazione
			LocalDate dataInizioMat;
			if(rs.getObject("data_inizio_maturazione")==null) dataInizioMat=null;
			else dataInizioMat=rs.getDate("data_inizio_maturazione").toLocalDate();
			LocalDate dataFineMat;
			if(rs.getObject("data_fine_maturazione")==null) dataFineMat=null;
			else dataFineMat=rs.getDate("data_fine_maturazione").toLocalDate();
			double tempMat;
			if(rs.getObject("temperatura_maturazione")==null) tempMat=Double.NaN;
			else tempMat=rs.getDouble("temperatura_maturazione");
			Maturazione m=new Maturazione(dataInizioMat, dataFineMat, tempMat, rs.getBoolean("maturazione_in_botte"));
			if(numFase!=7) m.completa();
			c.setMaturazione(m);
			System.out.println(c.getMaturazione().getClass());
			}
			}
			}
			}
			}
			}
			}
			}
			return c;
		}else throw new dbException("Id non trovato");
	}
	
	/**
	 * Estrae dal database la gittata corrispondente all'id passato
	 * L'id è costituito dal numero della gittata, da una stringa contenente la fase della gittata e da un intero contenente l'id della cotta
	 * @param num_gittata
	 * @param fase
	 * @param idCotta
	 * @return
	 * @throws SQLException
	 * @throws beerException
	 * @throws dbException se l'id non viene trovato nel database
	 */
	public Gittata getGittataFromId(int num_gittata, String fase, int idCotta) throws SQLException, dbException, beerException{
		ResultSet rs;
		switch(fase.toLowerCase()){
		case "bollitura": 
			rs=this.getGittataBollituraById(num_gittata, idCotta);
			if(rs.next()){
				Time orario;
				orario=rs.getTime("orario");
				Gittata gb;
				if(orario!=null){
					gb=new GittataBollitura(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), orario.toLocalTime());
				}else gb=new GittataBollitura(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), null); 
				return gb;
			}
		case "raffreddamento":
			rs=this.getGittataRaffreddamentoById(num_gittata, idCotta); 
			if(rs.next()){
				Time orario=rs.getTime("orario");
				Gittata gr;
				if(orario!=null){
					gr=new GittataRaffreddamento(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), rs.getTime("orario").toLocalTime());
				}else gr=new GittataRaffreddamento(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), null);
				return gr;
			}
		case "fermentazione": 
			rs=this.getGittataFermentazioneById(num_gittata, idCotta); 
			if(rs.next()){
				Date data;
				data=rs.getDate("data");
				Gittata gf;
				if(data!=null) {
					gf=new GittataFermentazione(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), data.toLocalDate());
				}else gf=new GittataFermentazione(num_gittata, this.getIngredienteFromId(rs.getInt("FK_id_ingrediente")), rs.getDouble("quantità"), null);
				return gf;
			}
		default: throw new dbException("Id non trovato");
		}
	}
	/**
	 * Estrae dal database il giudizio corrispondente all'id passato
	 * L'id è un intero positivo
	 * @param id
	 * @return Un Giudizio corrispondente all'id passato come parametro
	 * @throws dbException se l'id non viene trovato sul database
	 * @throws SQLException
	 */
	public Giudizio getGiudizioFromId(int id) throws dbException, SQLException{
		ResultSet rs=this.getGiudizioById(id);
		if(rs.next()){
			Giudizio g=null;
			try {
				g = new Giudizio(
						rs.getString("esame_visivo"), rs.getInt("voto_visivo"),
						rs.getString("esame_olfattivo"), rs.getInt("voto_olfattivo"),
						rs.getString("esame_gustativo"), rs.getInt("voto_gustativo"),
						rs.getString("esame_sensazioni_boccali"), rs.getInt("voto_sensazioni_boccali"),
						rs.getString("opinione_generale"), rs.getInt("voto_generale"));
			} catch (beerException e1) {
				e1.printStackTrace();
				throw new dbException("Impossibile recuperare il giudizio richiesto");
			}
			try {
				g.setDifetti(rs.getBoolean("acetaldeide"), rs.getBoolean("alcoli_superiori"), 
						rs.getBoolean("astringenza"), rs.getBoolean("diacetile"), rs.getBoolean("dms"), 
						rs.getBoolean("esteri_fruttati"), rs.getBoolean("erba_tagliata"), rs.getBoolean("colpo_di_luce"), 
						rs.getBoolean("metallico"), rs.getBoolean("fenolico"), rs.getBoolean("solvente"), rs.getBoolean("muffa_stantio"), 
						rs.getBoolean("acido"), rs.getBoolean("sulfureo"), rs.getBoolean("vegetale"), rs.getBoolean("lievito"), 
						rs.getBoolean("ossidazione"), rs.getBoolean("autolisi"), rs.getString("altro"));
				g.setId(rs.getInt("id_giudizio"));
				if(!rs.getObject("nome_giudice").equals(null)) g.setNome_giudice(rs.getString("nome_giudice"));
				if(!rs.getObject("data").equals(null)) g.setData(rs.getDate("data"));
				//Inserire anche la città
			} catch (beerException e) {
				e.printStackTrace();
				throw new dbException("Impossibile recuperare il giudizio richiesto");
			}
			try {
				g.setNote(rs.getString("note"));
			} catch (beerException e) {
				e.printStackTrace();
			}
			return g;
		}else throw new dbException("Id non trovato");
	}
	/**
	 * Estrae dal database un ingrediente corrispondente all'id passato come parametro.
	 * L'oggetto restituito sarà un oggetto della classe più specifica possibile per l'ingrediente trovato
	 * @param id
	 * @return Un Ingrediente corrispondente all'id cercato
	 * @throws SQLException
	 * @throws beerException
	 * @throws dbException se l'id non è presente nel database
	 */
	public Ingrediente getIngredienteFromId(int id) throws SQLException, beerException, dbException{
		ResultSet rs=this.getIngredienteById(id);
		if(rs.next()){
			Ingrediente ing=new Ingrediente(rs.getString("nome"), rs.getString("tipo"), rs.getString("tipologia"), rs.getString("note"));
			ing.setId(rs.getInt("id_ingrediente"));
			switch(ing.getTipologia()){
				case "malto":
					int coloreEbc;
					if(rs.getObject("colore_ebc")==null) coloreEbc=-1;
					else coloreEbc=rs.getInt("colore_ebc");
					return ing.toMalto(coloreEbc);
				case "lievito": 
					String statoMateriale="";
					if(rs.getBoolean("liquido")) statoMateriale="liquido";
					else statoMateriale="solido";
					double qBusta;
					if(rs.getObject("quantità_busta")==null) qBusta=Double.NaN;
					else qBusta=rs.getDouble("quantità_busta");
					return ing.toLievito(statoMateriale, qBusta);
				case "luppolo": 
					double aa;
					if(rs.getObject("alfa_acidi")==null) aa=Double.NaN;
					else aa=rs.getDouble("alfa_acidi");
					return ing.toLuppolo(aa);
				default: return ing;
			}
		}else throw new dbException("Id non trovato");
	}

	/**
	 * Estrae dal database lo stile corrispondente all'id passato
	 * L'id è un intero positivo
	 * @param id
	 * @return Uno Stile corrispondente all'id passato
	 * @throws dbException se l'id non è presente nel database
	 * @throws SQLException
	 * @throws beerException
	 */
	public Stile getStileFromId(int id) throws dbException, SQLException, beerException{
		ResultSet rs=this.getStileById(id);
		if(rs.next()){
			Stile s=new Stile(rs.getString("nome"));
			s.setId(rs.getInt("id_stile"));
			s.setAroma(rs.getString("aroma"));
			s.setAspetto(rs.getString("aspetto"));
			s.setSapore(rs.getString("sapore"));
			s.setSapore(rs.getString("sensazioni_boccali"));
			s.setImpressioni_generali(rs.getString("impressioni_generali"));
			s.setCommenti(rs.getString("commenti"));
			s.setIngredienti(rs.getString("ingredienti"));
			s.setIBU(rs.getInt("IBU_min"), rs.getInt("IBU_max"));
			s.setSRM(rs.getInt("SRM_min"), rs.getInt("SRM_max"));
			s.setFG(rs.getInt("FG_min"), rs.getInt("FG_max"));
			s.setOG(rs.getInt("OG_min"), rs.getInt("OG_max"));
			s.setABV(rs.getFloat("ABV_min"), rs.getFloat("ABV_max"));
			s.setStoria(rs.getString("storia"));
			s.setCategoria(this.getCategoriaFromId(rs.getInt("FK_id_categoria")));
			return s;
		}else throw new dbException("Id non trovato");
	}
	/**
	 * Estrae dal database la ricetta corrispondente all'id passato
	 * L'id è un intero positivo
	 * @param id
	 * @return Una Ricetta corrispondente all'id passato
	 * @throws beerException
	 * @throws SQLException
	 * @throws dbException se l'id non è presente nel database
	 */
	public Ricetta getRicettaFromId(int id) throws beerException, SQLException, dbException{
		ResultSet rs=this.getRicettaById(id);
		if(rs.next()){
			Ricetta r=new Ricetta(rs.getString("nome"), rs.getString("note"), rs.getDate("data_creazione"));
			r.setId(rs.getInt("id_ricetta"));
			if(rs.getObject("FK_id_stile")!=null) r.setStile(this.getStileFromId(rs.getInt("FK_id_stile")));
			
			/*Aggiungo gli ingredienti != lievito alla lista*/
			PreparedStatement stmt=this.connessione.prepareStatement("SELECT FK_id_ingrediente FROM utilizzo_ricetta_ingrediente WHERE FK_id_ricetta=?");
			stmt.setInt(1, id);
			ResultSet ingredienti=stmt.executeQuery();
			while (ingredienti.next()){
				r.addIngrediente(this.getUtilizzoRicettaFromId(id, ingredienti.getInt(1)));
			}
			/*Aggiungo i lieviti alla lista*/
			stmt=this.connessione.prepareStatement("SELECT FK_tipo_lievito FROM utilizzo_ricetta_lievito WHERE FK_id_ricetta=?");
			stmt.setInt(1, id);
			ResultSet tipiLievito=stmt.executeQuery();
			while(tipiLievito.next()){
				r.addIngrediente(this.getUtilizzoRicettaFromId(id, this.fromTipoToId(tipiLievito.getString(1))));
			}
			return r;
		}else throw new dbException("Id non trovato");
	}
	
	/**
	 * Estrae dal database l'utilizzo corrispondente all'id passato.
	 * La ricerca dell'utilizzo avviene per tutte le tipologie di ingrediente. 
	 * Nel caso l'ingrediente sia di tipo Lievito, il metodo restituisce un'istanza di UtilizzoLievito
	 * @param idCotta
	 * @param idIngrediente
	 * @return Un Utilizzo contenente l'ingrediente utilizzato e la relativa quantità all'interno della ricetta specificata
	 * @throws dbException se l'id non è presente nel database
	 * @throws beerException
	 * @throws SQLException
	 */
	public Utilizzo getUtilizzoCottaFromId(int idCotta, int idIngrediente) throws SQLException, beerException, dbException{
		Ingrediente ing=this.getIngredienteFromId(idIngrediente);
		ResultSet rs;
		if(ing instanceof Lievito) {
			rs=this.getUtilizzoCottaLievitoById(idCotta, ing.getTipo());
		}
		else rs=this.getUtilizzoCottaIngredienteById(idCotta, idIngrediente);
		if(rs.next()){
			if(ing instanceof Lievito) {
				double nBuste, lStarter;
				int gStarter;
				if(rs.getObject("numero_buste")==null) nBuste=Double.NaN;
				else nBuste=rs.getDouble("numero_buste");
				if(rs.getObject("litri_starter")==null) lStarter=Double.NaN;
				else lStarter=rs.getDouble("litri_starter");
				if(rs.getObject("giorni_starter")!=null) gStarter=rs.getInt("giorni_starter");
				else gStarter=-1;
				UtilizzoLievito u=new UtilizzoLievito((Lievito)ing, nBuste);
				u.setLitri_starter(lStarter);
				u.setGiorni_starter(gStarter);
				return u;
			}else{
				double q;
				if(rs.getObject("quantità")!=null) q=rs.getDouble("quantità");
				else q=Double.NaN;
				Utilizzo u=new Utilizzo(ing, q);
				return u;
			}
		}else throw new dbException("Id non trovato");
	}

	/**
	 * Estrae dal database l'utilizzo corrispondente all'id passato.
	 * La ricerca dell'utilizzo avviene per tutte le tipologie di ingrediente. Nel caso l'ingrediente sia di tipo Lievito, il metodo restituisce un'istanza di UtilizzoLievito
	 * @param idRicetta
	 * @param idIngrediente
	 * @return Un Utilizzo contenente l'ingrediente utilizzato e la relativa quantità all'interno della ricetta specificata
	 * @throws dbException se l'id non è presente nel database
	 * @throws beerException
	 * @throws SQLException
	 */
	public Utilizzo getUtilizzoRicettaFromId(int idRicetta, int idIngrediente) throws dbException, beerException, SQLException{
		Ingrediente ing=this.getIngredienteFromId(idIngrediente);
		ResultSet rs;
		if(ing instanceof Lievito) {
			rs=this.getUtilizzoRicettaLievitoById(idRicetta, ing.getTipo());
		}
		else rs=this.getUtilizzoRicettaIngredienteById(idRicetta, idIngrediente);
		if(rs.next()){
			if(ing instanceof Lievito) {
				double nBuste, lStarter;
				int gStarter;
				if(rs.getObject("numero_buste")==null) nBuste=Double.NaN;
				else nBuste=rs.getDouble("numero_buste");
				if(rs.getObject("litri_starter")==null) lStarter=Double.NaN;
				else lStarter=rs.getDouble("litri_starter");
				if(rs.getObject("giorni_starter")!=null) gStarter=rs.getInt("giorni_starter");
				else gStarter=-1;
				UtilizzoLievito u=new UtilizzoLievito((Lievito)ing, nBuste);
				u.setLitri_starter(lStarter);
				u.setGiorni_starter(gStarter);
				return u;
			}else{
				double q;
				if(rs.getObject("quantità")!=null) q=rs.getDouble("quantità");
				else q=Double.NaN;
				Utilizzo u=new Utilizzo(ing, q);
				return u;
			}
		}else throw new dbException("Id non trovato");
	}
	
	/**
	 * Cerca all'interno del database la data d'inizio della cotta corrispondente all'id passato
	 * @param id
	 * @return Un LocalDate contenente la data trovata, null se l'id non è presente nel database
	 * @throws SQLException
	 */
	public LocalDate fromIdToDataCotta(int id) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("SELECT data FROM cotta WHERE id_cotta=?");
		stmt.setInt(1, id);
		ResultSet rs=stmt.executeQuery();
		if(rs.next()){
			if(rs.getObject(1)==null) return null;
			else return rs.getDate(1).toLocalDate();
		}else return null;
	}
	/**
	 * Estrae dal database il rilievo in fase di ammostamento corrispondente all'id specificato.
	 * L'id è composto da una stringa contenente la fase del rilievo e dall'id dell cotta
	 * @param numRilievo
	 * @param idCotta
	 * @return Un RilievoAmmostamento contenente il rilievo trovato. La data di inizio del rilievo non è settata
	 * @throws SQLException
	 * @throws dbException se l'id non è presente nel database
	 */
	public RilievoAmmostamento getRilievoAmmostamentoFromId(int numRilievo, int idCotta) throws SQLException, dbException{
		ResultSet rs=this.getRilievoAmmostamentoById(numRilievo, idCotta);
		if(rs.next()){
			RilievoAmmostamento r;
			try {
				double temp;
				if(rs.getObject("temperatura")==null) temp=Double.NaN;
				else temp=rs.getDouble("temperatura");
				double ph;
				if(rs.getObject("ph")==null) ph=Double.NaN;
				else ph=rs.getDouble("ph");
				LocalTime orarioI;
				if(rs.getObject("orario_inizio")==null) orarioI=null;
				else orarioI=rs.getTime("orario_inizio").toLocalTime();
				LocalTime orarioF;
				if(rs.getObject("orario_fine")==null) orarioF=null;
				else orarioF=rs.getTime("orario_fine").toLocalTime();
				r = new RilievoAmmostamento(numRilievo, temp, ph, orarioI, orarioF);
			} catch (beerException e) {
				e.printStackTrace();
				throw new dbException("Impossibile recuperare il rilievo richiesto");
			}
			return r;
		}else throw new dbException("Id non trovato");
	}
	/**
	 * Estrae dal database il rilievo infase di bollitura corrispondente all'id specificato.
	 * L'id è composto da una stringa contenente la fase del rilievo e dall'id dell cotta
	 * @param numRilievo
	 * @param idCotta
	 * @return Un RilievoBollitura contenente il rilievo trovato. La data di inizio del rilievo non è settata
	 * @throws SQLException
	 * @throws dbException se l'id non è presente nel database
	 */
	public RilievoBollitura getRilievoBollituraFromId(int numRilievo, int idCotta) throws SQLException, dbException{
		ResultSet rs=this.getRilievoBollituraById(numRilievo, idCotta);
		if(rs.next()){
			RilievoBollitura r;
			try {
				double temp;
				if(rs.getObject("temperatura")==null) temp=Double.NaN;
				else temp=rs.getDouble("temperatura");
				double ph;
				if(rs.getObject("ph")==null) ph=Double.NaN;
				else ph=rs.getDouble("ph");
				LocalTime orarioI;
				if(rs.getObject("orario_inizio")==null) orarioI=null;
				else orarioI=rs.getTime("orario_inizio").toLocalTime();
				LocalTime orarioF;
				if(rs.getObject("orario_fine")==null) orarioF=null;
				else orarioF=rs.getTime("orario_fine").toLocalTime();
				r=new RilievoBollitura(numRilievo, temp, ph, orarioI, orarioF);
			} catch (beerException e) {
				e.printStackTrace();
				throw new dbException("Impossibile recuperare il rilievo richiesto");
			}
			return r;
		}else throw new dbException("Id non trovato");
	}
	/**
	 * Estrae dal database il rilievo in fase di fermentazione corrispondente all'id specificato.
	 * L'id è composto da una stringa contenente la fase del rilievo e dall'id dell cotta
	 * @param numRilievo
	 * @param idCotta
	 * @return Un RilievoFermentazione contenente il rilievo trovato.
	 * @throws SQLException
	 * @throws dbException se l'id non è presente nel database
	 */
	public RilievoFermentazione getRilievoFermentazioneFromId(int numRilievo, int idCotta) throws SQLException, dbException{
		ResultSet rs=this.getRilievoFermentazioneById(numRilievo, idCotta);
		if (rs.next()){
			RilievoFermentazione r;
			try {
				double temp;
				if(rs.getObject("temperatura")==null) temp=Double.NaN;
				else temp=rs.getDouble("temperatura");
				double ph;
				if(rs.getObject("ph")==null) ph=Double.NaN;
				else ph=rs.getDouble("ph");
				LocalDate dataI;
				if(rs.getObject("data_inizio")==null) dataI=null;
				else dataI=rs.getDate("data_inizio").toLocalDate();
				LocalDate dataF;
				if(rs.getObject("data_fine")==null) dataF=null;
				else dataF=rs.getDate("data_fine").toLocalDate();
				r=new RilievoFermentazione(numRilievo, temp, ph, dataI, dataF);
			} catch (beerException e) {
				e.printStackTrace();
				throw new dbException("Impossibile recuperare il rilievo richiesto");
			}
			return r;
		}else throw new dbException("Id non trovato");
	}
	/**
	 * Estrae dal database il rilievo in fase di filtraggio corrispondente all'id specificato.
	 * L'id è composto da una stringa contenente la fase del rilievo e dall'id dell cotta
	 * @param numRilievo
	 * @param idCotta
	 * @return Un RilievoFiltraggio contenente il rilievo trovato. La data di inizio del rilievo non è settata
	 * @throws SQLException
	 * @throws dbException se l'id non è presente nel database
	 */
	public RilievoFiltraggio getRilievoFiltraggioFromId(int numRilievo, int idCotta) throws SQLException, dbException{
		ResultSet rs=this.getRilievoFiltraggioById(numRilievo, idCotta);
		if(rs.next()){
			RilievoFiltraggio r;
			try {
				double temp;
				if(rs.getObject("temperatura")==null) temp=Double.NaN;
				else temp=rs.getDouble("temperatura");
				double ph;
				if(rs.getObject("ph")==null) ph=Double.NaN;
				else ph=rs.getDouble("ph");
				LocalTime orarioI;
				if(rs.getObject("orario_inizio")==null) orarioI=null;
				else orarioI=rs.getTime("orario_inizio").toLocalTime();
				LocalTime orarioF;
				if(rs.getObject("orario_fine")==null) orarioF=null;
				else orarioF=rs.getTime("orario_fine").toLocalTime();
				r=new RilievoFiltraggio(numRilievo, temp, ph, orarioI, orarioF);
			} catch (beerException e) {
				e.printStackTrace();
				throw new dbException("Impossibile recuperare il rilievo richiesto");
			}
			return r;
		}else throw new dbException("Id non trovato");
	}
	
	/*Gestione analisi*/
	private int insertAnalisiFermentazione(int idCotta, double litri, double gradoAlc, double gradoAm, double dens, double plato, double brix, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO analisi(fase_analisi, FK_id_cotta, litri, grado_alcolico, grado_amaro, densità, gradi_plato, gradi_brix, note)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, "fermentazione");
			stmt.setInt(2, idCotta);
			if(!Double.valueOf(litri).equals(Double.NaN))stmt.setDouble(3, litri);
			else stmt.setNull(3, java.sql.Types.INTEGER);
			if(!Double.valueOf(gradoAlc).equals(Double.NaN)) stmt.setDouble(4, gradoAlc);
			else stmt.setNull(4, java.sql.Types.INTEGER);
			if(!Double.valueOf(gradoAm).equals(Double.NaN))	stmt.setDouble(5, gradoAm);
			else stmt.setNull(5, java.sql.Types.FLOAT);
			if(!Double.valueOf(dens).equals(Double.NaN))stmt.setDouble(6, dens);
			else stmt.setNull(6, java.sql.Types.FLOAT);
			if(!Double.valueOf(plato).equals(Double.NaN))stmt.setDouble(7, plato);
			else stmt.setNull(7, java.sql.Types.FLOAT);
			if(!Double.valueOf(brix).equals(Double.NaN))stmt.setDouble(8, brix);
			else stmt.setNull(8, java.sql.Types.FLOAT);
			if(!note.equals("")) stmt.setString(9, note);
			else stmt.setNull(9, java.sql.Types.VARCHAR);
			stmt.execute();
			int i=stmt.getUpdateCount();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	private int insertAnalisi(String fase, int idCotta, double litri, double dens, double plato, double brix, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO analisi(fase_analisi, FK_id_cotta, litri, densità, gradi_plato, gradi_brix, note)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, fase);
			stmt.setInt(2, idCotta);
			if(!Double.valueOf(litri).equals(Double.NaN))stmt.setDouble(3, litri);
			else stmt.setNull(3, java.sql.Types.INTEGER);
			if(!Double.valueOf(dens).equals(Double.NaN))stmt.setDouble(4, dens);
			else stmt.setNull(4, java.sql.Types.FLOAT);
			if(!Double.valueOf(plato).equals(Double.NaN))stmt.setDouble(5, plato);
			else stmt.setNull(5, java.sql.Types.FLOAT);
			if(!Double.valueOf(brix).equals(Double.NaN))stmt.setDouble(6, brix);
			else stmt.setNull(6, java.sql.Types.FLOAT);
			if(!note.equals("")) stmt.setString(7, note);
			else stmt.setNull(7, java.sql.Types.VARCHAR);
			stmt.execute();
			int i=stmt.getUpdateCount();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	public int insertAnalisi(Analisi a){
		if(a instanceof AnalisiFermentazione) return this.insertAnalisiFermentazione(a.getIdCotta(), a.getLitri(), ((AnalisiFermentazione) a).getGrado_alcolico(), ((AnalisiFermentazione) a).getGrado_amaro(), a.getDensità(), a.getGradi_plato(), a.getGradi_brix(), a.getNote());
		else return this.insertAnalisi(a.getFase(), a.getIdCotta(), a.getLitri(), a.getDensità(), a.getGradi_plato(), a.getGradi_brix(), a.getNote());
	}
	public boolean updateAnalisi(String fase, int idCotta, double litri, double gradoAlc, double gradoAm, double dens, double plato, double brix, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE analisi SET litri=?, grado_alcolico=?, grado_amaro=?, densità=?, gradi_plato=?, gradi_brix=?, note=? WHERE fase_analisi=? AND FK_id_cotta=?");
			if(!Double.valueOf(litri).equals(Double.NaN)) stmt.setDouble(1, litri);
			else stmt.setNull(1, java.sql.Types.FLOAT);
			if(!Double.valueOf(gradoAlc).equals(Double.NaN))stmt.setDouble(2, gradoAlc);
			else stmt.setNull(2, java.sql.Types.FLOAT);
			if(!Double.valueOf(gradoAm).equals(Double.NaN)) stmt.setDouble(3, gradoAm);
			else stmt.setNull(3, java.sql.Types.FLOAT);
			if(!Double.valueOf(dens).equals(Double.NaN))stmt.setDouble(4, dens);
			else stmt.setNull(4, java.sql.Types.FLOAT);
			if(!Double.valueOf(plato).equals(Double.NaN)) stmt.setDouble(5, plato);
			else stmt.setNull(5, java.sql.Types.FLOAT);
			if (!Double.valueOf(brix).equals(Double.NaN)) stmt.setDouble(6, brix);
			else stmt.setNull(6, java.sql.Types.FLOAT);
			if(!note.equals("")) stmt.setString(7, note);
			else stmt.setNull(7, java.sql.Types.VARCHAR);
			stmt.setString(8, fase);
			stmt.setInt(9, idCotta);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteAnalisi(int idCotta, String fase){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM analisi WHERE fase_analisi=? AND FK_id_cotta=?");
			stmt.setString(1, fase);
			stmt.setInt(2, idCotta);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*Gestione categoria*/
	private int insertCategoria(String nome, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO categoria (nome, note) VALUES (?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			if(!nome.equals("")) stmt.setString(1, nome);
			else stmt.setNull(1, java.sql.Types.VARCHAR);
			if(note.equals("")) stmt.setNull(2, java.sql.Types.VARCHAR);
			else stmt.setString(2, note);
			stmt.execute();
			ResultSet rs=stmt.getGeneratedKeys();
			if(rs.next()) return rs.getInt(1);
			else return -1;
		}catch (SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	public boolean deleteCategoria(int id){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM categoria WHERE id_categoria=?");
			stmt.setInt(1, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}
	public boolean updateCategoria(int id, String nome, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE categoria SET nome=?, note=? WHERE id_categoria=? ");
			if(!nome.equals("")) stmt.setString(1, nome);
			else stmt.setNull(1, java.sql.Types.VARCHAR);
			if(note.equals("")) stmt.setNull(2, java.sql.Types.VARCHAR);
			else stmt.setString(2, note);
			stmt.setInt(3, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public int insertCategoria(Categoria c){
		return this.insertCategoria(c.getNome(), c.getNote());
	}
	
	/*Gestione cotta*/
	public int modifyAmmostamento(int id, Ammostamento a) throws SQLException{
		if(a==null) a=new Ammostamento();
		for(int i=0; i<a.getRilievi().size(); i++){
			this.insertRilievo(a.getRilievi().get(i), a.getRilievi().get(i).getNumRilievo(), id);
		}
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta SET litri_acqua_ammostamento=? WHERE id_cotta=?");
		if(!Double.valueOf(a.getLitriAcqua()).isNaN()) stmt.setDouble(1, a.getLitriAcqua());
		else stmt.setNull(1, java.sql.Types.FLOAT);
		stmt.setInt(2, id);
		return stmt.executeUpdate();
	}
	public int modifyFiltraggio(int id, Filtraggio f) throws SQLException{
		if(f==null) f=new Filtraggio();
		for(int i=0; i<f.getRilievi().size(); i++){
			this.insertRilievo(f.getRilievi().get(i), f.getRilievi().get(i).getNumRilievo(), id);
		}
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta SET litri_acqua_filtraggio=? WHERE id_cotta=?");
		if(!Double.valueOf(f.getLitriAcqua()).isNaN()) stmt.setDouble(1, f.getLitriAcqua());
		else stmt.setNull(1, java.sql.Types.FLOAT);
		stmt.setInt(2, id);
		return stmt.executeUpdate();
	}
	public int modifyBollitura(int id, Bollitura b) throws SQLException{
		if(b==null) b=new Bollitura();
		//Inserire rilievi
		for(int i=0; i<b.getRilievi().size(); i++){
			this.insertRilievo(b.getRilievi().get(i), b.getRilievi().get(i).getNumRilievo(), id);
		}
		//Inserire gittate
		for(int i=0; i<b.getGittate().size(); i++){
			GittataBollitura git=b.getGittate().get(i);
			if(git.getIngrediente().getTipologia().equals("lievito"))	this.insertGittataBollitura(git.getNum_gittata(), id, git.getIngrediente().getId() , git.getOrario(), git.getNumBuste());
			else this.insertGittataBollitura(git.getNum_gittata(), id, git.getIngrediente().getId() , git.getOrario(), git.getQuantità());
		}
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET orario_inizio_riscaldamento=?, orario_inizio_bollitura=? "
				+ "WHERE id_cotta=?");
		if(b.getOrarioInizioRiscaldamento()!=null) stmt.setTimestamp(1, Timestamp.valueOf(b.getOrarioInizioRiscaldamento()));
		else stmt.setNull(1, java.sql.Types.FLOAT);
		if(b.getOrarioInizioBollitura()!=null) stmt.setTimestamp(2, Timestamp.valueOf(b.getOrarioInizioBollitura()));
		else stmt.setNull(2, java.sql.Types.FLOAT);
		stmt.setInt(3, id);
		return stmt.executeUpdate();
	}
	public int modifyRaffreddamento(int id, Raffreddamento raf) throws SQLException{
		if(raf==null) raf=new Raffreddamento();
		for(int i=0; i<raf.getGittate().size(); i++){
			GittataRaffreddamento git=raf.getGittate().get(i);
			if(git.getIngrediente() instanceof Lievito)	this.insertGittataRaffreddamento(git.getNum_gittata(), id, 
					git.getIngrediente().getId(), 
					git.getOrario(), 
					git.getNumBuste());
			else this.insertGittataRaffreddamento(git.getNum_gittata(), id, 
					git.getIngrediente().getId(), 
					git.getOrario(), 
					git.getQuantità());
		}
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET orario_inizio_raffreddamento=?, orario_fine_raffreddamento=?, "
				+ "temperatura_fine_raffreddamento=?, "
				+ "orario_inizio_whirlpool=?, orario_fine_whirlpool=? WHERE id_cotta=?");
		if(raf.getOrarioInizioRaffreddamento()!=null) stmt.setTimestamp(1, Timestamp.valueOf(raf.getOrarioInizioRaffreddamento()));
		else stmt.setNull(1, java.sql.Types.TIMESTAMP);
		if(raf.getOrarioFineRaffreddamento()!=null) stmt.setTimestamp(2, Timestamp.valueOf(raf.getOrarioInizioRaffreddamento()));
		else stmt.setNull(2, java.sql.Types.TIMESTAMP);
		if(!Double.valueOf(raf.getTemperaturaFineRaffreddamento()).isNaN()) stmt.setDouble(3, raf.getTemperaturaFineRaffreddamento());  
		else stmt.setNull(3, java.sql.Types.FLOAT);
		if(raf.getOrarioInizioWhirlpool()!=null) stmt.setTimestamp(4, Timestamp.valueOf(raf.getOrarioInizioWhirlpool()));
		else stmt.setNull(4, java.sql.Types.TIMESTAMP);
		if(raf.getOrarioFineWhirlpool()!=null) stmt.setTimestamp(5, Timestamp.valueOf(raf.getOrarioFineWhirlpool()));
		else stmt.setNull(5, java.sql.Types.TIMESTAMP);
		stmt.setInt(6, id);
		return stmt.executeUpdate();
	}
	public int modifyFermentazione(int id, Fermentazione fer) throws SQLException{
		if(fer==null) fer=new Fermentazione();
		for(int i=0; i<fer.getGittate().size(); i++){
			GittataFermentazione git=fer.getGittate().get(i);
			if(git.getIngrediente() instanceof Lievito) this.insertGittataFermentazione(git.getNum_gittata(), id, 
					git.getIngrediente().getId(), 
					git.getData(), 
					git.getNumBuste());
			else this.insertGittataFermentazione(git.getNum_gittata(), id, 
					git.getIngrediente().getId(), 
					git.getData(), 
					git.getQuantità());
		}
		for(int i=0; i<fer.getRilievi().size(); i++){
			this.insertRilievo(fer.getRilievi().get(i), fer.getRilievi().get(i).getNumRilievo(), id);
		}
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET temperatura_min_fermentazione=?, "
				+ "temperatura_max_fermentazione=?, "
				+ "data_inizio_fermentazione=?, "
				+ "data_fine_fermentazione=?, "
				+ "travaso=? "
				+ "WHERE id_cotta=?");
		if(!Double.valueOf(fer.getTemperaturaMinFermentazione()).isNaN()) stmt.setDouble(1, fer.getTemperaturaMinFermentazione());
		else stmt.setNull(1, java.sql.Types.FLOAT);
		if(!Double.valueOf(fer.getTemperaturaMaxFermentazione()).isNaN()) stmt.setDouble(2, fer.getTemperaturaMaxFermentazione());
		else stmt.setNull(2, java.sql.Types.FLOAT);
		if(fer.getDataInizioFermentazione()!=null) stmt.setDate(3, Date.valueOf(fer.getDataInizioFermentazione()));
		else stmt.setNull(3, java.sql.Types.DATE);
		if(fer.getDataFineFermentazione()!=null) stmt.setDate(4, Date.valueOf(fer.getDataFineFermentazione()));
		else stmt.setNull(4, java.sql.Types.DATE);
		switch(fer.isTravaso()){
		case 0: stmt.setBoolean(5, Boolean.FALSE); break;
		case 1: stmt.setBoolean(5, Boolean.TRUE); break;
		case -1: stmt.setNull(5, java.sql.Types.BIT); break;
		}
		stmt.setInt(6, id);
		return stmt.executeUpdate();
	}
	public int modifyRifermentazione(int id, Rifermentazione rif) throws SQLException{
		if (rif==null) rif=new Rifermentazione();
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET data_inizio_rifermentazione=?, "
				+ "data_fine_rifermentazione=?, "
				+ "temperatura_rifermentazione=? "
				+ "WHERE id_cotta=?");
		if(rif.getDataInizioRifermentazione()!=null) stmt.setDate(1, Date.valueOf(rif.getDataInizioRifermentazione()));
		else stmt.setNull(1, java.sql.Types.DATE);
		if(rif.getDataFineRifermentazione()!=null) stmt.setDate(2, Date.valueOf(rif.getDataFineRifermentazione()));
		else stmt.setNull(2, java.sql.Types.DATE);
		if(!Double.valueOf(rif.getTemperaturaRifermentazione()).equals(Double.NaN)) stmt.setDouble(3, rif.getTemperaturaRifermentazione());
		else stmt.setNull(3, java.sql.Types.FLOAT);
		stmt.setInt(4, id);
		return stmt.executeUpdate();
	}
	public int modifyMaturazione(int id, Maturazione mat) throws SQLException{
		if(mat==null) mat=new Maturazione();
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET data_inizio_maturazione=?, "
				+ "data_fine_maturazione=?, "
				+ "temperatura_maturazione=?, "
				+ "maturazione_in_botte=? "
				+ "WHERE id_cotta=?");
		System.out.println(mat.getClass());
		mat.setBotte(true);
		if(!(mat.getDataInizioMaturazione()==null)) stmt.setDate(1, Date.valueOf(mat.getDataInizioMaturazione()));
		else stmt.setNull(1, java.sql.Types.DATE);
		if(!(mat.getDataFineMaturazione()==null))	stmt.setDate(2, Date.valueOf(mat.getDataFineMaturazione()));
		else stmt.setNull(2, java.sql.Types.DATE);
		if(!Double.valueOf(mat.getTemperaturaMaturazione()).isNaN()) stmt.setDouble(3, mat.getTemperaturaMaturazione());
		else stmt.setNull(3, java.sql.Types.FLOAT);
		switch(mat.isBotte()){
		case 0: stmt.setBoolean(4, Boolean.FALSE); break;
		case 1: stmt.setBoolean(4, Boolean.TRUE); break;
		case -1: stmt.setNull(4, java.sql.Types.BIT); break;
		}
		stmt.setInt(5, id);
		return stmt.executeUpdate();
	}
	public int insertCotta(Cotta c) throws SQLException, dbException{
		//Inserire dati cotta
		int idCotta=0;
		int numFase=Cotta.numFase(c.getFaseCorrente());
		PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO cotta (data, luogo, "
				+ "quantità_acqua, luogo_acqua, sali_minerali, note, FK_id_ricetta, fase) VALUES(?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setDate(1, Date.valueOf(c.getData()));
		stmt.setString(2, c.getLuogo());
		if(c.getQuantitàAcqua()!=Double.NaN) stmt.setDouble(3, c.getQuantitàAcqua());
		else stmt.setNull(3, java.sql.Types.FLOAT);
		if(!c.getLuogoAcqua().equals("")) stmt.setString(4, c.getLuogoAcqua());
		else stmt.setNull(4, java.sql.Types.VARCHAR);
		if(c.getSaliMinerali()!=Double.NaN) stmt.setDouble(5, c.getSaliMinerali());
		else stmt.setNull(5, java.sql.Types.FLOAT);
		if((c.getNote()!=null)&&(!c.getNote().equals(""))) stmt.setString(6, c.getNote());
		else stmt.setNull(6, java.sql.Types.VARCHAR);
		if(c.getRicetta()!=null) stmt.setInt(7, c.getRicetta().getId());
		else stmt.setNull(7, java.sql.Types.INTEGER);
		stmt.setString(8, c.getFaseCorrente().toLowerCase());
		if(stmt.executeUpdate()==0) throw new dbException("Errore nell'inserimento della cotta");
		ResultSet rs=stmt.getGeneratedKeys();
		if(rs.next())idCotta=rs.getInt(1);
		else throw new dbException("Errore nell'inserimento della cotta");
		//inserire ammostamento
		if(numFase>=1)	this.modifyAmmostamento(idCotta, c.getAmmostamento());
		//inserire filtraggio
		if(numFase>=2)	this.modifyFiltraggio(idCotta, c.getFiltraggio());
		//inserire bollitura
		if(numFase>=3)	this.modifyBollitura(idCotta, c.getBollitura());
		//inserire raffreddamento
		if(numFase>=4)	this.modifyRaffreddamento(idCotta, c.getRaffreddamento());
		//inserire fermentazione
		if(numFase>=5)	this.modifyFermentazione(idCotta, c.getFermentazione());
		//inserire rifermentazione
		if(numFase>=6)	this.modifyRifermentazione(idCotta, c.getRifermentazione());
		//inserire maturazione
		if(numFase>=7)	this.modifyMaturazione(idCotta, c.getMaturazione());
		//inserire fase
		stmt=this.connessione.prepareStatement("UPDATE cotta SET fase=? WHERE id_cotta=?");

		stmt.setString(1, c.getFaseCorrente().toLowerCase());
		stmt.setInt(2, idCotta);
		stmt.executeUpdate();
		//inserire ingredienti (utilizzi)
		for(int i=0; i<c.getIngredienti().size(); i++){
			this.insertUtilizzoCotta(c.getIngredienti().get(i), idCotta);
		}
		//inserire analisi
		for(int i=0; i<c.getAnalisi().size(); i++){
			this.insertAnalisi(c.getAnalisi().get(i));
		}
		//inserire giudizi
		for(int i=0; i<c.getGiudizi().size(); i++){
			this.insertGiudizio(c.getGiudizi().get(i), idCotta);
		}
		return idCotta;
	}
	public int deleteCotta(int id) throws SQLException{
		if(id>0){
			//Elimino gittate della cotta
			this.deleteGittateCotta(id);
			this.deleteUtilizziCotta(id);
			this.deleteGiudiziCotta(id);
			this.deleteAnalisi(id, "filtraggio");
			this.deleteAnalisi(id, "raffreddamento");
			this.deleteAnalisi(id, "fermentazione");
			this.deleteRilieviCotta(id);
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM cotta WHERE id_cotta=?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
			return stmt.getUpdateCount();
			
		}else return 0;
		
	}
	public int updateDatiCotta(int id, Ricetta r, LocalDate data, String luogo, double qAcqua, String lAcqua, double saliMinerali, String note, String fase) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET data=?, "
				+ "luogo=?, "
				+ "FK_id_ricetta=?, "
				+ "quantità_acqua=?, "
				+ "luogo_acqua=?, "
				+ "sali_minerali=?, "
				+ "note=?, "
				+ "fase=? "
				+ "WHERE id_cotta=?");
		if(data!=null) stmt.setDate(1, Date.valueOf(data));
		else stmt.setNull(1, java.sql.Types.DATE);
		if((luogo==null)||(luogo.equals(""))) stmt.setNull(2, java.sql.Types.VARCHAR);
		else stmt.setString(2, luogo);
		stmt.setInt(3, r.getId());
		if(qAcqua!=Double.NaN) stmt.setDouble(4, qAcqua);
		else stmt.setNull(4, java.sql.Types.FLOAT);
		if((lAcqua.equals(""))||(lAcqua==null)) stmt.setNull(5, java.sql.Types.VARCHAR);
		else stmt.setString(5, lAcqua);
		if(saliMinerali!=Double.NaN) stmt.setDouble(6, saliMinerali);
		else stmt.setNull(6, java.sql.Types.FLOAT);
		if((note==null||(note.equals("")))) stmt.setNull(7, java.sql.Types.VARCHAR);
		else stmt.setString(7, note);
		stmt.setString(8, fase);
		stmt.setInt(9, id);
		return stmt.executeUpdate();
	}
	public int setEfficienza(int id, double eff) throws SQLException{
		if((Double.valueOf(eff)==Double.NaN)||(eff<0)) return 0;
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET efficienza=? "
				+ "WHERE id_cotta=?");
		stmt.setDouble(1, eff);
		stmt.setInt(2, id);
		stmt.executeUpdate();
		return stmt.getUpdateCount();
	}
	public int updateAmmostamentoCotta(int id, double litri) throws SQLException, beerException{
		if(Double.valueOf(litri).isNaN()||(litri<0)) throw new beerException("Impossibile modificare la cotta. Il numero di litri deve essere positivo");
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET litri_acqua_ammostamento=? "
				+ "WHERE id_cotta=?");
		stmt.setDouble(1, litri);
		stmt.setInt(2, id);
		return stmt.executeUpdate();
	}
	public int updateFiltraggioCotta(int id, double litri) throws beerException, SQLException{
		if(Double.valueOf(litri).isNaN()||(litri<0)) throw new beerException("Impossibile modificare la cotta. Il numero di litri deve essere positivo");
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET litri_acqua_filtraggio=? "
				+ "WHERE id_cotta=?");
		stmt.setDouble(1, litri);
		stmt.setInt(2, id);
		return stmt.executeUpdate();
	}
	public int updateBollituraCotta(int id, LocalDateTime inizioRis, LocalDateTime inizioBol) throws SQLException, beerException{
		if((inizioRis==null)||(inizioBol==null)||(inizioBol.isAfter(inizioRis))){
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
					+ "SET orario_inizio_riscaldamento=?, "
					+ "orario_inizio_bollitura=? "
					+ "WHERE id_cotta=?");
			if(inizioRis!=null) stmt.setTimestamp(1, Timestamp.valueOf(inizioRis));
			else stmt.setNull(1, java.sql.Types.TIMESTAMP);
			if(inizioBol!=null) stmt.setTimestamp(2, Timestamp.valueOf(inizioBol));
			else stmt.setNull(2, java.sql.Types.TIMESTAMP);
			stmt.setInt(3, id);
			return stmt.executeUpdate();
		}else throw new beerException("Impossibile modificare la cotta. Impostare correttamente gli istanti di riscaldamento e bollitura");
	}
	public int updateRaffreddamentoCotta(int id, LocalDateTime inizioR, LocalDateTime fineR, double temp, LocalDateTime inizioW, LocalDateTime fineW) throws SQLException, beerException{
		if((inizioR!=null)&&(fineR!=null)&&(inizioR.isAfter(fineR))) throw new beerException("Impossibile modificare la cotta. Controllare gli istanti di inizio e fine raffreddamento");
		if((inizioW!=null)&&(fineW!=null)&&(inizioW.isAfter(fineW))) throw new beerException("Impossibile modificare la cotta. Controllare gli istanti di inizio e fine whirlpool");
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET orario_inizio_raffreddamento=?, "
				+ "orario_fine_raffreddamento=?, "
				+ "temperatura_fine_raffreddamento=?, "
				+ "orario_inizio_whirlpool=?, "
				+ "orario_fine_whirlpool=? "
				+ "WHERE id_cotta=?");
		if(inizioR!=null)	stmt.setTimestamp(1, Timestamp.valueOf(inizioR));
		else stmt.setNull(1, java.sql.Types.TIMESTAMP);
		if(fineR!=null)	stmt.setTimestamp(2, Timestamp.valueOf(fineR));
		else stmt.setNull(2, java.sql.Types.TIMESTAMP);
		if(!Double.valueOf(temp).isNaN())	stmt.setDouble(3, temp);
		else stmt.setNull(3, java.sql.Types.FLOAT);
		if(inizioW!=null)	stmt.setTimestamp(4, Timestamp.valueOf(inizioW));
		else stmt.setNull(4, java.sql.Types.TIMESTAMP);
		if(fineW!=null)	stmt.setTimestamp(5, Timestamp.valueOf(fineW));
		else stmt.setNull(5, java.sql.Types.TIMESTAMP);
		stmt.setInt(6, id);
		return stmt.executeUpdate();
	}
	public int updateFermentazioneCotta(int id, double tempMin, double tempMax, LocalDate inizio, LocalDate fine, int travaso) throws SQLException, beerException{
		if((!Double.valueOf(tempMin).isNaN())&&(!Double.valueOf(tempMax).isNaN())&&(tempMin>tempMax)) throw new beerException("Impossibile modificare la cotta. Controllare le temperatura minima e massima");
		if((inizio!=null)&&(fine!=null)&&(inizio.isAfter(fine))) throw new beerException("Impossibile modificare la cotta. Controllare gli istanti di inizio e fine fermentazione");
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET temperatura_min_fermentazione=?, "
				+ "temperatura_max_fermentazione=?, "
				+ "travaso=?, "
				+ "data_inizio_fermentazione=?, "
				+ "data_fine_fermentazione=? "
				+ "WHERE id_cotta=?");
		if(!Double.valueOf(tempMin).isNaN())	stmt.setDouble(1, tempMin);
		else stmt.setNull(1, java.sql.Types.FLOAT);
		if(!Double.valueOf(tempMax).isNaN())	stmt.setDouble(2, tempMax);
		else stmt.setNull(2, java.sql.Types.FLOAT);
		switch(travaso){
			case 1: stmt.setBoolean(3, Boolean.TRUE); break;
			case 0: stmt.setBoolean(3, Boolean.FALSE); break;
			case -1: stmt.setNull(3, java.sql.Types.BOOLEAN);
			default: stmt.setNull(3, java.sql.Types.BOOLEAN);
		}
		if(inizio!=null) stmt.setDate(4, Date.valueOf(inizio));
		else stmt.setNull(4, java.sql.Types.DATE);
		if(fine!=null) stmt.setDate(5, Date.valueOf(fine));
		else stmt.setNull(5, java.sql.Types.DATE);
		stmt.setInt(6, id);
		return stmt.executeUpdate();
	}
	public int updateRifermentazioneCotta(int id, LocalDate inizio, LocalDate fine, double temp) throws SQLException, beerException{
		if((inizio==null)||(fine==null)||(!inizio.isAfter(fine))){
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
					+ "SET data_inizio_rifermentazione=?, "
					+ "data_fine_rifermentazione=?, "
					+ "temperatura_rifermentazione=? "
					+ "WHERE id_cotta=?");
			if(inizio!=null)	stmt.setDate(1, Date.valueOf(inizio));
			else stmt.setNull(1, java.sql.Types.DATE);
			if(fine!=null)	stmt.setDate(2, Date.valueOf(fine));
			else stmt.setNull(2, java.sql.Types.DATE);
			if(!Double.valueOf(temp).isNaN())	stmt.setDouble(3, temp);
			else stmt.setNull(3, java.sql.Types.FLOAT);
			stmt.setInt(4, id);
			return stmt.executeUpdate();
		}else throw new beerException("Impossibile modificare la cotta. Controllare le date di inizio e fine rifermentazione");
		
	}
	public int updateMaturazioneCotta(int id, LocalDate inizio, LocalDate fine, double temp, int botte) throws SQLException, beerException{
		if((inizio!=null)&&(fine!=null)&&(inizio.isAfter(fine))) throw new beerException("Impossibile modificare la cotta. Controllare gli istanti di inizio e fine maturazione");
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE cotta "
				+ "SET temperatura_maturazione=?, "
				+ "data_inizio_maturazione=?, "
				+ "data_fine_maturazione=?, "
				+ "maturazione_in_botte=? "
				+ "WHERE id_cotta=?");
		if(Double.valueOf(temp).isNaN()) stmt.setNull(1, java.sql.Types.FLOAT);
		else stmt.setDouble(1, temp);
		if(inizio==null)	stmt.setNull(2, java.sql.Types.DATE);
		else stmt.setDate(2, Date.valueOf(inizio));
		if(fine==null)	stmt.setNull(3, java.sql.Types.DATE);
		else stmt.setDate(3, Date.valueOf(fine));
		switch(botte){
			case 0: stmt.setBoolean(4, Boolean.FALSE); break;
			case 1: stmt.setBoolean(4, Boolean.TRUE); break;
			case -1: stmt.setNull(4, java.sql.Types.BIT);
			default: stmt.setNull(4, java.sql.Types.BIT);
		}
		stmt.setInt(5, id);
		return stmt.executeUpdate();
	}

	
	/*Gestione gittate*/
	public int insertGittataBollitura(int num, int idCotta, int idIng, LocalTime orario, double quantità){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO gittata_bollitura"
					+ "(num_gittata, FK_id_cotta, FK_id_ingrediente, orario, quantità) "
					+ "VALUES(?, ?, ?, ?, ?)");
			if((num>0)&&(idCotta>0)&&(idIng>0)){
				stmt.setInt(1, num);
				stmt.setInt(2, idCotta);
				stmt.setInt(3, idIng);
			}else return -1;
			if(!(orario==null)) stmt.setTime(4, Time.valueOf(orario));
			else stmt.setNull(4, java.sql.Types.TIME);
			if(!Double.valueOf(quantità).equals(Double.NaN))stmt.setDouble(5, quantità);
			else stmt.setNull(5, java.sql.Types.VARCHAR);
			stmt.execute();
			int i=stmt.getUpdateCount();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	public int insertGittataFermentazione(int num, int idCotta, int idIng, LocalDate data, double quantità){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO gittata_fermentazione"
					+ "(num_gittata, FK_id_cotta, FK_id_ingrediente, data, quantità) "
					+ "VALUES(?, ?, ?, ?, ?)");
			if((num>0)&&(idCotta>0)&&(idIng>0)){
				stmt.setInt(1, num);
				stmt.setInt(2, idCotta);
				stmt.setInt(3, idIng);
			}else return -1;
			if(!(data==null)) stmt.setDate(4, Date.valueOf(data));
			else stmt.setNull(4, java.sql.Types.DATE);
			if(!Double.valueOf(quantità).equals(Double.NaN))stmt.setDouble(5, quantità);
			else stmt.setNull(5, java.sql.Types.VARCHAR);
			stmt.execute();
			int i=stmt.getUpdateCount();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	public int insertGittataRaffreddamento(int num, int idCotta, int idIng, LocalTime orario, double quantità){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO gittata_raffreddamento"
					+ "(num_gittata, FK_id_cotta, FK_id_ingrediente, orario, quantità) "
					+ "VALUES(?, ?, ?, ?, ?)");
			if((num>0)&&(idCotta>0)&&(idIng>0)){
				stmt.setInt(1, num);
				stmt.setInt(2, idCotta);
				stmt.setInt(3, idIng);
			}else return -1;
			if(!(orario==null)) stmt.setTime(4, Time.valueOf(orario));
			else stmt.setNull(4, java.sql.Types.TIME);
			if(!Double.valueOf(quantità).equals(Double.NaN))stmt.setDouble(5, quantità);
			else stmt.setNull(5, java.sql.Types.VARCHAR);
			stmt.execute();
			int i=stmt.getUpdateCount();
			return i;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
	}
	public boolean deleteGittata(int num, int idCotta, String fase){
		String table;
			switch(fase.toLowerCase()){
			case "bollitura": table="gittata_bollitura"; break;
			case "fermentazione": table="gittata_fermentazione"; break;
			case "raffreddamento": table="gittata_raffreddamento"; break;
			default: return false;
		}
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM "+table+" WHERE num_gittata=? AND FK_id_cotta=?");
			if((num>0)&&(idCotta>0)){
				stmt.setInt(1, num);
				stmt.setInt(2, idCotta);
			}else return false;
			stmt.executeUpdate();
			stmt.getUpdateCount();
			return true;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteGittateCotta(int idCotta, String fase){
		String table;
		switch(fase.toLowerCase()){
		case "bollitura": table="gittata_bollitura"; break;
		case "fermentazione": table="gittata_fermentazione"; break;
		case "raffreddamento": table="gittata_raffreddamento"; break;
		default: return false;
		}
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM "+table+" WHERE FK_id_cotta=?");
			if(idCotta>0) stmt.setInt(1, idCotta);
			else return false;
			stmt.executeUpdate();
			stmt.getUpdateCount();
			return true;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteGittateCotta(int idCotta){
		if(idCotta>0){
			if((this.deleteGittateCotta(idCotta, "bollitura"))&&(this.deleteGittateCotta(idCotta, "fermentazione"))&&(this.deleteGittateCotta(idCotta, "raffreddamento"))) return true;
			else return false;
		}else return false;
	}
	public int updateGittata(int num, int idCotta, String fase, int idIng, LocalDate data, LocalTime orario, double quantità){
		String table;
		switch(fase.toLowerCase()){
		case "bollitura": table="gittata_bollitura"; break;
		case "fermentazione": table="gittata_fermentazione"; break;
		case "raffreddamento": table="gittata_raffreddamento"; break;
		default: return 0;
		}
		try{
			PreparedStatement stmt;
			if(fase.toLowerCase().equals("fermentazione")) {
				stmt=this.connessione.prepareStatement("UPDATE "+table+" SET FK_id_ingrediente=?, data=?, quantità=? "
						+ "WHERE num_gittata=? AND FK_id_cotta=?");
				if(data==null)stmt.setNull(2, java.sql.Types.DATE);
				else stmt.setDate(2, Date.valueOf(data));
			}else{	stmt=this.connessione.prepareStatement("UPDATE TABLE "+table+" SET FK_id_ingrediente=?, orario=?, quantità=? "
					+ "WHERE num_gittata=? AND FK_id_cotta=?");
					if(orario==null) stmt.setNull(2, java.sql.Types.TIME);
					else stmt.setTime(2, Time.valueOf(orario));
			}
			if((idIng>0)&&(!Double.valueOf(quantità).equals(Double.NaN))&&(quantità>0)&&(num>0)&&(idCotta>0)) {
				stmt.setInt(1, idIng);
				stmt.setDouble(3, quantità);
				stmt.setInt(4, num);
				stmt.setInt(5, idCotta);
				return stmt.executeUpdate();
			}else return 0;
		}catch(SQLException e){
			e.printStackTrace();
			return 0;
		}
	}
	
	/*Gestione giudizio*/
	public int insertGiudizio(Giudizio g, int idCotta){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO giudizio (nome_giudice, "
					+ "esame_visivo, voto_visivo, esame_olfattivo, voto_olfattivo, esame_gustativo, voto_gustativo, "
					+ "esame_sensazioni_boccali, voto_sensazioni_boccali, opinione_generale, voto_generale, "
					+ "acetaldeide, alcoli_superiori, astringenza, diacetile, DMS, esteri_fruttati, erba_tagliata, colpo_di_luce, "
					+ "metallico, fenolico, solvente, muffa_stantio, acido, sulfureo, vegetale, lievito, ossidazione, autolisi, altro, FK_id_cotta, note, data)"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			if((g.getNomeGiudice()==null)||g.getNomeGiudice().equals("")) stmt.setNull(1, java.sql.Types.VARCHAR);
			else stmt.setString(1, g.getNomeGiudice());
			if(g.getEsameVisivo()==null) stmt.setNull(2, java.sql.Types.VARCHAR);
			else stmt.setString(2, g.getEsameVisivo());
			if(g.getVotoVisivo()==-1) stmt.setNull(3, java.sql.Types.INTEGER);
			else stmt.setInt(3, g.getVotoVisivo());
			if(g.getEsameOlfattivo()==null) stmt.setNull(4, java.sql.Types.VARCHAR);
			else stmt.setString(4, g.getEsameOlfattivo());
			if(g.getVotoOlfattivo()==-1) stmt.setNull(5, java.sql.Types.INTEGER);
			else stmt.setInt(5, g.getVotoOlfattivo());
			if(g.getEsameGustativo()==null) stmt.setNull(6, java.sql.Types.VARCHAR);
			else stmt.setString(6, g.getEsameGustativo());
			if(g.getVotoGustativo()==-1) stmt.setNull(7, java.sql.Types.INTEGER);
			else stmt.setInt(7, g.getVotoGustativo());
			if(g.getEsameSensazioniBoccali()==null) stmt.setNull(8, java.sql.Types.VARCHAR);
			else stmt.setString(8, g.getEsameSensazioniBoccali());
			if(g.getVotoSensazioniBoccali()==-1) stmt.setNull(9, java.sql.Types.INTEGER);
			else stmt.setInt(9, g.getVotoSensazioniBoccali());
			if(g.getEsameGenerale()==null) stmt.setNull(10, java.sql.Types.VARCHAR);
			else stmt.setString(10, g.getEsameGenerale());
			if(g.getVotoGenerale()==-1) stmt.setNull(11, java.sql.Types.INTEGER);
			else stmt.setInt(11, g.getVotoGenerale());
			if(g.isAcetaldeide()) stmt.setBoolean(12, Boolean.TRUE);
			else stmt.setBoolean(12, Boolean.FALSE);
			if(g.isAlcoliSuperiori()) stmt.setBoolean(13, Boolean.TRUE);
			else stmt.setBoolean(13, Boolean.FALSE);
			if(g.isAstringenza()) stmt.setBoolean(14, Boolean.TRUE);
			else stmt.setBoolean(14, Boolean.FALSE);
			if(g.isDiacetile()) stmt.setBoolean(15, Boolean.TRUE);
			else stmt.setBoolean(15, Boolean.FALSE);
			if(g.isDms()) stmt.setBoolean(16, Boolean.TRUE);
			else stmt.setBoolean(16, Boolean.FALSE);
			if(g.isEsteriFruttati()) stmt.setBoolean(17, Boolean.TRUE);
			else stmt.setBoolean(17, Boolean.FALSE);
			if(g.isErbaTagliata()) stmt.setBoolean(18, Boolean.TRUE);
			else stmt.setBoolean(18, Boolean.FALSE);
			if(g.isColpoDiLuce()) stmt.setBoolean(19, Boolean.TRUE);
			else stmt.setBoolean(19, Boolean.FALSE);
			if(g.isMetallico()) stmt.setBoolean(20, Boolean.TRUE);
			else stmt.setBoolean(20, Boolean.FALSE);
			if(g.isFenolico()) stmt.setBoolean(21, Boolean.TRUE);
			else stmt.setBoolean(21, Boolean.FALSE);
			if(g.isSolvente()) stmt.setBoolean(22, Boolean.TRUE);
			else stmt.setBoolean(22, Boolean.FALSE);
			if(g.isMuffaStantio()) stmt.setBoolean(23, Boolean.TRUE);
			else stmt.setBoolean(23, Boolean.FALSE);
			if(g.isAcido()) stmt.setBoolean(24, Boolean.TRUE);
			else stmt.setBoolean(24, Boolean.FALSE);
			if(g.isSulfureo()) stmt.setBoolean(25, Boolean.TRUE);
			else stmt.setBoolean(25, Boolean.FALSE);
			if(g.isVegetale()) stmt.setBoolean(26, Boolean.TRUE);
			else stmt.setBoolean(26, Boolean.FALSE);
			if(g.isLievito()) stmt.setBoolean(27, Boolean.TRUE);
			else stmt.setBoolean(27, Boolean.FALSE);
			if(g.isOssidazione()) stmt.setBoolean(28, Boolean.TRUE);
			else stmt.setBoolean(28, Boolean.FALSE);
			if(g.isAutolisi()) stmt.setBoolean(29, Boolean.TRUE);
			else stmt.setBoolean(29, Boolean.FALSE);
			if((g.getAltro()==null)||(g.getAltro().equals(""))) stmt.setNull(30, java.sql.Types.VARCHAR);
			else stmt.setString(30, g.getAltro());
			if(idCotta>0) stmt.setInt(31, idCotta);
			else return -1;
			if((g.getNote()==null)||(g.getNote().equals(""))) stmt.setNull(32, java.sql.Types.VARCHAR);
			else stmt.setString(32, g.getNote());
			if(g.getData()==null) stmt.setNull(33, java.sql.Types.DATE);
			else stmt.setDate(33, g.getData());
			stmt.execute();
			ResultSet rs=stmt.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}else return -1;
		}catch(SQLException e){
			e.printStackTrace();
			return -1;
		}
		
	}
	public int deleteGiudizio(int id){
		if(id>0){
			try{
				PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM giudizio WHERE id_giudizio=?");
				stmt.setInt(1, id);
				stmt.executeUpdate();
				return stmt.getUpdateCount();
			}catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
		}else return -1;
	}
	public int updateGiudizio(int id, int idCotta, Giudizio g){
		if((id>0)&&(idCotta>0)){
			try{
				PreparedStatement stmt=this.connessione.prepareStatement("UPDATE giudizio SET nome_giudice=?, "
						+ "esame_visivo=?, voto_visivo=?, esame_olfattivo=?, voto_olfattivo=?, esame_gustativo=?, voto_gustativo=?, "
						+ "esame_sensazioni_boccali=?, voto_sensazioni_boccali=?, opinione_generale=?, voto_generale=?, "
						+ "acetaldeide=?, alcoli_superiori=?, astringenza=?, diacetile=?, DMS=?, esteri_fruttati=?, erba_tagliata=?, colpo_di_luce=?, "
						+ "metallico=?, fenolico=?, solvente=?, muffa_stantio=?, acido=?, sulfureo=?, vegetale=?, "
						+ "lievito=?, ossidazione=?, autolisi=?, altro=?, FK_id_cotta=?, note=?, data=? WHERE id_giudizio=?");
				if((g.getNomeGiudice()==null)||g.getNomeGiudice().equals("")) stmt.setNull(1, java.sql.Types.VARCHAR);
				else stmt.setString(1, g.getNomeGiudice());
				if(g.getEsameVisivo()==null) stmt.setNull(2, java.sql.Types.VARCHAR);
				else stmt.setString(2, g.getEsameVisivo());
				if(g.getVotoVisivo()==-1) stmt.setNull(3, java.sql.Types.INTEGER);
				else stmt.setInt(3, g.getVotoVisivo());
				if(g.getEsameOlfattivo()==null) stmt.setNull(4, java.sql.Types.VARCHAR);
				else stmt.setString(4, g.getEsameOlfattivo());
				if(g.getVotoOlfattivo()==-1) stmt.setNull(5, java.sql.Types.INTEGER);
				else stmt.setInt(5, g.getVotoOlfattivo());
				if(g.getEsameGustativo()==null) stmt.setNull(6, java.sql.Types.VARCHAR);
				else stmt.setString(6, g.getEsameGustativo());
				if(g.getVotoGustativo()==-1) stmt.setNull(7, java.sql.Types.INTEGER);
				else stmt.setInt(7, g.getVotoGustativo());
				if(g.getEsameSensazioniBoccali()==null) stmt.setNull(8, java.sql.Types.VARCHAR);
				else stmt.setString(8, g.getEsameSensazioniBoccali());
				if(g.getVotoSensazioniBoccali()==-1) stmt.setNull(9, java.sql.Types.INTEGER);
				else stmt.setInt(9, g.getVotoSensazioniBoccali());
				if(g.getEsameGenerale()==null) stmt.setNull(10, java.sql.Types.VARCHAR);
				else stmt.setString(10, g.getEsameGenerale());
				if(g.getVotoGenerale()==-1) stmt.setNull(11, java.sql.Types.INTEGER);
				else stmt.setInt(11, g.getVotoGenerale());
				if(g.isAcetaldeide()) stmt.setBoolean(12, Boolean.TRUE);
				else stmt.setBoolean(12, Boolean.FALSE);
				if(g.isAlcoliSuperiori()) stmt.setBoolean(13, Boolean.TRUE);
				else stmt.setBoolean(13, Boolean.FALSE);
				if(g.isAstringenza()) stmt.setBoolean(14, Boolean.TRUE);
				else stmt.setBoolean(14, Boolean.FALSE);
				if(g.isDiacetile()) stmt.setBoolean(15, Boolean.TRUE);
				else stmt.setBoolean(15, Boolean.FALSE);
				if(g.isDms()) stmt.setBoolean(16, Boolean.TRUE);
				else stmt.setBoolean(16, Boolean.FALSE);
				if(g.isEsteriFruttati()) stmt.setBoolean(17, Boolean.TRUE);
				else stmt.setBoolean(17, Boolean.FALSE);
				if(g.isErbaTagliata()) stmt.setBoolean(18, Boolean.TRUE);
				else stmt.setBoolean(18, Boolean.FALSE);
				if(g.isColpoDiLuce()) stmt.setBoolean(19, Boolean.TRUE);
				else stmt.setBoolean(19, Boolean.FALSE);
				if(g.isMetallico()) stmt.setBoolean(20, Boolean.TRUE);
				else stmt.setBoolean(20, Boolean.FALSE);
				if(g.isFenolico()) stmt.setBoolean(21, Boolean.TRUE);
				else stmt.setBoolean(21, Boolean.FALSE);
				if(g.isSolvente()) stmt.setBoolean(22, Boolean.TRUE);
				else stmt.setBoolean(22, Boolean.FALSE);
				if(g.isMuffaStantio()) stmt.setBoolean(23, Boolean.TRUE);
				else stmt.setBoolean(23, Boolean.FALSE);
				if(g.isAcido()) stmt.setBoolean(24, Boolean.TRUE);
				else stmt.setBoolean(24, Boolean.FALSE);
				if(g.isSulfureo()) stmt.setBoolean(25, Boolean.TRUE);
				else stmt.setBoolean(25, Boolean.FALSE);
				if(g.isVegetale()) stmt.setBoolean(26, Boolean.TRUE);
				else stmt.setBoolean(26, Boolean.FALSE);
				if(g.isLievito()) stmt.setBoolean(27, Boolean.TRUE);
				else stmt.setBoolean(27, Boolean.FALSE);
				if(g.isOssidazione()) stmt.setBoolean(28, Boolean.TRUE);
				else stmt.setBoolean(28, Boolean.FALSE);
				if(g.isAutolisi()) stmt.setBoolean(29, Boolean.TRUE);
				else stmt.setBoolean(29, Boolean.FALSE);
				if((g.getAltro()==null)||(g.getAltro().equals(""))) stmt.setNull(30, java.sql.Types.VARCHAR);
				else stmt.setString(30, g.getAltro());
				stmt.setInt(31, idCotta);
				if((g.getNote()==null)||(g.getNote().equals(""))) stmt.setNull(32, java.sql.Types.VARCHAR);
				else stmt.setString(32, g.getNote());
				if(g.getData()==null) stmt.setNull(33, java.sql.Types.DATE);
				else stmt.setDate(33, g.getData());
				stmt.setInt(34, id);
				return stmt.executeUpdate();
			}catch(SQLException e){
				e.printStackTrace();
				return -1;
			}
		}else return -1;
	}
	public int deleteGiudiziCotta(int idCotta) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM giudizio WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		stmt.executeUpdate();
		return stmt.getUpdateCount();
	}
	
	/*Gestione ingrediente*/
	public int insertIngrediente(Ingrediente ing) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO ingrediente(nome, tipo, colore_ebc, alfa_acidi, liquido, quantità_busta, tipologia, note)"
				+ "VALUES(?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
		stmt.setString(1, ing.getNome());
		stmt.setString(2, ing.getTipo());
		stmt.setString(7, ing.getTipologia());
		stmt.setString(8, ing.getNote());
		if(ing instanceof Malto) {
			stmt.setInt(3, ((Malto) ing).getColore_ebc());
			stmt.setNull(4, java.sql.Types.FLOAT);
			stmt.setNull(5, java.sql.Types.BIT);
			stmt.setNull(6, java.sql.Types.FLOAT);
		}else if(ing instanceof Lievito){
			stmt.setNull(3, java.sql.Types.INTEGER);
			stmt.setNull(4, java.sql.Types.FLOAT);
			if(((Lievito) ing).getStato_materiale().equals("liquido")) stmt.setBoolean(5, Boolean.TRUE);
			else stmt.setBoolean(5, Boolean.FALSE);
			stmt.setDouble(6, ((Lievito) ing).getQuantità_busta());
		}else if(ing instanceof Luppolo){
			stmt.setNull(3, java.sql.Types.INTEGER);
			stmt.setDouble(4, ((Luppolo) ing).getAlfa_acidi());
			stmt.setNull(5, java.sql.Types.BIT);
			stmt.setNull(6, java.sql.Types.FLOAT);
		}
		stmt.executeUpdate();
		ResultSet rs=stmt.getGeneratedKeys();
		if(rs.next()) return rs.getInt(1);
		else return 0;		
	}
	public int deleteIngrediente(int id) throws SQLException{
		if(id<=0) return 0;
		else{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM ingrediente WHERE id_ingrediente=?");
			stmt.setInt(1, id);
			return stmt.executeUpdate();
		}
	}
	public int updateIngrediente(Ingrediente ing) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("UPDATE ingrediente "
				+ "SET nome=?, tipo=?, colore_ebc=?, alfa_acidi=?, liquido=?, quantità_busta=?, "
				+ "tipologia=?, note=? "
				+ "WHERE id_ingrediente=?");
		stmt.setString(1, ing.getNome());
		stmt.setString(2, ing.getTipo());
		stmt.setString(7, ing.getTipologia());
		stmt.setString(8, ing.getNote());
		if(ing instanceof Malto){
			stmt.setInt(3, ((Malto) ing).getColore_ebc());
			stmt.setNull(4, java.sql.Types.FLOAT);
			stmt.setNull(5, java.sql.Types.BOOLEAN);
			stmt.setNull(6, java.sql.Types.FLOAT);
		}else if(ing instanceof Lievito){
			stmt.setNull(3, java.sql.Types.INTEGER);
			stmt.setNull(4, java.sql.Types.FLOAT);
			if(((Lievito) ing).getStato_materiale().equals("lievito")) stmt.setBoolean(5, Boolean.TRUE);
			else stmt.setBoolean(5, Boolean.FALSE);
			stmt.setDouble(6, ((Lievito) ing).getQuantità_busta());
		}else if(ing instanceof Luppolo){
			stmt.setNull(3, java.sql.Types.INTEGER);
			stmt.setDouble(4, ((Luppolo) ing).getAlfa_acidi());
			stmt.setNull(5, java.sql.Types.BOOLEAN);
			stmt.setNull(6, java.sql.Types.FLOAT);
		}
		stmt.setInt(9, ing.getId());
		return stmt.executeUpdate();
	}
	
	/*Gestione ricetta*/
	public int insertRicetta(Ricetta r){
		int idRicetta;
		idRicetta=this.insertRicetta(r.getNome(), r.getNote(), r.getData_creazione(), r.getStile().getId());
		Utilizzo u;
		for(int i=0; i<r.getListaIngredienti().size(); i++){
			u=r.getListaIngredienti().get(i);
			if(u instanceof UtilizzoLievito) this.insertUtilizzoRicettaLievito(idRicetta, u.getIngrediente().getTipo(), ((UtilizzoLievito) u).getNumeroBuste(), ((UtilizzoLievito) u).getLitri_starter(), ((UtilizzoLievito) u).getGiorni_starter());
			else this.insertUtilizzoRicettaIngrediente(idRicetta, u.getIngrediente().getId(), u.getQuantità());
		}
		return idRicetta;
	}
	public boolean insertUtilizzoRicettaIngrediente(int idRicetta, int idIngrediente, double quantità){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO utilizzo_ricetta_ingrediente (FK_id_ricetta, FK_id_ingrediente, quantità)"
					+ "VALUES (?, ?, ?)");
			stmt.setInt(1, idRicetta);
			stmt.setInt(2, idIngrediente);
			if(!Double.valueOf(quantità).equals(Double.NaN)) stmt.setDouble(3, quantità);
			else stmt.setNull(3, java.sql.Types.FLOAT);
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean insertUtilizzoRicettaLievito(int idRicetta, String  tipoLievito, double numBuste, double litriStarter, int giorniStarter){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO utilizzo_ricetta_lievito (FK_id_ricetta, FK_tipo_lievito, numero_buste, giorni_starter, litri_starter)"
					+ "VALUES (?, ?, ?, ?, ?)");
			stmt.setInt(1, idRicetta);
			stmt.setString(2, tipoLievito);
			if(!Double.valueOf(numBuste).equals(Double.NaN)) stmt.setDouble(3, numBuste);
			else stmt.setNull(3, java.sql.Types.FLOAT);
			if(!Double.valueOf(litriStarter).equals(Double.NaN)) stmt.setDouble(5, litriStarter);
			else stmt.setNull(5, java.sql.Types.FLOAT);
			if(!Double.valueOf(giorniStarter).equals(Double.NaN)) stmt.setDouble(4, giorniStarter);
			else stmt.setNull(4, java.sql.Types.FLOAT);
			stmt.execute();
			return true;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		
	}
	private int insertRicetta(String nome, String note, Date data, int idStile){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO ricetta (nome, note, data_creazione, FK_id_stile)"
					+ "VALUES (?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, nome);
			if((note==null)||(note.equals(""))) stmt.setNull(2, java.sql.Types.VARCHAR);
			else stmt.setString(2, note);
			if(data==null) stmt.setNull(3, java.sql.Types.DATE);
			else stmt.setDate(3, data);
			if(idStile==-1) stmt.setNull(4, java.sql.Types.INTEGER);
			else stmt.setInt(4, idStile);
			stmt.execute();
			ResultSet rs=stmt.getGeneratedKeys();
			if(rs.next()){
				return rs.getInt(1);
			}else return -1;
		}catch (SQLException e){
			e.printStackTrace();
			return -1;
		}
		
	}
	public boolean updateRicetta(int id, String nome, String note, Date data, int idStile){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE ricetta SET nome=?, note=?, data_creazione=?, FK_id_stile=? WHERE id_ricetta=?");
			if((!(nome==null))&&(!nome.equals(""))) stmt.setString(1, nome);
			else stmt.setNull(1, java.sql.Types.VARCHAR);
			if((note==null)||(note.equals(""))) stmt.setNull(2, java.sql.Types.VARCHAR);
			else stmt.setString(2, note);
			if(data==null) stmt.setNull(3, java.sql.Types.DATE);
			else stmt.setDate(3, data);
			if(idStile==-1) stmt.setNull(4, java.sql.Types.INTEGER);
			else stmt.setInt(4, idStile);
			stmt.setInt(5, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteRicetta(int id){
		PreparedStatement stmt;
		//Elimino gli utilizzi degli ingredienti
		try{
			stmt=this.connessione.prepareStatement("DELETE FROM utilizzo_ricetta_ingrediente WHERE FK_id_ricetta=?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
		//Elimino gli utilizzi dei lieviti
		try{
			stmt=this.connessione.prepareStatement("DELETE FROM utilizzo_ricetta_lievito WHERE FK_id_ricetta=?");
			stmt.setInt(1, id);
			stmt.executeUpdate();
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
		//Elimino la ricetta
		try{
			stmt=this.connessione.prepareStatement("DELETE FROM ricetta WHERE id_ricetta=?");
			stmt.setInt(1, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*Gestione rilievo*/
	public int insertRilievo(Rilievo r, int num, int idCotta) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement(""
				+ "INSERT INTO rilievo_"+r.getFaseRilievo().toLowerCase()+" "
				+ "VALUES(?, ?, ?, ?, ?, ?)");
		stmt.setInt(1, num);
		stmt.setInt(2, idCotta);
		if(!Double.valueOf(r.getTemperatura()).equals(Double.NaN)) stmt.setDouble(3, r.getTemperatura());
		else stmt.setNull(3, java.sql.Types.FLOAT);
		if(!Double.valueOf(r.getPh()).equals(Double.NaN)) stmt.setDouble(4, r.getPh());
		else stmt.setNull(4, java.sql.Types.FLOAT);
		if(r instanceof RilievoFermentazione){
			if (((RilievoFermentazione) r).getDataInizio()!=null) stmt.setDate(5, Date.valueOf(((RilievoFermentazione) r).getDataInizio()));
			else stmt.setNull(5, java.sql.Types.DATE);
			if(((RilievoFermentazione) r).getdataFine()!=null) stmt.setDate(6, Date.valueOf(((RilievoFermentazione) r).getdataFine()));
			else stmt.setNull(6, java.sql.Types.DATE);
		}else if(r instanceof RilievoAmmostamento){
			if(((RilievoAmmostamento) r).getOrarioInizio()!=null) stmt.setTime(5, Time.valueOf(((RilievoAmmostamento) r).getOrarioInizio()));
			else stmt.setNull(5, java.sql.Types.TIME);
			if(((RilievoAmmostamento) r).getOrarioFine()!=null)	stmt.setTime(6, Time.valueOf(((RilievoAmmostamento) r).getOrarioFine()));
			else stmt.setNull(6, java.sql.Types.TIME);
		}else if(r instanceof RilievoFiltraggio){
			if(((RilievoFiltraggio) r).getOrarioInizio()!=null)	stmt.setTime(5, Time.valueOf(((RilievoFiltraggio) r).getOrarioInizio()));
			else stmt.setNull(5, java.sql.Types.TIME);
			if(((RilievoFiltraggio) r).getOrarioFine()!=null) stmt.setTime(6, Time.valueOf(((RilievoFiltraggio) r).getOrarioFine()));
			else stmt.setNull(6, java.sql.Types.TIME);
		}else if(r instanceof RilievoBollitura){
			if(((RilievoBollitura) r).getOrarioInizio()!=null) stmt.setTime(5, Time.valueOf(((RilievoBollitura) r).getOrarioInizio()));
			else stmt.setNull(5, java.sql.Types.TIME);
			if(((RilievoBollitura) r).getOrarioFine()!=null) stmt.setTime(6, Time.valueOf(((RilievoBollitura) r).getOrarioFine()));
			else stmt.setNull(6, java.sql.Types.TIME);
		}
		return stmt.executeUpdate();
	}
	public int updateRilievo(Rilievo r, int num ,int idCotta) throws SQLException{
		this.deleteRilievo(num, idCotta, r.getFaseRilievo().toLowerCase());
		return this.insertRilievo(r, num, idCotta);
	}
	public int deleteRilievo(int num, int idCotta, String fase) throws SQLException{
		PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM rilievo_"
				+fase.toLowerCase()+" WHERE num_rilievo=? AND FK_id_cotta=?");
		stmt.setInt(1, num);
		stmt.setInt(2, idCotta);
		return stmt.executeUpdate();
	}
	public int deleteRilieviCotta(int idCotta) throws SQLException{
		int righe=0;
		PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM rilievo_ammostamento WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		righe+=stmt.executeUpdate();
		stmt=this.connessione.prepareStatement("DELETE FROM rilievo_filtraggio WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		righe+=stmt.executeUpdate();
		stmt=this.connessione.prepareStatement("DELETE FROM rilievo_bollitura WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		righe+=stmt.executeUpdate();
		stmt=this.connessione.prepareStatement("DELETE FROM rilievo_fermentazione WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		righe+=stmt.executeUpdate();
		return righe;
	}
	
	/*Gestione stile*/
	private int insertStile(String nome, String aroma, String aspetto, String sapore, String sensBoccali, String impGenerali, String commenti, String ing, String storia,
			int IBUMin, int IBUMax, int SRMMin, int SRMMax, int OGMin, int OGMax, int FGMin, int FGMax, double ABVMin, double ABVMax, int idCategoria, String note){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("INSERT INTO stile(nome, aroma, aspetto, sapore, sensazioni_boccali, "
					+ "impressioni_generali, commenti, ingredienti, storia, IBU_min, IBU_max, SRM_min, SRM_max, OG_min, OG_max, "
					+ "FG_min, FG_max, ABV_min, ABV_max, FK_id_categoria, note) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)", PreparedStatement.RETURN_GENERATED_KEYS);
			if(!nome.equals("")) stmt.setString(1, nome);
			else stmt.setNull(1, java.sql.Types.VARCHAR);
			if(!aroma.equals("")) stmt.setString(2, aroma);
			else stmt.setNull(2, java.sql.Types.VARCHAR);
			if(!aspetto.equals("")) stmt.setString(3, aspetto);
			else stmt.setNull(3, java.sql.Types.VARCHAR);
			if(!sapore.equals("")) stmt.setString(4, sapore);
			else stmt.setNull(4, java.sql.Types.VARCHAR);
			if(!sensBoccali.equals("")) stmt.setString(5, sensBoccali);
			else stmt.setNull(5, java.sql.Types.VARCHAR);
			if(!impGenerali.equals("")) stmt.setString(6, impGenerali);
			else stmt.setNull(6, java.sql.Types.VARCHAR);
			if(!commenti.equals("")) stmt.setString(7, commenti);
			else stmt.setNull(7, java.sql.Types.VARCHAR);
			if(!ing.equals("")) stmt.setString(8, ing);
			else stmt.setNull(8, java.sql.Types.VARCHAR);
			if(!storia.equals("")) stmt.setString(9, storia);
			else stmt.setNull(9, java.sql.Types.VARCHAR);
			if(IBUMin==-1) stmt.setNull(10, java.sql.Types.INTEGER);
			else stmt.setInt(10, IBUMin);
			if(IBUMax==-1) stmt.setNull(11, java.sql.Types.INTEGER); 
			else stmt.setInt(11, IBUMax);
			if(SRMMin==-1) stmt.setNull(12, java.sql.Types.INTEGER);
			else stmt.setInt(12, SRMMin);
			if(SRMMax==-1) stmt.setNull(13, java.sql.Types.INTEGER);
			else stmt.setInt(13, SRMMax);
			if(OGMin==-1) stmt.setNull(14, java.sql.Types.INTEGER);
			else stmt.setInt(14, OGMin);
			if(OGMax==-1) stmt.setNull(15, java.sql.Types.INTEGER);
			else stmt.setInt(15, OGMax);
			if(FGMin==-1) stmt.setNull(16, java.sql.Types.INTEGER);
			else stmt.setInt(16, FGMin);
			if (FGMax==-1)stmt.setNull(17, java.sql.Types.INTEGER);
			else stmt.setInt(17, FGMax);
			if(!Double.valueOf(ABVMin).equals(Double.NaN)) stmt.setDouble(18, ABVMin);
			else stmt.setNull(18, java.sql.Types.FLOAT);
			if(!Double.valueOf(ABVMax).equals(Double.NaN)) stmt.setDouble(19, ABVMax);
			else stmt.setNull(19, java.sql.Types.FLOAT);
			if(idCategoria!=-1) stmt.setInt(20, idCategoria);
			else stmt.setNull(20, java.sql.Types.INTEGER);
			if(!note.equals("")) stmt.setString(21, note);
			else stmt.setNull(21, java.sql.Types.VARCHAR);
			stmt.execute();
			ResultSet rs=stmt.getGeneratedKeys();
			if(rs.next()) return rs.getInt(1);
			else return -1;
		}catch (SQLException e){
			e.printStackTrace();
			return -1;
		}
		
	}
	public int insertStile(Stile s){
		return this.insertStile(s.getNome(), s.getAroma(), s.getAspetto(), s.getSapore(), s.getSensazioni_boccali(), 
				s.getImpressioni_generali(), s.getCommenti(), s.getIngredienti(), s.getStoria(), s.getIBU_min(), s.getIBU_max(), 
				s.getSRM_min(), s.getSRM_max(), s.getOG_min(), s.getOG_max(), s.getFG_min(), s.getFG_max(), s.getABV_min(), s.getABV_max(), 
				s.getCategoria().getId(), s.getNote());
	}
	public boolean updateStile(int id, String nome, String aroma, String aspetto, String sapore, String sensBoccali, String impGenerali, String commenti, String ing, String storia,
			int IBUMin, int IBUMax, int SRMMin, int SRMMax, int OGMin, int OGMax, int FGMin, int FGMax, double ABVMin, double ABVMax, int idCategoria, String note) throws SQLException{
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("UPDATE stile "
					+ "SET nome=?, aroma=?, aspetto=?, sapore=?, sensazioni_boccali=?, "
					+ "impressioni_generali=?, commenti=?, ingredienti=?, storia=?, "
					+ "IBU_min=?, IBU_max=?, SRM_min=?, SRM_max=?, OG_min=?, OG_max=?, "
					+ "FG_min=?, FG_max=?, ABV_min=?, ABV_max=?, FK_id_categoria=?, note=?"
					+ " WHERE id_stile=?");
			if(!nome.equals("")) stmt.setString(1, nome);
			else stmt.setNull(1, java.sql.Types.VARCHAR);
			if(!aroma.equals("")) stmt.setString(2, aroma);
			else stmt.setNull(2, java.sql.Types.VARCHAR);
			if(!aspetto.equals("")) stmt.setString(3, aspetto);
			else stmt.setNull(3, java.sql.Types.VARCHAR);
			if(!sapore.equals("")) stmt.setString(4, sapore);
			else stmt.setNull(4, java.sql.Types.VARCHAR);
			if(!sensBoccali.equals("")) stmt.setString(5, sensBoccali);
			else stmt.setNull(5, java.sql.Types.VARCHAR);
			if(!impGenerali.equals("")) stmt.setString(6, impGenerali);
			else stmt.setNull(6, java.sql.Types.VARCHAR);
			if(!commenti.equals("")) stmt.setString(7, commenti);
			else stmt.setNull(7, java.sql.Types.VARCHAR);
			if(!ing.equals("")) stmt.setString(8, ing);
			else stmt.setNull(8, java.sql.Types.VARCHAR);
			if(!storia.equals("")) stmt.setString(9, storia);
			else stmt.setNull(9, java.sql.Types.VARCHAR);
			if(IBUMin==-1) stmt.setNull(10, java.sql.Types.INTEGER);
			else stmt.setInt(10, IBUMin);
			if(IBUMax==-1) stmt.setNull(11, java.sql.Types.INTEGER); 
			else stmt.setInt(11, IBUMax);
			if(SRMMin==-1) stmt.setNull(12, java.sql.Types.INTEGER);
			else stmt.setInt(12, SRMMin);
			if(SRMMax==-1) stmt.setNull(13, java.sql.Types.INTEGER);
			else stmt.setInt(13, SRMMax);
			if(OGMin==-1) stmt.setNull(14, java.sql.Types.INTEGER);
			else stmt.setInt(14, OGMin);
			if(OGMax==-1) stmt.setNull(15, java.sql.Types.INTEGER);
			else stmt.setInt(15, OGMax);
			if(FGMin==-1) stmt.setNull(16, java.sql.Types.INTEGER);
			else stmt.setInt(16, FGMin);
			if(FGMax==-1) stmt.setNull(17, java.sql.Types.INTEGER);
			else stmt.setInt(17, FGMax);
			if(!Double.valueOf(ABVMin).equals(Double.NaN)) stmt.setDouble(18, ABVMin);
			else stmt.setNull(18, java.sql.Types.FLOAT);
			if(!Double.valueOf(ABVMax).equals(Double.NaN)) stmt.setDouble(19, ABVMax);
			else stmt.setNull(19, java.sql.Types.FLOAT);
			if(idCategoria!=-1) stmt.setInt(20, idCategoria);
			else stmt.setNull(20, java.sql.Types.INTEGER);
			if(!note.equals("")) stmt.setString(21, note);
			else stmt.setNull(21, java.sql.Types.VARCHAR);
			stmt.setInt(22, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch (SQLException e){
			e.printStackTrace();
			return false;
		}
	}
	public boolean deleteStile(int id){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM stile WHERE id_stile=?");
			stmt.setInt(1, id);
			if(stmt.executeUpdate()!=0) return true;
			else return false;
		}catch(SQLException e){
			e.printStackTrace();
			return false;
		}
	}
		
	/*Gestione utilizzi*/
	public int insertUtilizzoCotta(Utilizzo u, int idCotta) throws SQLException{
		PreparedStatement stmt;
		if(u instanceof UtilizzoLievito){
			stmt=this.connessione.prepareStatement("INSERT INTO utilizzo_cotta_lievito VALUES(?, ?, ?, ?, ?)");
			stmt.setInt(1, idCotta);
			stmt.setString(2, u.getIngrediente().getTipo());
			if(((UtilizzoLievito) u).getNumeroBuste()!=Double.NaN)	stmt.setDouble(3, ((UtilizzoLievito) u).getNumeroBuste());
			else stmt.setNull(3, java.sql.Types.FLOAT);
			if(((UtilizzoLievito) u).getGiorni_starter()==-1) stmt.setNull(4, java.sql.Types.INTEGER);
			else stmt.setInt(4, ((UtilizzoLievito) u).getGiorni_starter());
			if(((UtilizzoLievito) u).getLitri_starter()==Double.NaN)	stmt.setNull(5, java.sql.Types.FLOAT);
			else stmt.setDouble(5, ((UtilizzoLievito) u).getLitri_starter());
			stmt.execute();
			return stmt.getUpdateCount();
		}else{
			stmt=this.connessione.prepareStatement("INSERT INTO utilizzo_cotta_ingrediente VALUES(?, ?, ?)");
			stmt.setInt(1, idCotta);
			stmt.setInt(2, u.getIngrediente().getId());
			if(u.getQuantità()!=Double.NaN)	stmt.setDouble(3, u.getQuantità());
			else stmt.setNull(3, java.sql.Types.FLOAT);
			stmt.execute();
			return stmt.getUpdateCount();
		}
	}
	public int deleteUtilizziCotta(int idCotta) throws SQLException{
		int righe=0;
		PreparedStatement stmt=this.connessione.prepareStatement("DELETE FROM utilizzo_cotta_ingrediente WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		stmt.executeUpdate();
		righe+=stmt.getUpdateCount();
		stmt=this.connessione.prepareStatement("DELETE FROM utilizzo_cotta_lievito WHERE FK_id_cotta=?");
		stmt.setInt(1, idCotta);
		stmt.executeUpdate();
		righe+=stmt.getUpdateCount();
		return righe;
	}
	
		
	public Utente login(String username, String password){
		try{
			PreparedStatement stmt=this.connessione.prepareStatement("SELECT * FROM utente WHERE username=? AND pw=?");
			stmt.setString(1, username);
			stmt.setString(2, password);
			ResultSet rs=stmt.executeQuery();
			rs.next();
			if (rs.getObject("id_utente")==null) return new Utente();
			else {
				Utente u=new Utente();
				u.setNome(rs.getString("nome"));
				u.setCognome(rs.getString("cognome"));
				u.setDataNascita(rs.getDate("data_nascita").toLocalDate());
				u.setEmail(rs.getString("email"));
				u.setId(rs.getInt("id_utente"));
				u.setIndirizzo(rs.getString("indirizzo"));
				return u;
			}
		}catch(SQLException e){
			e.printStackTrace();
		}
		return new Utente();
	}
	
}
