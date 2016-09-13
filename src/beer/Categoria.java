package beer;

import anelloAromaticoServerPack.dbException;

public class Categoria {
	protected int id;
	private String nome;
	private String note;
	
	public Categoria() {
	}

	public Categoria(String name){
		this.nome=name;
		this.note="";
	}
	
	public Categoria(String name, String nota){ 
		this.nome=name;
		this.note=nota;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) throws dbException {
		if (id>0) this.id = id;
		else throw new dbException("Id negativo");
	}

	/**
	 * @return the nome
	 */
	public String getNome() {
		return nome;
	}
	

	/**
	 * @param name the nome to set
	 * @throws Exception 
	 */
	public void setNome(String name) throws beerException {
		if (name!=null){
			this.nome = name;
		}else{
			throw new beerException("Null name");
		}
	}

	/**
	 * @return the note
	 */
	public String getNote() {
		return note;
	}

	/**
	 * @param nota the note to set
	 * @throws Exception 
	 */
	public void setNote(String nota) throws beerException {
		if (nota!=null){
			this.note = nota;
		}else{
			throw new beerException("Null note");
		}
	}
	
	public void stampa(){
		System.out.println("--------------------");
		System.out.println("STAMPA CATEGORIA");
		System.out.println("idCategoria: "+this.id);
		System.out.println("Nome: "+this.nome);
		System.out.println("Note: "+this.note);
	}

}
