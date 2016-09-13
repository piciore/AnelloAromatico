package anelloAromaticoServerPack;

import java.io.PrintWriter;
import java.time.LocalDate;

public class Utente {
	protected String nome;
	protected String cognome;
	protected LocalDate dataNascita;
	protected String email;
	protected int id;
	protected String indirizzo;
	
	
	public Utente() {
	}
	
	public void stampa(PrintWriter out){
		out.println("Id: "+this.id);
		out.println("Nome: "+this.nome);
		out.println("Cognome: "+this.cognome);
		//out.println("Data di nascita: "+this.dataNascita.toString());
		out.println("Indirizzo mail: "+this.getEmail());
		out.println("Indirizzo: "+this.indirizzo);
		out.flush();
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


	public String getCognome() {
		return cognome;
	}


	public void setCognome(String cognome) {
		this.cognome = cognome;
	}


	public LocalDate getDataNascita() {
		return dataNascita;
	}


	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		if (id>0) this.id = id;
	}


	public String getIndirizzo() {
		return indirizzo;
	}


	public void setIndirizzo(String indirizzo) {
		this.indirizzo = indirizzo;
	}

}
