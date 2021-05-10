package client.startmenu;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class ConnectServer extends JFrame
{
	private JLabel serveur; 
	private JTextField rentrerServeur; 
	private JPanel panelServeur;
	private JPanel panelServeurPseudo;
	private JButton connecterServeur;
	
	private DebutJeu callback;
	
	public ConnectServer(DebutJeu callback)
	{
		this.callback = callback;
		
		serveur= new JLabel ("Adresse du serveur et porte : (192.168.27.7:8000)" ); // etiquette permettant de rentrer le serveur que le joueur veut rejoindre
		serveur.setBackground (new Color(255,209,53) );
		serveur.setOpaque(true);
		serveur.setHorizontalAlignment(SwingConstants.CENTER); 
		serveur.setVerticalAlignment(SwingConstants.CENTER); 
		serveur.setForeground(Color.RED);
		
		panelServeur= new JPanel(); // Panel FlowLayout pour afficher a la suite l'etiquette et l'entree du serveur
		panelServeur.setBackground (Color.WHITE);
		rentrerServeur = new JTextField ("",30);
		rentrerServeur.setText("localhost:8000");
		rentrerServeur.setHorizontalAlignment(JTextField.CENTER);

		
		connecterServeur = new JButton ("Connecter !"); // Bouton pour creer le serveur
		connecterServeur.addActionListener(new EcouteurConnecterAuServeur(this));
		connecterServeur.setBackground (new Color(255,209,53) );
		connecterServeur.setOpaque(true);
		connecterServeur.setBorderPainted(false);
		connecterServeur.setForeground(Color.RED);
		
		panelServeur.add(serveur); 
		panelServeur.add(rentrerServeur);
		panelServeur.add(connecterServeur);
		 
		panelServeurPseudo = new JPanel (new BorderLayout()); // Panel permettannt de mettre au centre ensemble l'entree du pseudo et du serveur
		
		panelServeurPseudo.add(panelServeur, BorderLayout.CENTER);
		
		add(panelServeurPseudo);
		
		setSize(300,125);
		setLocationRelativeTo(null);
		setVisible(true);
	}
	
	public void connecterAuServeur()
	{
		callback.connecteeServeur(rentrerServeur.getText());
		setVisible(false);
		dispose();
	}
	
}
