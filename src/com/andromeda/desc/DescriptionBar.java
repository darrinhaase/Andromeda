package com.andromeda.desc;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class DescriptionBar extends JPanel {
	
	ArrayList<DrawingText> textToDraw = new ArrayList<>();

	public DescriptionBar(Dimension screen) {
		this.setBounds(0, 0, (int) Math.round(screen.getWidth()-Math.round(screen.getWidth()/1.25f)), (int) screen.getHeight());
		this.setBackground(new Color(230, 230, 230));
	}
	
	public void renderText(DrawingText text, boolean clear) {
		if (clear) {
			this.textToDraw.clear();
		}
		this.textToDraw.add(text);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g.create();

		for (DrawingText i : textToDraw) {
			Font customFont = new Font("Sans", Font.PLAIN, i.getSize());
		    customFont = customFont.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, i.getBoldness()));
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		    g2d.setFont(customFont);
			g2d.drawString(i.getText(), i.getX(), i.getY());
		}
	}
}