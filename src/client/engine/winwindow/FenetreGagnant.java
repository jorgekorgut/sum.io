package client.engine.winwindow;

import java.awt.*;
import java.awt.Font.*;

import javax.security.auth.callback.Callback;
import javax.swing.*;

import client.MainClient;
import client.engine.sound.AudioMaster;

import java.awt.event.*;

public class FenetreGagnant extends JFrame implements ActionListener{

   private JLabel textNom, textGagnant, imageFond;

   private JButton rejouer, quitter;
   
   private MainClient callback;

   public FenetreGagnant(MainClient callback, String name) {

       this.callback = callback;

       this.setTitle("Victoire");
       this.setBounds(400, 100, 500, 500);

       Font police = new Font("Arial", Font.BOLD, 20);

       imageFond = new JLabel(new ImageIcon(System.getProperty("user.dir") + System.getProperty("file.separator")+"res"+ System.getProperty("file.separator")+"images"+ System.getProperty("file.separator")+"feu.gif"));
       
       imageFond.setBounds(0, 0, getWidth(), getHeight());

       textNom = new JLabel("Winner winner chicken dinner",SwingConstants.CENTER);//"Felicitations  " + name + "!" , SwingConstants.CENTER);
       textNom.setBounds(imageFond.getWidth()/2 - 200, imageFond.getHeight()/2 - 200, 400, 250);
       textNom.setBackground(Color.white);
       textNom.setFont(police);
       textNom.setForeground(Color.white);

       textGagnant = new JLabel(name + " est le dernier survivant!", SwingConstants.CENTER);
       textGagnant.setBounds(imageFond.getWidth()/2 - 200, imageFond.getHeight()/2 - 175, 400, 250);
       textGagnant.setBackground(Color.white);
       textGagnant.setFont(police);
       textGagnant.setForeground(Color.white);

       rejouer = new JButton("Rejouer");
       rejouer.setBounds(imageFond.getWidth()/4, imageFond.getHeight() - 200, 125, 70);
       rejouer.setBackground(Color.white);
       rejouer.setFont(police);
       rejouer.setForeground(Color.blue);
       rejouer.addActionListener(this);

       quitter = new JButton("Quitter");
       quitter.setBounds(3*imageFond.getWidth()/4 - 125, imageFond.getHeight() - 200, 125, 70);
       quitter.setBackground(Color.white);
       quitter.setFont(police);
       quitter.setForeground(Color.blue);
       quitter.addActionListener(this);

       imageFond.add(textNom);
       imageFond.add(textGagnant);
       imageFond.add(rejouer);
       imageFond.add(quitter);
       this.add(imageFond);
       
       setLocationRelativeTo(null);
       this.setVisible(true);

   }

   public void actionPerformed(ActionEvent e) {

       if(e.getSource() == rejouer) {

    	callback.returnLobby();
    	callback.getAudioMaster().stopSound(AudioMaster.END_MUSIC_REFERENCE);
        this.dispose();
       }

       if(e.getSource() == quitter) {

    	 System.exit(1);
         this.dispose();
       }
   }

}