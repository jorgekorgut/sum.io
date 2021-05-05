package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurDroite implements ActionListener {
	private DebutJeu f;
	
	public EcouteurDroite (DebutJeu f){
		this.f=f;
	}
	public void actionPerformed (ActionEvent ae){
		f.deplacementDroite();
	}
}

