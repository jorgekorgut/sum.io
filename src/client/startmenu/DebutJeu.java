package client.startmenu;

import javax.swing.*;

import client.MainClient;
import client.engine.ImageCache;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.*;
import java.io.IOException;

public class DebutJeu extends JFrame{
	
	/* Les boutons et label de l'interface*/
	private JLabel bienvenue; 
	private JButton droite;
	private JButton gauche; 
	private JLabel pseudo;
	private JTextField rentrerPseudo;
	private JButton creerServeur;
	private JButton connecterServeur;

	
	/* Les Panel de l'interface pour la disposition des elements*/
	private JPanel panelCentre;
	private JPanel lancerJeu;
	private JPanel panelInter;
	private JPanel panelPseudo;
	private JPanel panelImage; 

	
	/*Les images permettant de choisir son personnage*/
	private JLabel personnage;
	private BufferedImage diablo;
	private BufferedImage Mexicano;
	private BufferedImage minion;
	private BufferedImage bob;
	private BufferedImage logo;
	private int currentImage=0;
	private BufferedImage [] index; 
	
	private MainClient callback;
	
	public DebutJeu(MainClient callback){
		
		this.callback = callback;
		
		lancerJeu = new JPanel(new BorderLayout()); //panel principal
		lancerJeu.setBackground (Color.WHITE);
		
		// Code des images
		diablo = callback.getImageCache().getImageMap().get("devil");
		bob = callback.getImageCache().getImageMap().get("bob");
		Mexicano = callback.getImageCache().getImageMap().get("mexicano");
		minion = callback.getImageCache().getImageMap().get("minion");
		logo =  callback.getImageCache().getImageMap().get("SUM.IO");
		
this.index= new BufferedImage[]{diablo, bob, Mexicano, minion}; // creation d'un tableau d'image pour travailler avec les positions
		
		panelImage= new JPanel (); //panel pour les images de personnages
		panelImage.setOpaque(false);
		personnage = new JLabel(new ImageIcon(diablo));
		panelImage.add(personnage);
		
		
		
		panelPseudo = new JPanel (); // panel pour le choix du pseudo
		panelPseudo.setBackground (Color.WHITE);
		
		pseudo = new JLabel (" Pseudo :");  // etiquette permettant de rentrer son pseudo
		pseudo.setBackground (new Color(255,209,53) );
		pseudo.setOpaque(true);
		pseudo.setHorizontalAlignment(SwingConstants.CENTER); 
		pseudo.setVerticalAlignment(SwingConstants.CENTER); 
		pseudo.setForeground(Color.RED);
		rentrerPseudo = new JTextField("",10);
		
		gauche = new JButton ("<"); // Bouton permettant de se deplacer a gauche dans le tableau d'image
		gauche.setBackground (new Color(255,209,53) );
		gauche.setOpaque(true);
		gauche.setBorderPainted(false);
		gauche.setForeground(Color.RED);

		droite = new JButton (">"); // Bouton permettant de se deplacer a droite dans le tableau d'image
		droite.setBackground (new Color(255,209,53) );
		droite.setOpaque(true);
		droite.setBorderPainted(false);
		droite.setForeground(Color.RED);
		
		droite.addActionListener(new EcouteurDroite(this)); // ActionListener liant les methodes et les boutons
		gauche.addActionListener(new EcouteurGauche(this));
		
		panelInter = new JPanel();	 // panel pour la disposition des boutons droite et gauche
		panelInter.setBackground (Color.WHITE);
		panelInter.add(gauche);
		panelInter.add(panelImage);
		panelInter.add(droite);
		panelPseudo.add(pseudo); // Panel FlowLayout pour afficher a la suite l'etiquette et l'entree du pseudo
		panelPseudo.add(rentrerPseudo);
		

		
		bienvenue = new JLabel (new ImageIcon (logo)); // Etiquette de bienvenu sur le jeu
		bienvenue.setHorizontalAlignment(SwingConstants.CENTER);
		
		creerServeur = new JButton ("Creer un Serveur !"); // Bouton pour creer le serveur
		creerServeur.addActionListener(new EcouteurCreerServeur(this));
		creerServeur.setBackground (new Color(255,209,53) );
		creerServeur.setOpaque(true);
		creerServeur.setBorderPainted(false);
		creerServeur.setForeground(Color.RED);
		
		
		connecterServeur = new JButton ("Connecter a un serveur !"); // Bouton pour creer le serveur
		connecterServeur.setBackground (new Color(255,209,53) );
		connecterServeur.addActionListener(new EcouteurConnexion1(this));
		connecterServeur.setOpaque(true);
		connecterServeur.setBorderPainted(false);
		connecterServeur.setForeground(Color.RED);
		
		
		panelCentre = new JPanel (new BorderLayout()); // Panel du centre regroupant choix du personnage, pseudo et serveur
		panelCentre.setBackground(Color.WHITE);
		panelCentre.add(panelInter, BorderLayout.NORTH);
		panelCentre.add(panelPseudo, BorderLayout.CENTER);
		
		lancerJeu.add(bienvenue,BorderLayout.NORTH); // Disposition dans le panel principal
		lancerJeu.add(panelCentre,BorderLayout.CENTER);
		
		JPanel southPanel = new JPanel();
		southPanel.add(creerServeur);
		southPanel.add(connecterServeur);
		lancerJeu.add(southPanel,BorderLayout.SOUTH);
			
		add(lancerJeu);
		
		setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		setSize(800,700);
		setLocationRelativeTo(null);
		setVisible(true);
	}
		
	public void deplacementDroite(){
		if(currentImage<index.length-1){
				currentImage++;
		}
			personnage.setIcon(new ImageIcon(index[currentImage]));
			panelImage.repaint();
			
	lancerJeu.repaint();

  }
	
	public void deplacementGauche(){
		if(currentImage>0){
				currentImage--;
		}
		personnage.setIcon(new ImageIcon(index[currentImage]));
		panelImage.repaint();
			
	lancerJeu.repaint();
   }
	
	public void creerServeur()
	{
		kill();
		//FIXME: HardCoded the port
		callback.onCreateServer(rentrerPseudo.getText(),"localhost",8000);
	}
	
	public void connecteeServeur(String ip)
	{
		kill();
		String adresse = ip.substring(0, ip.length()-5);
		int porte = Integer.parseInt( ip.substring(ip.length()-4, ip.length()));
		callback.onConnectServer(rentrerPseudo.getText(), adresse, porte);
	}
	
	public void lancerOptionsConnexions()
	{
		new ConnectServer(this);
	}
	
	public void kill()
	{
		remove(lancerJeu);
		revalidate();
		repaint();
	}
	
}
