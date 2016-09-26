package visualClient;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import anelloAromaticoServerPack.ServerResponse;
import anelloAromaticoServerPack.Utente;
import beer.Ingrediente;
import beer.Malto;

public class LoginPanel extends JPanel implements ActionListener{
	protected static final int marginiRiga=5;
	protected static final int marginiForm=20;
	protected static final int larghezzaCampi=15;
	protected JTextField userJTextField;
	protected JPasswordField passJPasswordField;
	protected JButton confermaLogin;
	protected JButton annullaLogin;
	protected JButton provaLogin;
	protected AnelloAromaticoHttpClient client;
	protected MainPanel parent;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public LoginPanel(AnelloAromaticoHttpClient main, MainPanel parent){
		this.client=main;
		this.parent=parent;
		this.setBorder(BorderFactory.createEmptyBorder(marginiForm, marginiForm, marginiForm, marginiForm));
		this.setBackground(Color.getHSBColor(62, 75, 68));
		JPanel formPanel=new JPanel();
		formPanel.setOpaque(false);
		formPanel.setBorder(BorderFactory.createTitledBorder("Login"));
		formPanel.setPreferredSize(new Dimension(300, 250));
		formPanel.setLayout(new GridLayout(5,1));
		
		JPanel riga1=new JPanel();
		JLabel userJLabel=new JLabel("Username", JLabel.CENTER);
		userJLabel.setText("Username ");
		riga1.add(userJLabel);
		riga1.setOpaque(false);
		riga1.setBorder(BorderFactory.createEmptyBorder(marginiRiga, marginiRiga, marginiRiga, marginiRiga));
		formPanel.add(riga1);
		
		JPanel riga2=new JPanel();
		userJTextField=new JTextField(larghezzaCampi);
		userJTextField.addActionListener(this);
		userJTextField.setText("piciore");
		riga2.add(userJTextField);
		riga2.setOpaque(false);
		riga2.setBorder(BorderFactory.createEmptyBorder(marginiRiga, marginiRiga, marginiRiga, marginiRiga));
		formPanel.add(riga2);
		
		JPanel riga3=new JPanel();
		JLabel passJLabel=new JLabel("Password", JLabel.CENTER);
		riga3.add(passJLabel);
		riga3.setOpaque(false);
		riga3.setBorder(BorderFactory.createEmptyBorder(marginiRiga, marginiRiga, marginiRiga, marginiRiga));
		formPanel.add(riga3);
		
		JPanel riga4=new JPanel();
		passJPasswordField=new JPasswordField(larghezzaCampi);
		passJPasswordField.addActionListener(this);
		passJPasswordField.setText("lasolita");
		riga4.add(passJPasswordField);
		riga4.setOpaque(false);
		riga4.setBorder(BorderFactory.createEmptyBorder(marginiRiga, marginiRiga, marginiRiga, marginiRiga));
		formPanel.add(riga4);
		
		JPanel riga5=new JPanel();
		riga5.setBorder(BorderFactory.createEmptyBorder(marginiRiga, marginiRiga, marginiRiga, marginiRiga));
		confermaLogin=new JButton("Login");
		confermaLogin.setActionCommand("confermalogin");
		confermaLogin.addActionListener(this);
		annullaLogin=new JButton("Cancella");
		annullaLogin.addActionListener(this);
		provaLogin=new JButton("Prova");
		provaLogin.addActionListener(this);
		riga5.add(confermaLogin);
		riga5.add(annullaLogin);
		riga5.add(provaLogin);
		riga5.setOpaque(false);
		formPanel.add(riga5);
		
		this.add(formPanel);
		formPanel.setVisible(true);
		this.setVisible(true);
	}

	private void effettuaLogin(){
		ServerResponse<Utente> sr=client.Login(this.userJTextField.getText(), 
				String.valueOf(this.passJPasswordField.getPassword()));
		if(sr==null) {
			this.parent.writeError(3, "Problema di connessione. Impossibile comunicare con il server");
			return;
		}
		if(sr.isExc()){
			this.parent.writeError(sr.getErrorNumber(), sr.getExcMessage());
		}else{
			this.parent.loginEffettuato(sr.getCastObj());
		}
	}
	
	private void prendiIng() throws UnsupportedEncodingException{
		ServerResponse<ArrayList<Ingrediente>> sr=client.getIngredientiTipologia("malto");
		
		if(sr.isExc()){
			parent.writeError(sr.getErrorNumber(), sr.getExcMessage());
			System.out.println("Eccezione");
			System.out.println(sr.getErrorNumber()+"-"+sr.getExcMessage());
		}else{
			for(int i=0; i<sr.getCastObj().size(); i++){
				sr.getCastObj().get(i).stampa();
			}
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		//Se l'utente preme "Annulla" resetto tutti i campi
		if(e.getSource().equals(annullaLogin)){
			this.userJTextField.setText("");
			this.passJPasswordField.setText("");
		}else if(e.getSource().equals(provaLogin)){
			try {
				this.prendiIng();
			} catch (UnsupportedEncodingException e1) {
				e1.printStackTrace();
			}
		}
		else //Se l'utente preme "Login", effettuo una richiesta di login che restituirà una ServerResponse
			if((e.getSource().equals(confermaLogin))||(e.getSource().equals(passJPasswordField)))this.effettuaLogin();	
	}
}
