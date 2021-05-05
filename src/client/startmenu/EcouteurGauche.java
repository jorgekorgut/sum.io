package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurGauche implements ActionListener {
	private DebutJeu f;
	
	public EcouteurGauche (DebutJeu f){
		this.f=f;
	}
	public void actionPerformed (ActionEvent ae){
		f.deplacementGauche();
	}
}


