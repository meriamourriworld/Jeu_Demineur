package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class FenetreMessage extends JDialog {

	JPanel msgPanel;
	ImageIcon msgIcone;
	JLabel lblMsgIcon, lblMsg;
	JButton btnNouvellePartie,test;

	public FenetreMessage(JFrame parent, String msg, String chemin) {
		super(parent, "MESSAGE", true);
		// Initialisation des composants
		msgPanel = new JPanel(null);
		msgIcone = new ImageIcon(FenetreMessage.class.getResource(chemin));
		lblMsgIcon = new JLabel();
		lblMsgIcon.setIcon(msgIcone);
		lblMsg = new JLabel(msg);
		btnNouvellePartie = new JButton("Nouvelle Partie");
		test = new JButton("test");

		// Personnalisation des composants
		msgPanel.setBackground(new Color(92, 0, 163));
		lblMsgIcon.setBounds(140, 30, 150, 150);
		lblMsg.setBounds(120, 180, 220, 40);
		lblMsg.setForeground(Color.white);
		lblMsg.setFont(new Font("Perpetua Titling MT", Font.BOLD, 24));
		msgPanel.setBorder(new LineBorder(new Color(203, 138, 255), 2, true));
		btnNouvellePartie.setBounds(115, 230, 180, 40);
		btnNouvellePartie.setForeground(Color.white);
		btnNouvellePartie.setBackground(new Color(117, 0, 209));
		btnNouvellePartie.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 14));
		btnNouvellePartie.setFocusPainted(false);
		btnNouvellePartie.setRolloverEnabled(false);
		btnNouvellePartie.setCursor(new Cursor(Cursor.HAND_CURSOR));

		// Ajout des composants au Panel
		msgPanel.add(lblMsgIcon);
		msgPanel.add(lblMsg);
		msgPanel.add(btnNouvellePartie);
		
		
		//Événements
		btnNouvellePartie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});

		// Personnalisation de la fenêtre
		
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setContentPane(msgPanel);
		this.setVisible(true);
		

}
}
