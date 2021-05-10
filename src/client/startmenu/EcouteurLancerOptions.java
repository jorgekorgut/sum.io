package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurLancerOptions implements ActionListener {
	private DebutJeu f;
	
	public EcouteurLancerOptions (DebutJeu f){
		this.f=f;
	}
	public void actionPerformed (ActionEvent ae){
		f.lancerOptionsConnexions();
	}
}


