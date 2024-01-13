package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class Joueur extends JFrame{

	JLabel btnClose, playerIcon,lblNom;
	JTextField txtNom;
	JButton btnValider;
	JPanel mainPanel;
	ImageIcon profilImg;
	
	//Constructeur
	public Joueur()
	{
		//Initialisation des composants
		profilImg = new ImageIcon(Joueur.class.getResource("/images/playerIcon.PNG"));
		playerIcon = new JLabel();
		lblNom = new JLabel("Joueur");
		txtNom = new JTextField();
		btnValider = new JButton("Valider");
		btnClose = new JLabel("X");
		mainPanel = new JPanel(null);
		
		//Mise en forme des composants
		mainPanel.setBackground(new Color(92, 0, 163));
		mainPanel.setBorder(new LineBorder(new Color(203, 138, 255), 2, true));
		playerIcon.setIcon(profilImg);
		playerIcon.setBounds(140, 30, 120, 100);
		lblNom.setBounds(170, 115, 100, 80);
		txtNom.setBounds(115, 170, 180, 40);
		btnValider.setBounds(115, 230, 180, 40);
		btnClose.setBounds(370, 2, 40, 40);

		lblNom.setFont(new Font("Perpetua Titling MT", Font.BOLD, 13));
		txtNom.setBorder(new LineBorder(new Color(203, 138, 255), 2, true));
		txtNom.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 14));
		txtNom.setHorizontalAlignment(JTextField.CENTER);
		lblNom.setForeground(Color.white);
		btnValider.setForeground(Color.white);
		btnValider.setBackground(new Color(117, 0, 209));
		btnValider.setFont(new Font("Perpetua Titling MT", Font.BOLD, 14));
		btnValider.setFocusPainted(false);
		btnValider.setRolloverEnabled(false);
		btnValider.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnClose.setForeground(new Color(189,45,45));
		btnClose.setCursor(new Cursor(Cursor.HAND_CURSOR));
		btnClose.setFont(new Font("Arial", Font.BOLD, 20));


		//Ajouter les composants au MainPanel

		mainPanel.add(btnClose);
		mainPanel.add(playerIcon);
		mainPanel.add(lblNom);
		mainPanel.add(txtNom);
		mainPanel.add(btnValider);
		
		//Ajouter les événements aux boutons
		btnClose.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				dispose();
			}
		});
		
		this.btnValider.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(txtNom.getText().replace(" ", "").equals("")) 
				{
					txtNom.setBorder(new LineBorder(Color.red, 2, true));
					txtNom.setText("");
				}else
				{
					new JeuDemineur(getNom());
					setVisible(false);
				}
			}
		});
		
		//Initialisation de la fenetre
		this.setSize(400, 300);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setContentPane(mainPanel);
		this.setVisible(true);
	}
	
	//Getters & Setters
	public String getNom()
	{
		return txtNom.getText();
	}
	

}
