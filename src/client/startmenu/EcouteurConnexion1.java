package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurConnexion1 implements ActionListener {
	private DebutJeu f;
	
	public EcouteurConnexion1 (DebutJeu f){
		this.f=f;
	}
	public void actionPerformed (ActionEvent ae){
		f.lancerOptionsConnexions();
	}
}


