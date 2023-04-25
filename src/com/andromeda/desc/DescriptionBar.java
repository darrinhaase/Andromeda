package com.andromeda.desc;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import com.andromeda.main.Main;

@SuppressWarnings("serial")
public class DescriptionBar extends JPanel {
	
	private ArrayList<DrawingText> textToDraw = new ArrayList<>();
	private ArrayList<JTextArea> texts = new ArrayList<>();
	private Rectangle close = null;
	private boolean clear;

	public DescriptionBar(Dimension screen) {
		this.setBounds(0, 0, (int) Math.round(screen.getWidth()-Math.round(screen.getWidth()/1.25f)), (int) screen.getHeight());
		this.setBackground(new Color(230, 230, 230));
		this.setLayout(null);
		this.addMouseListener(new MouseListener() {
			public void mouseReleased(MouseEvent e) {}
			public void mousePressed(MouseEvent e) {
				if (close.contains(e.getX(), e.getY())) {
					Main.toggleDescriptionBar(false);
				}
			}
			public void mouseExited(MouseEvent e) {}
			public void mouseEntered(MouseEvent e) {}
			public void mouseClicked(MouseEvent e) {}
		});
	}
	
	public void renderText(DrawingText text, boolean clear) {
		if (clear) {
			this.removeAll();
			for (JTextArea j : texts) {
				this.remove(j);
			}
			this.textToDraw.clear();
			this.texts.clear();
		}
		this.clear = clear;
		this.textToDraw.add(text);
	}
	
	@Override
	public void paintComponents(Graphics g) {
		if (clear) {
			for (JTextArea j : texts) {
				this.remove(j);
			}
			this.textToDraw.clear();
			this.texts.clear();
			this.removeAll();
		}
		
		
		//Necessary to reverse the order of the index so everything is painted in the correct order
		for (int l = textToDraw.size()-1; l >= 0; l--) {
			Font customFont = null;
			DrawingText i = textToDraw.get(l);
			
			if (i.isItalics()) {
				customFont = new Font("Sans", Font.ITALIC, i.getSize());
			} else {
				customFont = new Font("Sans", Font.PLAIN, i.getSize());
			}
			
			customFont = customFont.deriveFont(Collections.singletonMap(TextAttribute.WEIGHT, i.getBoldness()));
			Map<TextAttribute, Object> attributes = new HashMap<TextAttribute, Object>();
			attributes.put(TextAttribute.TRACKING, 0.03);
			customFont = customFont.deriveFont(attributes);
		    GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
		    ge.registerFont(customFont);
		    
		    JTextArea textArea = new JTextArea(i.getText());
		    textArea.setFont(customFont);
		    textArea.setLineWrap(true);
		    textArea.setWrapStyleWord(true);
		    textArea.setBackground(this.getBackground());
		    textArea.setEditable(false);
		    textArea.setHighlighter(null);
		    textArea.setBounds(i.getX(), i.getY()-10, this.getWidth()-i.getX()-10, this.getHeight()-i.getY());
		    
		    texts.add(textArea);
		    
		    
		    this.add(textArea);
		    this.repaint();
		}
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setStroke(new BasicStroke(2));
		g2d.drawLine(this.getWidth()-25, 10, this.getWidth()-10, 25);
		g2d.drawLine(this.getWidth()-25, 25, this.getWidth()-10, 10);
		this.close = new Rectangle(this.getWidth()-25, 10, 15, 15);
	}
}