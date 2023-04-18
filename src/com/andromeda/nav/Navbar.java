package com.andromeda.nav;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.HashMap;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Navbar extends JPanel {
	
	HashMap<String, Dimension> textToDraw = new HashMap<>();

	public Navbar(Dimension screen) {
		this.setBounds(0, 0, (int) Math.round(screen.getWidth()-Math.round(screen.getWidth()/1.25f)), (int) screen.getHeight());
		this.setBackground(new Color(181,181,181));
	}
	
	public void renderText(HashMap<String, Dimension> text) {
		this.textToDraw = text;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(10));
		for (String i : textToDraw.keySet()) {
			g2d.drawString(i, (int) Math.round(textToDraw.get(i).getWidth()), (int) Math.round(textToDraw.get(i).getWidth()));
		}
	}
}