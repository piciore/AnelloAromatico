package visualClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import beer.Ingrediente;
import beer.Ricetta;
import beer.beerException;

public class RicetteJPanel extends JPanel implements ActionListener{
	private static final int defaultHue=63;
	private static final int defaultSaturation=54;
	private static final int defaultBrightness=100;
	protected BigJTabbedPane parent;
	protected JScrollPane elencoRicetteJScrollPane;
	protected JPanel cercaJPanel;
	protected JTextField cercaJTextField;
	protected JPanel westJPanel;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private void riempiElenco(ArrayList<Ricetta> lista){
		//Creo un pannelloLista con tante righe quante gli ingredienti
		//Per ogni ingrediente creo un pannello riga contenente nome, tipo e freccia
		if(lista==null) lista=new ArrayList<Ricetta>();
		JPanel pannelloLista=new JPanel();
		if(lista.size()>10)	pannelloLista.setLayout(new GridLayout(lista.size(), 1));
		else pannelloLista.setLayout(new GridLayout(10, 1));
		if(lista.size()==0){
			JLabel zeroJLabel=new JLabel("Non ci sono ricette da visualizzare");
			zeroJLabel.setVisible(true);
			pannelloLista.add(zeroJLabel);
		}
		//Icon icon=new ImageIcon(getClass().getResource("frecciaDestraSmall.jpg"));
		for(int i=0; i<lista.size(); i++){
			JPanel riga=new JPanel();
			riga.setLayout(new BorderLayout());
			riga.setBorder(BorderFactory.createLineBorder(Color.BLACK));
			riga.setPreferredSize(new Dimension(0, 25));
			
			JLabel ingrediente= new JLabel(lista.get(i).getNome()+"-"+lista.get(i).getStile());
			ingrediente.setBorder(BorderFactory.createEmptyBorder(1, 2, 0, 1));
			riga.add(ingrediente);
			
			JButton freccia=new JButton(/*icon*/);
			freccia.setPreferredSize(new Dimension(20, 20));
			riga.add(freccia, BorderLayout.EAST);
			freccia.setActionCommand("modificaRicetta".concat(String.valueOf(i)));
			freccia.addActionListener(this);
			pannelloLista.add(riga);
		}
		pannelloLista.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.elencoRicetteJScrollPane.getViewport().add(pannelloLista);
		this.elencoRicetteJScrollPane.validate();
	}
	
	public RicetteJPanel(BigJTabbedPane parent){
		this.parent=parent;
		this.setLayout(new BorderLayout());
		this.westJPanel=new JPanel();
		this.westJPanel.setLayout(new BorderLayout());
		this.cercaJTextField=new JTextField();
		this.cercaJTextField.setText("Prova");
		this.cercaJPanel=new JPanel();
		this.cercaJPanel.add(this.cercaJTextField, BorderLayout.WEST);
		this.westJPanel.add(this.cercaJPanel, BorderLayout.NORTH);
		this.elencoRicetteJScrollPane=new JScrollPane();
		this.elencoRicetteJScrollPane.setPreferredSize(new Dimension(200, 100));
		this.elencoRicetteJScrollPane.setBorder(BorderFactory.createLineBorder(Color.GREEN));
		//this.add(this.elencoRicetteJScrollPane, BorderLayout.WEST);
		this.parent.parent.writeInArea("Seleziona la ricetta dall'elenco di sinistra");
		this.setBackground(Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		try {
			Ricetta r1 = new Ricetta("tra");
			Ricetta r2=new Ricetta("tra1");
			Ricetta r3=new Ricetta("tra1");
			ArrayList<Ricetta> listaRicette=new ArrayList<Ricetta>();
			listaRicette.add(r1);
			listaRicette.add(r2);
			listaRicette.add(r3);
			this.riempiElenco(listaRicette);
		} catch (beerException e) {
			e.printStackTrace();
		}
		this.westJPanel.add(this.elencoRicetteJScrollPane);
		this.add(this.westJPanel, BorderLayout.WEST);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		
	}

}
