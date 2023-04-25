package com.andromeda.vgraph;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import javax.swing.JPanel;
import com.andromeda.main.Main;
import com.andromeda.main.trusty;

@SuppressWarnings("serial")
public class GraphGUI extends JPanel {

	private Dimension size = null;
	public boolean saved = true;
	public File saveFile = null;
	private ArrayList<Instruction> instructions = new ArrayList<>();
	private int naming = 0;
	
	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}

	public void setInstructions(ArrayList<Instruction> instructions) {
		this.instructions = instructions;
	}

	public GraphGUI(int x, int y, int width, int height) throws Exception {
		if (width <= Main.screen.width/6 || height <= Main.screen.height/6) throw new Exception("Graph cannot be smaller than 1/5 of the screen width or height");
		size = new Dimension(width,height);
		this.setPreferredSize(size);
	}
	
	public void drawCircle(int x, int y, int r, String name) {
		Instruction tempInstruction = new Instruction(x-(r/2), y-(r/2), name, String.valueOf(naming));
			tempInstruction.setFilled(true);
			tempInstruction.setRadius(r);
			
		instructions.add(tempInstruction);
	}
	
	public void plot(int x, int y) {
		drawCircle(x,y,Main.screen.width/180, "Point");
		if (naming == 1) {
			drawLine(trusty.str(naming-1), trusty.str(naming));
		}
		naming++;
	}
	
	public void drawLine(String p1, String p2) {
		PlotPoint point1 = null;
		PlotPoint point2 = null;
		
		for (Instruction i : instructions) {
			if (i.getName().equals(p1)) point1 = new PlotPoint(i.getX()+(i.getRadius()/2), i.getY()+(i.getRadius()/2));
			else if (i.getName().equals(p2)) point2 = new PlotPoint(i.getX()+(i.getRadius()/2), i.getY()+(i.getRadius()/2));
		}
		
		Instruction tempInstruction = new Instruction(point1, point2, "Line", String.valueOf(naming));
		instructions.add(tempInstruction);
		naming++;
	}
	
	public ArrayList<Instruction> findObject(int x, int y) {
		ArrayList<Instruction> possibleObjects = new ArrayList<>();
		for (Instruction i : instructions) {
			if (i.isColliding(x,y)) {
				possibleObjects.add(i);
			}
		}
		return possibleObjects;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g.create();
		for (Instruction i : instructions) {
			switch(i.getType()) {
			
			case "Line":
			    g2.setStroke(new BasicStroke(3));
				g2.drawLine(i.getP1().getX(), i.getP1().getY(), i.getP2().getX(), i.getP2().getY());
				break;
			
			case "Point":
			    g2.setStroke(new BasicStroke(3));
			    if (i.getFilled())
					g2.fillOval(i.getX(), i.getY(), i.getRadius(), i.getRadius());
			    else
					g2.drawOval(i.getX(), i.getY(), i.getRadius(), i.getRadius());
			    break;
			case "Circle":
				g2.setStroke(new BasicStroke(3));
			    if (i.getFilled())
					g2.fillOval(i.getX(), i.getY(), i.getRadius(), i.getRadius());
			    else
					g2.drawOval(i.getX(), i.getY(), i.getRadius(), i.getRadius());
			    break;
			}
		}
	}
}