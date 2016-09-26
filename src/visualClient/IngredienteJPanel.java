package visualClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import anelloAromaticoServerPack.dbException;
import beer.Ingrediente;
import beer.Lievito;
import beer.Luppolo;
import beer.Malto;
import beer.beerException;

public class IngredienteJPanel extends JPanel implements ActionListener{
	private static final int defaultHue=63;
	private static final int defaultSaturation=54;
	private static final int defaultBrightness=100;
	private static final int larghezzaJButton=85;
	private static final int altezzaJButton=25;
	protected static final String[] tipologie={"Malto", "Lievito", "Luppolo", "Spezia", "Altro"};
	protected static final String[] stati={"Liquido", "Solido"};
	protected static final Dimension dimensioneCampi=new Dimension(200, 10);
	protected static final Dimension dimensioneScelte=new Dimension(80, 20);
	protected JLabel nomeJLabel=new JLabel("Nome");
	protected JLabel tipoJLabel=new JLabel("Tipo (id)");
	protected JLabel tipologiaJLabel=new JLabel("Tipologia");
	protected JLabel coloreJLabel=new JLabel("Colore EBC");
	protected JLabel acidiJLabel=new JLabel("Alfa acidi");
	protected JLabel statoJLabel=new JLabel("Stato");
	protected JLabel bustaJLabel=new JLabel("Quantità busta");
	protected JLabel mlJLabel=new JLabel("ml");
	protected JLabel gJLabel=new JLabel("g");
	protected JLabel noteJLabel=new JLabel("Note");
	protected JTextField nomeJTextField=new JTextField();
	protected JTextField tipoJTextField=new JTextField();
	protected JComboBox<String> tipologiaJCombo=new JComboBox<String>(IngredienteJPanel.tipologie);
	protected JTextField coloreJTextField=new JTextField();
	protected JTextField acidiJTextField=new JTextField();
	protected JComboBox<String> statoJCombo=new JComboBox<String>(IngredienteJPanel.stati);
	protected JTextField bustaJTextField=new JTextField();
	protected JTextField noteJTextField=new JTextField();
	protected JPanel specificiJPanel=new JPanel();
	protected JPanel sceltaMaltoJPanel=new JPanel();
	protected JPanel sceltaLievitoJPanel=new JPanel();
	protected JPanel sceltaLuppoloJPanel=new JPanel();
	protected JPanel sceltaAltroJPanel=new JPanel();
	protected JPanel bottoniJPanel=new JPanel();
	protected JButton confermaJButton=new JButton("Conferma");
	protected JButton annullaJButton=new JButton("Annulla");
	protected JButton eliminaJButton=new JButton("Elimina");
	protected IngredientiJPanel parent;
	protected Ingrediente ingCorrente;
	
	public IngredienteJPanel(){
		super();
	}
	
	/**
	 * Costruttore del pannello contenente un etichetta recante il messaggio passato come parametro
	 * @param messaggio
	 */
	public IngredienteJPanel(String messaggio) {
		super();
		this.removeAll();
		JLabel messaggioJLabel=new JLabel(messaggio);
		this.add(messaggioJLabel);
		this.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.setVisible(true);
		this.ingCorrente=null;
	}
	/**
	 * Costruttore del pannello contenente la maschera dei dati dell'ingrediente passato,
	 * con i vari campi riempiti con i valori dell'ingrediente.
	 * 
	 * @param parent contenitore che ascolta i bottoni Annulla, Modifica ed Elimina
	 * @param ingrediente
	 */
	public IngredienteJPanel(IngredientiJPanel parent, Ingrediente ingrediente){
		this(parent);
		this.ingCorrente=ingrediente;
		//cambio il titolo del bordo
		this.setBorder(BorderFactory.createTitledBorder("Ingrediente "+ingrediente.getId()));
		//Riempio i campi con i valori dell'ingrediente e disabilito la modifica
		this.nomeJTextField.setText(ingrediente.getNome());
		this.nomeJTextField.setEnabled(false);
		this.tipoJTextField.setText(ingrediente.getTipo());
		this.tipoJTextField.setEnabled(false);
		//Riempio il pannello dei campi specifici a seconda della tipologia dell'ingrediente
		int index=0;
		if(ingrediente instanceof Malto){
			index=0;
			this.specificiJPanel.removeAll();
			this.specificiJPanel.add(this.sceltaMaltoJPanel); 
			this.coloreJTextField.setText(String.valueOf(((Malto) ingrediente).getColore_ebc()));
			this.coloreJTextField.setEnabled(false);
		}else if(ingrediente instanceof Lievito){
			index=1;
			this.specificiJPanel.removeAll();
			this.specificiJPanel.add(this.sceltaLievitoJPanel);
			if(((Lievito) ingrediente).getStato_materiale().equals("liquido")){
				this.statoJCombo.setSelectedIndex(0);
			}else{
				this.statoJCombo.setSelectedIndex(1);
			}
			this.bustaJTextField.setEnabled(false);
			this.bustaJTextField.setText(String.valueOf(((Lievito) ingrediente).getQuantità_busta()));
		}else if(ingrediente instanceof Luppolo){
			index=2;
			this.specificiJPanel.removeAll();
			this.specificiJPanel.add(this.sceltaLuppoloJPanel);
			this.acidiJTextField.setText(String.valueOf(((Luppolo) ingrediente).getAlfa_acidi()));
			this.acidiJTextField.setEditable(false);
		}else {
			this.specificiJPanel.removeAll();
			this.specificiJPanel.add(this.sceltaAltroJPanel);
		}
		//Riempio gli ultimi campi
		this.tipologiaJCombo.setSelectedIndex(index);
		this.tipologiaJCombo.setEnabled(false);
		this.statoJCombo.setEnabled(false);
		this.noteJTextField.setText(ingrediente.getNote());
		this.noteJTextField.setEnabled(false);
		//Aggiungo i bottoni di Modifica ed eliminazione (Annulla è gia presente nella chiamata al costruttore del pannello)
		this.confermaJButton.setText("Modifica");
		this.confermaJButton.setActionCommand("modifica");
		this.eliminaJButton.addActionListener(parent);
		this.eliminaJButton.setActionCommand("eliminaIngrediente");
		this.eliminaJButton.setPreferredSize(new Dimension(larghezzaJButton, altezzaJButton));
		this.bottoniJPanel.add(this.eliminaJButton);
	}

	/**
	 * Costruttore del pannello contenente la maschera dati di un ingrediente da creare
	 * @param parent contenitore che ascolta i bottoni Annulla e Conferma
	 */
	public IngredienteJPanel(IngredientiJPanel parent){
		this.ingCorrente=null;
		this.parent=parent;
		//Aggiunta del bordo
		this.setBorder(BorderFactory.createTitledBorder("Aggiungi nuovo ingrediente"));
		this.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		GridLayout l=new GridLayout(6, 2);
		this.setLayout(l);
		l.setVgap(8);
		l.setHgap(5);
		
		//Nome
		this.add(this.nomeJLabel); this.add(this.nomeJTextField);
		nomeJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		//Tipo
		this.add(this.tipoJLabel); this.add(this.tipoJTextField);
		tipoJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		//Tipologia
		this.add(this.tipologiaJLabel);
		this.add(this.tipologiaJCombo);
		this.tipologiaJCombo.addActionListener(this); 
		this.tipologiaJCombo.setActionCommand("scegliTipologia");
		tipologiaJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		//Campi a seconda della tipologia
		
		this.sceltaLievitoJPanel.add(this.statoJLabel); this.sceltaLievitoJPanel.add(this.statoJCombo); 
		this.statoJCombo.setPreferredSize(IngredienteJPanel.dimensioneScelte); this.statoJCombo.setActionCommand("scegliStato"); this.statoJCombo.addActionListener(this);
		this.sceltaLievitoJPanel.add(this.mlJLabel); this.mlJLabel.setVisible(true); this.sceltaLievitoJPanel.add(this.gJLabel); this.gJLabel.setVisible(false);
		this.sceltaLievitoJPanel.add(this.bustaJLabel); this.sceltaLievitoJPanel.add(this.bustaJTextField); this.bustaJTextField.setColumns(3);
		this.sceltaLievitoJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaMaltoJPanel.add(this.coloreJLabel); this.sceltaMaltoJPanel.add(this.coloreJTextField); this.coloreJTextField.setColumns(3);
		this.sceltaMaltoJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaLuppoloJPanel.add(this.acidiJLabel); this.sceltaLuppoloJPanel.add(this.acidiJTextField); this.acidiJTextField.setColumns(3);
		this.sceltaLuppoloJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.sceltaAltroJPanel.add(new JLabel("Non ci sono opzioni aggiuntive"));
		this.sceltaAltroJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.specificiJPanel=new JPanel();
		this.specificiJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.specificiJPanel.add(this.sceltaMaltoJPanel);
		
		this.add(new JLabel()); this.add(specificiJPanel);
		//Note
		this.add(this.noteJLabel); this.add(this.noteJTextField);
		noteJLabel.setHorizontalAlignment(SwingConstants.RIGHT);
		//Bottoni
		bottoniJPanel=new JPanel();
		bottoniJPanel.setLayout(new FlowLayout());
		bottoniJPanel.add(this.annullaJButton); 
		bottoniJPanel.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		
		this.annullaJButton.setActionCommand("annulla");
		this.annullaJButton.addActionListener(parent);
		this.annullaJButton.setPreferredSize(new Dimension(larghezzaJButton, altezzaJButton));
		bottoniJPanel.add(this.confermaJButton);
		this.confermaJButton.setActionCommand("confermaInserimento");
		this.confermaJButton.addActionListener(parent);
		this.confermaJButton.setPreferredSize(new Dimension(larghezzaJButton, altezzaJButton));
		this.add(new JLabel()); this.add(bottoniJPanel);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		System.out.println("Si sta scegliendo la tipologia");
		switch(e.getActionCommand()){
		case "scegliTipologia": 
			switch(this.tipologiaJCombo.getSelectedIndex()){
			case 0:
				this.specificiJPanel.setVisible(false);
				//this.specificiJPanel.removeAll();
				this.specificiJPanel.add(this.sceltaMaltoJPanel);
				this.specificiJPanel.setVisible(true); break;
			case 1:
				this.specificiJPanel.setVisible(false);
				//this.specificiJPanel.removeAll();
				this.specificiJPanel.add(this.sceltaLievitoJPanel);
				this.specificiJPanel.setVisible(true); break;
			case 2:
				this.specificiJPanel.setVisible(false);
				//this.specificiJPanel.removeAll();
				this.specificiJPanel.add(this.sceltaLuppoloJPanel);
				this.specificiJPanel.setVisible(true); break;
			case 3:
				this.specificiJPanel.setVisible(false);
				//this.specificiJPanel.removeAll();
				this.specificiJPanel.add(this.sceltaAltroJPanel);
				this.specificiJPanel.setVisible(true); break;
			default: break;
			}
			break;
		case "scegliStato":
			this.mlJLabel.setVisible(!this.mlJLabel.isVisible());
			this.gJLabel.setVisible(!this.gJLabel.isVisible());
			break;
		default: break;
		}
	}

	public Ingrediente getIngrediente() throws beerException{
		Ingrediente ing=new Ingrediente(
				this.nomeJTextField.getText(), 
				this.tipoJTextField.getText(), 
				IngredienteJPanel.tipologie[this.tipologiaJCombo.getSelectedIndex()].toLowerCase(), 
				this.noteJTextField.getText());
		try{
			ing.setId(this.ingCorrente.getId());
			this.ingCorrente=ing;
			switch(ing.getTipologia().toLowerCase()){
			case "malto": 
				if(this.coloreJTextField.getText().equals("")) return ing.toMalto(); 
				else return ing.toMalto(Integer.valueOf(this.coloreJTextField.getText()));
			case "lievito": 
				if(this.bustaJTextField.getText().equals("")) return ing.toLievito();
				return ing.toLievito(IngredienteJPanel.stati[this.statoJCombo.getSelectedIndex()].toLowerCase(), Double.valueOf(this.bustaJTextField.getText()));
			case "luppolo": 
				if(this.acidiJTextField.getText().equals("")) return ing.toLuppolo();
				return ing.toLuppolo(Double.valueOf(this.acidiJTextField.getText()));
			default: return ing;
			}
		}catch(NumberFormatException e){
			throw new beerException("Inserito un valore non numerico");
		} catch (dbException e) {
			throw new beerException("Errore nell'id");
		}
		
	}
}
