package client.startmenu;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class EcouteurConnecterAuServeur implements ActionListener {
	private ConnectServer cs;
	
	public EcouteurConnecterAuServeur (ConnectServer cs){
		this.cs=cs;
	}
	public void actionPerformed (ActionEvent ae){
		cs.connecterAuServeur();
	}
}


