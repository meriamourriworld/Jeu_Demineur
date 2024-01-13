package game;

import java.awt.Color;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;
import javax.swing.border.LineBorder;


public class JeuDemineur extends JFrame{
	static Joueur joueur;
	String nomJoueur;
	enum niveau {Faible, Moyen, Expert};
	Timer horloge;
	static int time=0, cols=0, rows = 0;
	int nbMines= 0, nbDrapeau = 0, nbDrapeauxUtilises = 0, nbCaseOUvert = 0, scorePartie;
	Case [][] cases;
	LinkedList<Case> zoneVide;

	
	JPanel jeuPanel, headerPanel, grillePanel;
	JComboBox<String> selectNiveau;
	JButton btnStart, btnQuitter;
	JLabel timer,score, profile, lblCounter, lblScore, lblProfile;
	ImageIcon timerIcon,scoreIcon, profileIcon, bombeIcon, drapeauIcon;

	
	public JeuDemineur(String nom)
	{
		//Initialisation des composants
		zoneVide = new LinkedList<Case>();
		this.nomJoueur = nom;
		selectNiveau = new JComboBox<String>();
		selectNiveau.setModel(new DefaultComboBoxModel(niveau.values()));
		btnStart = new JButton("Lancer");
		btnQuitter = new JButton("Quitter");
		timer = new JLabel("");
		timerIcon = new ImageIcon(JeuDemineur.class.getResource("/images/timer.png"));
		timer.setIcon(timerIcon);
		score = new JLabel("");
		scoreIcon = new ImageIcon(JeuDemineur.class.getResource("/images/score.png"));
		score.setIcon(scoreIcon);
		profile = new JLabel("");
		profileIcon = new ImageIcon(JeuDemineur.class.getResource("/images/profile.png"));
		profile.setIcon(profileIcon);
		lblCounter = new JLabel("0000");

		lblScore = new JLabel("0");
		lblProfile = new JLabel(this.nomJoueur.toUpperCase());
		grillePanel = new JPanel();
		headerPanel = new JPanel();
		jeuPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		
		//Creation de la grille
		 creerGrille(selectNiveau.getSelectedItem().toString());

		//Personnalisation des composants
			//Jeu Panel
		jeuPanel.setBounds(0, 0, 600, 600);
		jeuPanel.setBackground(new Color(92, 0, 163));
		jeuPanel.setBorder(new LineBorder(new Color(203, 138, 255), 2, true));
		jeuPanel.setEnabled(false);
			//SelectNiveau
		selectNiveau.setPreferredSize(new Dimension(100,30));
		selectNiveau.setBackground(new Color(92, 0, 163));
		selectNiveau.setForeground(Color.WHITE);
		selectNiveau.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 12));
		selectNiveau.setBorder(new LineBorder(new Color(203, 138, 255), 1, true));
		selectNiveau.setCursor(new Cursor(Cursor.HAND_CURSOR));
			//START BUTTON
		btnStart.setBackground(new Color(117, 0, 209));
		btnStart.setPreferredSize(new Dimension(100,30));
		btnStart.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 12));
		btnStart.setFocusPainted(false);
		btnStart.setRolloverEnabled(false);
		btnStart.setForeground(Color.white);
		btnStart.setCursor(new Cursor(Cursor.HAND_CURSOR));
			//QUITTER BUTTON
		btnQuitter.setPreferredSize(new Dimension(100,30));
		btnQuitter.setBackground(new Color(117, 0, 209));
		btnQuitter.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 12));
		btnQuitter.setFocusPainted(false);
		btnQuitter.setRolloverEnabled(false);
		btnQuitter.setForeground(Color.white);
		btnQuitter.setCursor(new Cursor(Cursor.HAND_CURSOR));
			//LABEL COUNTER
		lblCounter.setForeground(Color.white);
			//LABEL Score
		lblScore.setForeground(Color.white);
			//LABEL Profile
		lblProfile.setForeground(Color.white);
			//HEADER PANEL
		headerPanel.setPreferredSize(new Dimension(598,60));
		headerPanel.setLocation(0, 0);
		headerPanel.setBackground(new Color(155, 62, 230));
					
		//Ajout des composants au Panel
		headerPanel.add(selectNiveau);
		headerPanel.add(btnStart);
		headerPanel.add(btnQuitter);
		headerPanel.add(timer);
		headerPanel.add(lblCounter);
		headerPanel.add(score);
		headerPanel.add(lblScore);
		headerPanel.add(profile);
		headerPanel.add(lblProfile);
		jeuPanel.add(headerPanel);
		jeuPanel.add(grillePanel);
		Component[] components = grillePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(false);
            }
        }
		
		//Initialisation de la fenetre
		this.setSize(600, 662);
		this.setLocationRelativeTo(null);
		this.setUndecorated(true);
		this.setContentPane(jeuPanel);
		this.setVisible(true);
		
		//Gestion des événements
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {


				horloge.start();
				Component[] components = grillePanel.getComponents();
		        for (Component component : components) {
		            if (component instanceof JButton) {
		                ((JButton) component).setEnabled(true);
		            }
		        }
		        btnStart.setEnabled(false);
			}
		});
		
		
		btnQuitter.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				joueur.dispose();
				dispose();
				enregistrerMeilleurScore();
			}
		});
		
			//Au changement du niveau
		selectNiveau.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				horloge.stop();
				creerGrille(selectNiveau.getSelectedItem().toString());
				Component[] components = grillePanel.getComponents();
		        for (Component component : components) {
		            if (component instanceof JButton) {
		                ((JButton) component).setEnabled(false);
		            }
		        }
			}
		});
	}
	
	//Permet de créer les cases de la grille et de les stocker dans un tableau[][] pour usage ultérieur
	public void creerGrille(String niveau)
	{ 
		int [] couleur1 = {67,0,130}, couleur= {};
		int []couleur2 = {165, 127, 192};
		horloge = new Timer(1000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				time++;
				lblCounter.setText(time + " s  ");
			}
		});

		btnStart.setEnabled(true);
		nbCaseOUvert = 0;
		time = 0;
		scorePartie = 0;
		lblScore.setText(scorePartie+"");
		lblCounter.setText("0");
		//Supprimer toutes les cases précédentes pour reconstituer la grille
		grillePanel.removeAll();
		switch (niveau)
		{
			case "Faible": cols = 10; rows = 8; nbMines = nbDrapeau = nbDrapeauxUtilises = 10; break;
			case "Moyen": cols = 20; rows = 12; nbMines = nbDrapeau = nbDrapeauxUtilises = 40 ; break;
			case "Expert": cols = 24; rows = 20; nbMines = nbDrapeau = nbDrapeauxUtilises = 99; break;
		}
		cases = new Case [rows][cols];
		grillePanel.setLayout(new GridLayout(rows, cols, 0, 0));
		//Creation des cases
		for(int i = 0; i < rows; i++)
		{
			for(int j = 0; j < cols; j++)
			{
				//Couleurs des cases pour les lignes impairs
				if(i % 2 !=0)
				{
					if(j % 2 !=0)
					{
						couleur = couleur1;
					}else {
						couleur = couleur2;
					}
				}
				//Couleurs des cases pour les lignes pairs
				if(i % 2 ==0)
				{
					if(j % 2 ==0)
					{
						couleur = couleur1;
					}else {
						couleur = couleur2;
					}
				}
				Case c = new Case(600/cols, 600/rows, new Color(couleur[0], couleur[1], couleur[2]));
				cases[i][j] = c;
				c.row = i;
				c.col = j;
				grillePanel.add(c);		
			}
		}
		
		//Mise en place des bombes et des indicateurs sur la grille
		placerBombes();
		placerIndicateur();
		
		//Attacher les événements aux cases afin de déminer la grille
		for(int i = 0; i <rows; i++)
		{
			for (int j = 0; j < cols; j++)
			{
				Case c = cases[i][j];
				cases[i][j].addActionListener(new ActionListener() {
	
					public void actionPerformed(ActionEvent e) {
						String content = c.getContenu();
						if(content.equals("bombe")) afficherBombe(c);
						else {
							if(content.length()>0) afficherIndicateur(c, content);
							else
							{
								//Ajouter une première case vide dans la liste de la zone vide
								zoneVide.add(cases[c.row][c.col]);
	
								//Propagation de la zone vide
								/***********************************DEBUT PROPAGATION***************************************/
								while(!zoneVide.isEmpty())
								{
									Case prochaineCase = zoneVide.getFirst();
									
									if(caseHorsLimite(prochaineCase.row-1, prochaineCase.col-1) == false && !zoneVide.contains(cases[prochaineCase.row-1][prochaineCase.col-1]) && cases[prochaineCase.row-1][prochaineCase.col-1].ouvert==false)
									{
										if(cases[prochaineCase.row-1][prochaineCase.col-1].getContenu().length() > 0) { 
											afficherIndicateur(cases[prochaineCase.row-1][prochaineCase.col-1], cases[prochaineCase.row-1][prochaineCase.col-1].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row-1][prochaineCase.col-1]);}
									}
		
									if(caseHorsLimite(prochaineCase.row-1, prochaineCase.col+1) == false && !zoneVide.contains(cases[prochaineCase.row-1][prochaineCase.col+1]) && cases[prochaineCase.row-1][prochaineCase.col+1].ouvert == false)
									{
										 if(cases[prochaineCase.row-1][prochaineCase.col+1].getContenu().length() > 0) { 
											 afficherIndicateur(cases[prochaineCase.row-1][prochaineCase.col+1], cases[prochaineCase.row-1][prochaineCase.col+1].getContenu());}
										 else {zoneVide.add(cases[prochaineCase.row-1][prochaineCase.col+1]);}
									}
										
									if(caseHorsLimite(prochaineCase.row+1, prochaineCase.col-1) == false && !zoneVide.contains(cases[prochaineCase.row+1][prochaineCase.col-1]) && cases[prochaineCase.row+1][prochaineCase.col-1].ouvert == false)
									{
										if(cases[prochaineCase.row+1][prochaineCase.col-1].getContenu().length() > 0) { 
											afficherIndicateur(cases[prochaineCase.row+1][prochaineCase.col-1], cases[prochaineCase.row+1][prochaineCase.col-1].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row+1][prochaineCase.col-1]);}
									}
										
									if(caseHorsLimite(prochaineCase.row+1, prochaineCase.col+1) == false && !zoneVide.contains(cases[prochaineCase.row+1][prochaineCase.col+1]) && cases[prochaineCase.row+1][prochaineCase.col+1].ouvert == false)
									{
										if(cases[prochaineCase.row+1][prochaineCase.col+1].getContenu().length() > 0) { 
											afficherIndicateur(cases[prochaineCase.row+1][prochaineCase.col+1], cases[prochaineCase.row+1][prochaineCase.col+1].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row+1][prochaineCase.col+1]);}
									}	
									
									if(caseHorsLimite(prochaineCase.row-1, prochaineCase.col) == false && !zoneVide.contains(cases[prochaineCase.row-1][prochaineCase.col]) && cases[prochaineCase.row-1][prochaineCase.col].ouvert == false)
									{
										if(cases[prochaineCase.row-1][prochaineCase.col].getContenu().length() > 0) { 
											afficherIndicateur(cases[prochaineCase.row-1][prochaineCase.col], cases[prochaineCase.row-1][prochaineCase.col].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row-1][prochaineCase.col]);}
									}	
									
									if(caseHorsLimite(prochaineCase.row, prochaineCase.col-1) == false && !zoneVide.contains(cases[prochaineCase.row][prochaineCase.col-1]) && cases[prochaineCase.row][prochaineCase.col-1].ouvert == false)
									{
										if(cases[prochaineCase.row][prochaineCase.col-1].getContenu().length() > 0) { 
											afficherIndicateur(cases[prochaineCase.row][prochaineCase.col-1], cases[prochaineCase.row][prochaineCase.col-1].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row][prochaineCase.col-1]);}
									}	
										
									if(caseHorsLimite(prochaineCase.row, prochaineCase.col+1) == false && !zoneVide.contains(cases[prochaineCase.row][prochaineCase.col+1]) && cases[prochaineCase.row][prochaineCase.col+1].ouvert == false)
									{
										if(cases[prochaineCase.row][prochaineCase.col+1].getContenu().length() > 0) {
											afficherIndicateur(cases[prochaineCase.row][prochaineCase.col+1], cases[prochaineCase.row][prochaineCase.col+1].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row][prochaineCase.col+1]);}
									}
									
									if(caseHorsLimite(prochaineCase.row+1, prochaineCase.col) == false && !zoneVide.contains(cases[prochaineCase.row+1][prochaineCase.col]) && cases[prochaineCase.row+1][prochaineCase.col].ouvert == false)
									{
										if(cases[prochaineCase.row+1][prochaineCase.col].getContenu().length() > 0) {
											afficherIndicateur(cases[prochaineCase.row+1][prochaineCase.col], cases[prochaineCase.row+1][prochaineCase.col].getContenu());}
										else {zoneVide.add(cases[prochaineCase.row+1][prochaineCase.col]);}
									}
									
									afficherIndicateur(prochaineCase, prochaineCase.contenu);
									zoneVide.removeFirst();
								}
								/*******************************************FIN PROPAGATION***************************************************/
						}
					}
					}
				});
				
				c.addMouseListener(new MouseAdapter() {
					@Override
					public void mouseClicked(MouseEvent e) {
						if(e.getModifiersEx() == MouseEvent.META_DOWN_MASK && c.ouvert == false && nbDrapeau > 0 && c.drapeau == false)
						{
							drapeauIcon = new ImageIcon(JeuDemineur.class.getResource("/images/drapeau.png"));
							c.setIcon(drapeauIcon); 
							c.drapeau = true;
							if(!c.contenu.equals("bombe")) {
								arreterJeu("drapeau");
								}
							nbDrapeauxUtilises--;
							nbCaseOUvert++;
							calculerScore();
						}
					}
				});
			}
		}
	}
	
	//Fonction qui sert à distribuer les bombes exigés par niveau dans la grille de façon aléatoire
	public void placerBombes()
	{
		int mine = 1,r,c;
		Random rand = new Random();
		while(mine <=nbMines)
		{
			//Boucler pour éviter de mettre une bombe dans une case qui en contient déjà une=> Résultat : avoir moins de bombes que prévu
			do
			{
				r = rand.nextInt(cases.length);
				c = rand.nextInt(cases.length);
			}while(cases[r][c].getContenu().equals("bombe"));
			cases[r][c].setContenu("bombe");
			mine++;
		}
	}
	//Méthode qui permet de placer le chiffre indicateur du nombre de mines cachées dans les cases adjacentes
	public void placerIndicateur()
	{
		int nbBombes = 0;
		
		for(int i = 0; i <rows; i++)
		{
			for(int j = 0; j <cols; j++)
			{
				if(!cases[i][j].getContenu().equals("bombe"))
				{
					if((caseHorsLimite(i-1, j-1) == false) && (cases[i-1][j-1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i-1, j+1) == false) && (cases[i-1][j+1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i+1, j-1) == false) && (cases[i+1][j-1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i+1, j+1) == false) && (cases[i+1][j+1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i-1, j) == false) && (cases[i-1][j].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i, j-1) == false) && (cases[i][j-1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i, j+1) == false) && (cases[i][j+1].getContenu() == "bombe")) nbBombes++;
					if((caseHorsLimite(i+1, j) == false) && (cases[i+1][j].getContenu() == "bombe")) nbBombes++;

					if(nbBombes == 0) { cases[i][j].setContenu("");}
					else { cases[i][j].setContenu(nbBombes+"");}
					nbBombes = 0;
				}
			}
		}
		
	}
	
	//Fonction qui permet de retourner un boolean indiquant si la case est dans les limites de la grille ou hors ces limites
	public boolean caseHorsLimite(int r, int c)
	{
		int min=0, maxR = rows-1, maxC = cols-1;
		return (r < min || c < min || r > maxR || c > maxC);
	}
	
	//Méthode qui permet d'afficher une bombe dans la case cliquée
	public void afficherBombe(Case c)
	{
		if(c.drapeau == false)
		{
			bombeIcon = new ImageIcon(JeuDemineur.class.getResource("/images/bombe.png"));
			c.setIcon(bombeIcon); 
			c.ouvert = true;
			arreterJeu("bombe");
		}
	}
	
	//Méthode qui permet d'afficher une un/des indicateur(s) sur le nombre de bombe dans l'environnement de la case cliquée
	public void afficherIndicateur(Case c, String nb)
	{
		int [] couleur1 = {238,181,73}, couleur= {};
		int []couleur2 = {255,211,126};
		//Couleurs des cases pour les lignes impairs
		if(c.row % 2 !=0)
		{
			if(c.col % 2 !=0)
			{
				couleur = couleur1;
			}else {
				couleur = couleur2;
			}
		}
		//Couleurs des cases pour les lignes pairs
		if(c.row % 2 ==0)
		{
			if(c.col % 2 ==0)
			{
				couleur = couleur1;
			}else {
				couleur = couleur2;
			}
		}
		c.setIcon(null);
		c.setBackground( new Color(couleur[0], couleur[1], couleur[2]));
		c.setText(nb);
		c.ouvert = true;
		nbCaseOUvert++;
		c.setEnabled(false);
		calculerScore();
		if((nbCaseOUvert + nbMines) == (rows * cols)) {new FenetreMessage(JeuDemineur.this,"FÉLICITATIONS!", "/images/bravo.PNG");enregistrerMeilleurScore();}
	}

	//Fonction qui calcule et actualise le score
	public void calculerScore()
	{
		if(time == 0) time=1;
		scorePartie = ((nbCaseOUvert * 10) + ((nbDrapeau - nbDrapeauxUtilises) * 15));
		scorePartie *= 10;
		lblScore.setText((scorePartie) +"");
	}
	
	//Fonction qui arrête la partie et réinitialise le jeu et permet également d'enregistrer le meilleur score si le joueur en a battu son dernier meilleur score
	public void arreterJeu(String motif)
	{
		horloge.stop();
        //Enregistrer Le meilleur score
        enregistrerMeilleurScore();
		creerGrille(selectNiveau.getSelectedItem().toString());
		Component[] components = grillePanel.getComponents();
        for (Component component : components) {
            if (component instanceof JButton) {
                ((JButton) component).setEnabled(false);
            }
        }
		if(motif.equals("bombe")) {FenetreMessage msg = new FenetreMessage(JeuDemineur.this,"GAME OVER !", "/images/gameOver.PNG");}
		if(motif.equals("drapeau")) {FenetreMessage msg = new FenetreMessage(JeuDemineur.this,"GAME OVER !", "/images/gameOverFlag.PNG");}
	}
	
	//Méthode qui permet de vérifier si le joueur a battu son dernier meilleur score enregistré dans le fichier alors le remplace
	public void enregistrerMeilleurScore()
	{
		int meilleurScore=0;
		if(time == 0) time=1;

		//Enregistrer Le meilleur score
		try {
			File f = new File("scores.txt");
			if(f.exists())
			{
				FileReader fr = new FileReader("scores.txt");
				BufferedReader buff = new BufferedReader(fr);
				String line = buff.readLine();
				if(line != null)
				{
					meilleurScore = Integer.parseInt(line);
				}
				buff.close();
				fr.close();
			}
			
			if((scorePartie/time) > meilleurScore)
			{
				FileWriter file = new FileWriter("scores.txt");
				PrintWriter fw = new PrintWriter(file);
				fw.print(scorePartie/time);
				fw.close();
			}
			scorePartie = 0;
			
		} catch (IOException e) {
			System.out.println("Problème d'enregistrement dans le fichier ");
		}
	}
	
	//Programme Principal
	public static void main(String[] args) {
		joueur = new Joueur();
		
	}

}
