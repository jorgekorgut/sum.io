package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurConnexion2 implements ActionListener {
	private ConnectServer cs;
	
	public EcouteurConnexion2 (ConnectServer cs){
		this.cs=cs;
	}
	public void actionPerformed (ActionEvent ae){
		cs.connecterAuServeur();
	}
}


