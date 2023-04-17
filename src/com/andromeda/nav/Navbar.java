package com.andromeda.nav;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class Navbar extends JPanel {

	public Navbar(Dimension screen) {
		this.setBounds(0, 0, (int) Math.round(screen.getWidth()-Math.round(screen.getWidth()/1.25f)), (int) screen.getHeight());
		this.setBackground(new Color(181,181,181));
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.drawImage(new ImageIcon("assets/line-segment.png").getImage(), 10, 10, 70, 70, null);
	}
	
}