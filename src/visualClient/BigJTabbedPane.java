package visualClient;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JTabbedPane;

import javafx.scene.paint.Color;

public class BigJTabbedPane extends JTabbedPane {
	private static final int defaultHue=63;
	private static final int defaultSaturation=54;
	private static final int defaultBrightness=100;
	protected AnelloAromaticoHttpClient client;
	protected MainPanel parent;

	public void setClient(AnelloAromaticoHttpClient c){
		this.client=c;
	}
	private void creazione(MainPanel parent){
		this.parent=parent;
		this.setClient(parent.parent.client);
		IngredientiJPanel ingredientiJPanel=new IngredientiJPanel(this, this.client);
		ingredientiJPanel.setBackground(java.awt.Color.getHSBColor(defaultHue, defaultSaturation, defaultBrightness));
		this.add("Ingredienti", ingredientiJPanel);
		RicetteJPanel ricetteJPanel=new RicetteJPanel(this);
		this.add("Ricette", ricetteJPanel);
		CatStyleJPanel catstyleJPanel=new CatStyleJPanel();
		this.add("Categorie e stili", catstyleJPanel);
		CotteJPanel cotteJPanel=new CotteJPanel();
		this.add("Cotte", cotteJPanel);
	}
	public void error(int num, String s){
		this.parent.writeError(num, s);
	}
	
	public BigJTabbedPane(MainPanel parent) {
		this.creazione(parent);
	}

	public BigJTabbedPane(MainPanel parent, int arg0) {
		super(arg0);
		this.creazione(parent);
	}

	public BigJTabbedPane(MainPanel parent, int arg0, int arg1) {
		super(arg0, arg1);
		this.creazione(parent);
	}

}
