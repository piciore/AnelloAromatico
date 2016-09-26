package visualClient;

import java.awt.Dimension;
import java.io.UnsupportedEncodingException;
import javax.swing.JFrame;

public class AnelloAromaticoVisualClient extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected AnelloAromaticoHttpClient client;
	protected MainPanel mainPanel;

	public void logout(){
		System.out.println("Entro nel logout");
		this.client.logout();
		mainPanel.setVisible(false);
		mainPanel=new MainPanel(this);
		this.setContentPane(mainPanel);
		mainPanel.writeInArea("Utente disconnesso");
		mainPanel.writeInArea("Inserire username e password per loggarsi nel sistema");
	}
	
	public AnelloAromaticoHttpClient getHttpClient(){
		return this.client;
	}
	
	public AnelloAromaticoVisualClient() throws UnsupportedEncodingException {
		super();
		this.client=new AnelloAromaticoHttpClient();
		JFrame.setDefaultLookAndFeelDecorated(true);
		setTitle("AnelloAromatico Client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mainPanel=new MainPanel(this);
		this.setContentPane(mainPanel);
		mainPanel.writeInArea("Inserire username e password per loggarsi nel sistema");
		setPreferredSize(new Dimension(800, 450));
		setMinimumSize(new Dimension(600,400));
		pack();
		setVisible(true);
	}

	
	public static void main(String[] args) {
		try {
			AnelloAromaticoVisualClient mainWindow=new AnelloAromaticoVisualClient();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
	}
}
