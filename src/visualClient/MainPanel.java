package visualClient;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.ScrollPane;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.RootPaneContainer;
import javax.swing.ScrollPaneConstants;
import javax.swing.ScrollPaneLayout;
import javax.swing.text.DefaultCaret;

import anelloAromaticoServerPack.Utente;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;

public class MainPanel extends JPanel implements ActionListener{
	AnelloAromaticoVisualClient parent;
	LoginPanel loginPanel;
	JPanel sudPanel;
	StateJTextArea stateJTextArea;
	JScrollPane textAreaJScrollPane;
	protected Utente utenteLoggato;

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public void loginEffettuato(Utente u){
		this.utenteLoggato=u;
		this.stateJTextArea.insertLine("Accesso effettuato come "+u.getNome()+" "+u.getCognome());
		loginPanel.setVisible(false);
		this.remove(loginPanel);
		JButton logoutJButton=new JButton("Logout");
		logoutJButton.setActionCommand("logout");
		logoutJButton.addActionListener(this);
		logoutJButton.setPreferredSize(new Dimension(100, 30));
		logoutJButton.setMaximumSize(new Dimension(40, 20));
		logoutJButton.setVisible(true);
		this.sudPanel.add(logoutJButton);
		
		BigJTabbedPane main=new BigJTabbedPane(this);
		main.setClient(parent.getHttpClient());
		this.add(main);		
		this.setBackground(Color.getHSBColor(62, 75, 68));
		this.revalidate();
		setVisible(true);
	}
	public void writeError(int num, String s){
		this.stateJTextArea.insertLine("Error "+num+": "+s);
	}
	public void writeInArea(String s){
		this.stateJTextArea.insertLine(s);
	}
	
	public MainPanel(AnelloAromaticoVisualClient parent)  {
		this.setLayout(new BorderLayout());
		this.parent=parent;
		this.loginPanel=new LoginPanel(parent.client, this);
		this.add(loginPanel);
		
		this.sudPanel=new JPanel();
		sudPanel.setLayout(new FlowLayout());
		
		this.stateJTextArea=new StateJTextArea(2, 50);
		this.stateJTextArea.setMaximumSize(new Dimension(300, 50));
		this.stateJTextArea.setAutoscrolls(true);
		this.stateJTextArea.setVisible(true);
		
			this.textAreaJScrollPane=new JScrollPane();
			this.textAreaJScrollPane.setPreferredSize(new Dimension(550, 60));
			this.textAreaJScrollPane.getViewport().add(this.stateJTextArea);
			DefaultCaret caret = (DefaultCaret)this.stateJTextArea.getCaret();
			caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
			this.textAreaJScrollPane.setVisible(true);
			textAreaJScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
		
		sudPanel.add(this.textAreaJScrollPane);
		sudPanel.setVisible(true);
		
		this.add(sudPanel, BorderLayout.SOUTH);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
		case "logout": parent.logout(); break;
		}
	}

}
