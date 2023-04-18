package com.andromeda.desc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.util.Collections;
import java.util.HashMap;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DescriptionBar extends JPanel {
	
	HashMap<String, Dimension> textToDraw = new HashMap<>();

	public DescriptionBar(Dimension screen) {
		this.setBounds(0, 0, (int) Math.round(screen.getWidth()-Math.round(screen.getWidth()/1.25f)), (int) screen.getHeight());
		this.setBackground(new Color(230, 230, 230));
	}
	
	public void renderText(HashMap<String, Dimension> text) {
		this.textToDraw = text;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
		Font customFont = new Font("Sans", Font.PLAIN, 30);
	    customFont = customFont.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, TextAttribute.WEIGHT_ULTRABOLD));
	    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
	    ge.registerFont(customFont);
	    g2d.setFont(customFont);
		
		for (String i : textToDraw.keySet()) {
			g2d.drawString(i, (int) Math.round(textToDraw.get(i).getWidth()), (int) Math.round(textToDraw.get(i).getHeight()));
		}
	}
}