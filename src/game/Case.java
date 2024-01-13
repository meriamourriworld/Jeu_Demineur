package game;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JButton;

public class Case extends JButton{
	String contenu;
	int row;
	int col;
	boolean ouvert, drapeau;
	
	
	public Case(int width, int height, Color color)
	{
		this.contenu = "";
		this.ouvert = false;
		this.drapeau = false;
		super.setPreferredSize(new Dimension(width, height));
		super.setBackground(color);
		super.setFont(new Font("Perpetua Titling MT", Font.PLAIN, 18));
		super.setForeground(Color.black);
		super.setFocusPainted(false);
		super.setRolloverEnabled(false);
		super.setBorder(null);
		super.setCursor(new Cursor(Cursor.HAND_CURSOR));
	}
	
	public void setContenu(String content)
	{
		this.contenu = content;
	}
	
	public String getContenu()
	{
		return this.contenu;
	}
}
