package com.andromeda.vgraph;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.JPanel;
import com.andromeda.main.Main;
import com.andromeda.main.trusty;

@SuppressWarnings("serial")
public class GraphGUI extends JPanel {

	private static Dimension size = null;
	public boolean saved = true;
	public File saveFile = null;
	private ArrayList<Instruction> instructions = new ArrayList<>();
	private HashMap<String, PlotPoint> plots = new HashMap<>();
	private int naming = 0;
	
	private int currentIndex = 0;
	
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
	
	public void drawCircle(int x, int y, int r) {
		x = x-(r/2);
		y = y-(r/2);
		
		Instruction tempInstruction = new Instruction(x, y, "circleDraw", String.valueOf(naming));
			tempInstruction.setFilled(true);
			tempInstruction.setRadius(r);
			
		instructions.add(tempInstruction);
		naming++;
	}

	public void plot(String name, int x, int y) {
		plots.put(name, new PlotPoint(x, y));
		drawCircle(x,y,Main.screen.width/180);
	}
	
	public void plot(int x, int y) {
		plots.put(trusty.str(currentIndex), new PlotPoint(x, y));
		drawCircle(x,y,Main.screen.width/180);
		currentIndex++;
	}
	
	public void drawLine(String p1, String p2) {
		PlotPoint point1 = plots.get(p1);
		PlotPoint point2 = plots.get(p2);
		
		Instruction tempInstruction = new Instruction(point1, point2, "lineDraw", String.valueOf(naming));
		instructions.add(tempInstruction);
		naming++;
	}
	
	public ArrayList<Dimension> findObject(int x, int y) {
		ArrayList<Dimension> possibleObjects = new ArrayList<>();
		for (Instruction i : instructions) {
			if (i.getBoundings().contains(x,y)) {
				possibleObjects.add(new Dimension(i.getX(), i.getY()));
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
			
			case "lineDraw":
			    g2.setStroke(new BasicStroke(3));
				g2.drawLine(i.getP1().getX(), i.getP1().getY(), i.getP2().getX(), i.getP2().getY());
				break;
			
			case "circleDraw":
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