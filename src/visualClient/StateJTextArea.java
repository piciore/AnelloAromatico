package visualClient;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

public class StateJTextArea extends JTextArea {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public StateJTextArea(int rows, int columns) {
		super(rows, columns);
		this.setBackground(Color.getHSBColor(62, 75, 68));
		this.setEditable(false);
		this.setLineWrap(true);
		this.setWrapStyleWord(true);
		this.setAutoscrolls(true);
	}
	public void insertLine(String s){
		this.append(s.concat("\n"));
	}
}
