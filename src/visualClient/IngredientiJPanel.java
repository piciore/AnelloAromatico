package visualClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import anelloAromaticoServerPack.ServerResponse;
import anelloAromaticoServerPack.dbException;
import beer.Ingrediente;
import beer.beerException;
import images.*;

public class IngredientiJPanel extends JPanel implements ActionListener{
	private static final int defaultHue=63;
	private static final int defaultSaturation=54;
	private static final int defaultBrightness=100;
	protected BigJTabbedPane parent;
	protected JButton aggiungiIngJButton;
	protected JButton modificaIngJButton;
	protected JButton eliminaIngJButton;
	protected static final String[] tipologie={"---", "Malto", "Lievito", "Luppolo", "Spezia", "Altro"};
	protected JComboBox<String> sceltaTipologia;
	protected JScrollPane sceltaIngJScrollPane; 
	protected JPanel nordPanel;
	protected JPanel centerPanel;
	protected IngredienteJPanel ingredienteJPanel;
	protected AnelloAromaticoHttpClient client;
	protected ArrayList<Ingrediente> listaIngredienti;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Riceve un ArrayList di ingredienti con cui rimpire il JScrollPane sulla sinistra della pagina
	 * @param lista ArrayList<Ingrediente> con cui riepire l'elenco di sinistra
	 */
	private void riempiElenco(ArrayList<Ingrediente> lista){
		//Creo un pannelloLista con tante righe quante gli ingredienti
		//Per ogni ingrediente creo un pannello riga contenente nome, tipo e freccia
		if(lista==null) lista=new ArrayList<Ingrediente>();
		JPanel pannelloLista=new JPanel();
		if(lista.size()>10)	pannelloLista.setLayout(new GridLayout(lista.size(), 1));
		else pannelloLista.setLayout(new GridLayout(10, 1));
		if(lista.size()==0){
			JLabel zeroJLabel=new JLabel("Non ci sono ingredienti da visualizzare");
			zeroJLabel.setVisible(true);
			pannelloLista.add(zeroJLabel);
		}
		//System.out.println(getClass().getResource("frecciaDestraSmall.jpg"));
		//System.out.println(getClass().getResource("Ruota.jpg"));
		//Icon icon=new ImageIcon("frecciaDestraSmall.jpg");
		Icon icon=new ImageIcon(getClass().getResource("frecciaDestraSmall.jpg"));
		//Icon icon=new ImageIcon(ClassLoader.getSystemResource("frecciaDestraSmall.jpg"));
		for(int i=0; i<lista.size(); i++){
			JPanel riga=new JPanel();
			riga.setLayout(new BorderLayout());
			riga.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			riga.setPreferredSize(new Dimension(0, 25));
			
			JLabel ingrediente= new JLabel(lista.get(i).getNome()+"-"+lista.get(i).getTipo());
			ingrediente.setBorder(BorderFactory.createEmptyBorder(1, 2, 0, 1));
			riga.add(ingrediente);
			
			JButton freccia=new JButton(icon);
			freccia.setPreferredSize(new Dimension(20, 20));
			riga.add(freccia, BorderLayout.EAST);
			freccia.setActionCommand("modificaIngrediente".concat(String.valueOf(i)));
			freccia.addActionListener(this);
			pannelloLista.add(riga);
		}
		pannelloLista.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaIngJScrollPane.getViewport().add(pannelloLista);
		this.sceltaIngJScrollPane.validate();
		
	}
	/**
	 * 
	 * @param numTipologia
	 * @return Un ArrayList<Ingrediente> contenente tutti gli ingredienti della tipologia specificata nel database
	 */
	private ArrayList<Ingrediente> getIngredienti(int numTipologia){
		String tipologia;
		switch(numTipologia){
		case 0: return null;
		case 1: tipologia="malto"; break;
		case 2: tipologia="lievito"; break;
		case 3: tipologia="luppolo"; break;
		case 4: tipologia="spezia"; break;
		case 5: tipologia="altro"; break;
		default: return null;
		}
		try {
			ServerResponse<ArrayList<Ingrediente>> sr=this.client.getIngredientiTipologia(tipologia);
			if(sr==null) this.parent.error(10, "Errore di connessione. Impossibile comunicare con il server");
			else {
				if(sr.isExc()) this.parent.error(sr.getErrorNumber(), sr.getExcMessage());
				else return sr.getCastObj();
			}	
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			this.parent.error(10, "Impossibile riempire l'elenco degli ingredienti");
		}
		return null;
	}
	
	public IngredientiJPanel(BigJTabbedPane parent, AnelloAromaticoHttpClient c) {
		this.client=c;
		this.parent=parent;
		this.setLayout(new BorderLayout());
		this.aggiungiIngJButton=new JButton("Aggiungi nuovo ingrediente");
		this.aggiungiIngJButton.setActionCommand("aggiungiNuovoIngrediente");
		this.eliminaIngJButton=new JButton("Elimina ingrediente");
		this.modificaIngJButton=new JButton("Modifica ingrediente");
		this.aggiungiIngJButton.addActionListener(this);
		this.eliminaIngJButton.addActionListener(this);
		this.modificaIngJButton.addActionListener(this);
		//Aggiungere le azioni dei bottoni
		
		//Preparo la parte alta della schermata: scelta della tipologia di ingrediente e aggiunta di un nuovo ingrediente
		this.sceltaTipologia=new JComboBox<String>(IngredientiJPanel.tipologie);
		this.sceltaTipologia.addActionListener(this);
		this.sceltaTipologia.setActionCommand("scegliTipologia");
		this.sceltaIngJScrollPane=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.sceltaIngJScrollPane.setVisible(true);
		this.nordPanel=new JPanel();
		this.nordPanel.setBorder(BorderFactory.createTitledBorder("nord"));
		this.nordPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.nordPanel.setLayout(new FlowLayout());
		this.nordPanel.add(this.sceltaTipologia);
		this.nordPanel.add(this.aggiungiIngJButton);
		this.add(nordPanel, BorderLayout.NORTH);
		this.centerPanel=new JPanel();
		this.add(centerPanel, BorderLayout.CENTER);
		this.centerPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.ingredienteJPanel=new IngredienteJPanel();
		
		//Preparo il panello scorrevole di scelta del singolo ingrediente
		this.sceltaIngJScrollPane.setBorder(BorderFactory.createTitledBorder("Elenco"));
		this.sceltaIngJScrollPane.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaIngJScrollPane.getViewport().setBackground(Color.RED);
		//this.sceltaIngJScrollPane.setBorder(BorderFactory.createLineBorder(Color.RED));
		//this.sceltaIngJScrollPane.getViewport().setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaIngJScrollPane.setPreferredSize(new Dimension(200, 200));
		this.sceltaIngJScrollPane.setMinimumSize(new Dimension(100, 200));
		this.sceltaIngJScrollPane.setMaximumSize(new Dimension(300, 400));
		//this.listaIngredienti=new ArrayList<Ingrediente>();
		
		this.riempiElenco(this.getIngredienti(this.sceltaTipologia.getSelectedIndex()));
		this.add(this.sceltaIngJScrollPane, BorderLayout.WEST);
		this.parent.parent.writeInArea("Seleziona una tipologia di ingrediente dal manù a tendina");
		this.setVisible(true);
	}

	/*Questo oggetto è ActionListener per la JCombo della tipologia,
	 * per la creazione del pannello di inserimento di un nuovo ingrediente e i JButton in esso contenuti
	 */
	
	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "scegliTipologia":
			//Attivato quando si vuole cambiare la tipoogia degli ingredienti presenti nello ScrollPane di sinistra
			this.parent.parent.writeInArea("Selezionata la tipologia "+IngredientiJPanel.tipologie[this.sceltaTipologia.getSelectedIndex()]);
			this.listaIngredienti=this.getIngredienti(this.sceltaTipologia.getSelectedIndex());
			if((this.listaIngredienti==null)||(this.listaIngredienti.size()==0)) {
				this.ingredienteJPanel.setVisible(false);
				this.centerPanel.removeAll();
				this.ingredienteJPanel=new IngredienteJPanel("Nessun ingrediente trovato per la tipologia specificata");
				this.centerPanel.add(this.ingredienteJPanel);
				this.centerPanel.setVisible(true);
				this.add(centerPanel, BorderLayout.CENTER);
			}else{
				this.ingredienteJPanel.setVisible(false);
				this.centerPanel.removeAll();
				this.ingredienteJPanel=new IngredienteJPanel("Seleziona un ingrediente dall'elenco a sinistra");
				this.centerPanel.add(this.ingredienteJPanel);
				this.centerPanel.setVisible(true);
				this.add(centerPanel, BorderLayout.CENTER);
			}
			this.riempiElenco(this.listaIngredienti);
			break;
		case "aggiungiNuovoIngrediente":
			//Attivato quando si vuole aggiungere un nuovo ingrediente. Cambia il contenuto del centerPanel
			this.centerPanel.setVisible(false);
			this.centerPanel.removeAll();
			this.ingredienteJPanel=new IngredienteJPanel(this);
			this.centerPanel=this.ingredienteJPanel;
			this.add(centerPanel, BorderLayout.CENTER);
			this.centerPanel.setVisible(true);
			this.parent.parent.writeInArea("Compila i campi e premi \"Conferma\" per aggiungere un nuovo ingrediente");
			break;
		case "annulla":
			//Attivato quando si vuole cancellae il pannello di modifica/inserimento. resetta il centerPanel
			this.centerPanel.setVisible(false);
			this.remove(centerPanel);
			this.ingredienteJPanel=new IngredienteJPanel("");
			this.centerPanel=this.ingredienteJPanel;
			this.add(centerPanel, BorderLayout.CENTER);
			this.centerPanel.setVisible(true);
			//this.parent.parent.writeInArea("Operazione annullata");
			break;
		case "confermaInserimento":
			//Attivato quando viene confermato l'inserimento di un nuovo ingrediente. Viene preso l'ingrediente corrente dall'IngredienteJPanel e utilizza il client per caricarlo sul database
			try {
				ServerResponse<Ingrediente> sr=parent.client.insertIngrediente(this.ingredienteJPanel.getIngrediente());
				if(sr!=null){
					if(sr.isExc()){
						JOptionPane.showMessageDialog(this, sr.getExcMessage(), String.valueOf(sr.getErrorNumber()), JOptionPane.ERROR_MESSAGE);
						parent.parent.writeInArea("Errore "+sr.getErrorNumber()+": "+sr.getExcMessage());
					}else{
						JOptionPane.showMessageDialog(this, "Inserito nuovo ingrediente con id ".concat(String.valueOf(sr.getCastObj().getId())), "Inserimento effettuato", JOptionPane.PLAIN_MESSAGE);
						parent.parent.writeInArea("Inserito nuovo "+sr.getCastObj().getTipologia()+" nel database con id "+sr.getCastObj().getId());
					}
				}
			} catch (beerException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			break;
		case "modifica":
			//Attivato quando si vogliono abilitare i campi per la modifica dell'ingrediente
			this.ingredienteJPanel.nomeJTextField.setEnabled(true);
			this.ingredienteJPanel.tipoJTextField.setEnabled(true);
			this.ingredienteJPanel.tipologiaJCombo.setEnabled(true);
			this.ingredienteJPanel.acidiJTextField.setEnabled(true);
			this.ingredienteJPanel.bustaJTextField.setEnabled(true);
			this.ingredienteJPanel.coloreJTextField.setEnabled(true);
			this.ingredienteJPanel.statoJCombo.setEnabled(true);
			this.ingredienteJPanel.noteJTextField.setEnabled(true);
			this.ingredienteJPanel.confermaJButton.setText("Salva");
			this.ingredienteJPanel.confermaJButton.setActionCommand("confermaModifica");
			break;
		case "eliminaIngrediente":
			//Attivato quando si vuole eliminare l'ingrediente visualizzato dal database
			int id;
			System.out.println("Eccoci");
			try {	
				id = this.ingredienteJPanel.getIngrediente().getId();
				int result=JOptionPane.showConfirmDialog(this, "Sicuro di voler eliminare l'ingrediente ".concat(String.valueOf(id)), "Conferma eliminazione", JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.YES_OPTION){
					ServerResponse<Boolean> sr1=parent.client.eliminaIngrediente(id);
					if(sr1!=null){
						if(sr1.isExc()){
							JOptionPane.showMessageDialog(this, sr1.getExcMessage(), String.valueOf(sr1.getErrorNumber()), JOptionPane.ERROR_MESSAGE);
							parent.parent.writeInArea("Errore "+sr1.getErrorNumber()+": "+sr1.getExcMessage());
						}else{
							JOptionPane.showMessageDialog(this, "Eliminato ingrediente con id ".concat(String.valueOf(id)), "Modifiche effettuate", JOptionPane.PLAIN_MESSAGE);
							parent.parent.writeInArea("Ingrediente "+id+" eliminato!!");
						}
					}
				}else{
					this.parent.parent.writeInArea("Annullato");
				}
			} catch (beerException e2) {
				JOptionPane.showMessageDialog(this, e2.getMessage(), e2.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			this.ingredienteJPanel.annullaJButton.doClick();
			break;
		case "confermaModifica":
			//Attivato quando si vuole salvare le modifiche apportare ad un ingrediente sul database
			try {
				ServerResponse<Boolean> sr2=parent.client.updateIngrediente(this.ingredienteJPanel.getIngrediente());
				if(sr2!=null){
					if(sr2.isExc()){
						JOptionPane.showMessageDialog(this, sr2.getExcMessage(), String.valueOf(sr2.getErrorNumber()), JOptionPane.ERROR_MESSAGE);
						parent.parent.writeInArea("Errore "+sr2.getErrorNumber()+": "+sr2.getExcMessage());
					}else{
						JOptionPane.showMessageDialog(this, "Modificato ingrediente con id ".concat(String.valueOf(this.ingredienteJPanel.getIngrediente().getId())), "Modifiche effettuate", JOptionPane.PLAIN_MESSAGE);
						parent.parent.writeInArea("Ingrediente "+this.ingredienteJPanel.getIngrediente().getId()+" modificato!!");
					}
				}
			} catch (beerException e1) {
				JOptionPane.showMessageDialog(this, e1.getMessage(), e1.getMessage(), JOptionPane.ERROR_MESSAGE);
			}
			this.ingredienteJPanel.nomeJTextField.setEnabled(false);
			this.ingredienteJPanel.tipoJTextField.setEnabled(false);
			this.ingredienteJPanel.noteJTextField.setEnabled(false);
			this.ingredienteJPanel.statoJCombo.setEnabled(false);
			this.ingredienteJPanel.tipologiaJCombo.setEnabled(false);
			this.ingredienteJPanel.acidiJTextField.setEnabled(false);
			this.ingredienteJPanel.bustaJTextField.setDragEnabled(false);
			this.ingredienteJPanel.coloreJTextField.setEnabled(false);
			break;
		default: 
			if(e.getActionCommand().startsWith("modificaIngrediente")){
				int num;
				num=Integer.valueOf(e.getActionCommand().substring(e.getActionCommand().length()-1));
				System.out.println(e.getActionCommand());
				System.out.println(num);
				this.centerPanel.setVisible(false);
				this.centerPanel.removeAll();
				this.ingredienteJPanel=new IngredienteJPanel(this, this.listaIngredienti.get(num));
				this.centerPanel=this.ingredienteJPanel;
				this.add(centerPanel, BorderLayout.CENTER);
				this.centerPanel.setVisible(true);
				this.parent.parent.writeInArea("Clicca su \"Modifica\" per modificare i parametri dell'ingrediente");
				break;
			}
			break;
		}
	}

}
