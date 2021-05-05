package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurCreerServeur implements ActionListener {
	private DebutJeu f;
	
	public EcouteurCreerServeur (DebutJeu f){
		this.f=f;
	}
	public void actionPerformed (ActionEvent ae){
		f.creerServeur();
	}
}


