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
	private int naming = 1;
	public boolean currentlyDrawing = false;
	
	/*
	 * DELETE FOR PRODUCTION
	 */
	public int plotnum = 1;
	
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
	
	public void plot(int x, int y) {
		
		Instruction tempInstruction = new Instruction(x-(Main.screen.width/180/2), y-(Main.screen.width/180/2), "Point", String.valueOf(plotnum));
		 tempInstruction.setFilled(true);
	     tempInstruction.setDiameter(Main.screen.width/180);
		
		if (Main.selectedTool.equals("point") || Main.selectedTool.equals("segment")) {
			tempInstruction.active = true;
		}
		tempInstruction.setSuperObject(trusty.str(naming));
		instructions.add(tempInstruction);

		switch (Main.selectedTool) {
			
			case "textbox":
				tempInstruction.setSuperObject(trusty.str(naming));
			    drawTextBox(x, y);
			    break;
			    

			case "point":
				break;

			case "segment":
				if (plotnum % 2 == 0) {
					tempInstruction.setSuperObject(trusty.str(naming));
					drawLine(trusty.str(plotnum-1), trusty.str(plotnum));
					currentlyDrawing = false;
				} else {
					tempInstruction.setSuperObject(trusty.str(naming));
					currentlyDrawing = true;
				}
				break;

			case "circle":
				if (plotnum % 2 == 0) {
					tempInstruction.setSuperObject(trusty.str(naming));
					drawCircle(trusty.str(plotnum-1), trusty.str(plotnum));
					currentlyDrawing = false;
				} else {
					tempInstruction.setSuperObject(trusty.str(naming));
					currentlyDrawing = true;
				}
				break;

			case "quadrilateral":
				if (plotnum % 4 == 0) {
					tempInstruction.setSuperObject(trusty.str(naming));
					drawRectangle(trusty.str(plotnum-3), trusty.str(plotnum-2), trusty.str(plotnum-1), trusty.str(plotnum));
					currentlyDrawing = false;
				} else {
					tempInstruction.setSuperObject(trusty.str(naming));
					currentlyDrawing = true;
				}
				break;

			default:
				System.out.println("Unknown tool");
				break;

		}
		plotnum++;
	}
	
	public void drawCircle(String midPoint, String outerPoint) {
		PlotPoint mid = null;
		PlotPoint out = null;
		
		for (Instruction i : instructions) {
			if (i.getName().equals(midPoint)) mid = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(outerPoint)) out = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
		}
		
		int diameter = (int) Math.round(calculateDistance(mid.getX(), mid.getY(), out.getX(), out.getY()))*2;
		
		Instruction tempInstruction = new Instruction(mid.getX()-(diameter/2), mid.getY()-(diameter/2), "Circle", String.valueOf(naming));
			tempInstruction.setFilled(false);
			tempInstruction.setDiameter(diameter);
		
		instructions.add(tempInstruction);
	}
	
	public void drawTextBox(int x, int y) {
        			
    	    Instruction tempInstruction = new Instruction(x-(Main.screen.width/180/2), y-(Main.screen.width/180/2), "Example Text", "Text", String.valueOf(naming));
			 	tempInstruction.setFilled(true);
		        tempInstruction.setDiameter(Main.screen.width/180);
		        
		        instructions.add(tempInstruction);

    		naming++;
    }
	
	public void drawRectangle(String p1, String p2, String p3, String p4) {
		PlotPoint point1 = null;
		PlotPoint point2 = null;
		PlotPoint point3 = null;
		PlotPoint point4 = null;
		
		for (Instruction i : instructions) {
			if (i.getName().equals(p1)) point1 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p2)) point2 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p3)) point3 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p4)) point4 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
		}
		
		Instruction tempInstruction = new Instruction("Rectangle", String.valueOf(naming), point1, point2, point3, point4);
		instructions.add(tempInstruction);
		naming++;
	}
	
	public void drawTriangle(String p1, String p2, String p3) {
		PlotPoint point1 = null;
		PlotPoint point2 = null;
		PlotPoint point3 = null;
		
		for (Instruction i : instructions) {
			if (i.getName().equals(p1)) point1 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p2)) point2 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p3)) point3 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
		}
		
		Instruction tempInstruction = new Instruction("Triangle", String.valueOf(naming), point1, point2, point3);
		instructions.add(tempInstruction);
		naming++;
	}
	
	public void drawLine(String p1, String p2) {
		PlotPoint point1 = null;
		PlotPoint point2 = null;
		
		for (Instruction i : instructions) {
			if (i.getName().equals(p1)) point1 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
			else if (i.getName().equals(p2)) point2 = new PlotPoint(i.getX()+(i.getDiameter()/2), i.getY()+(i.getDiameter()/2));
		}
		
		Instruction tempInstruction = new Instruction(point1, point2, "Segment", String.valueOf(naming));
		instructions.add(tempInstruction);
		naming++;
	}
	
	public ArrayList<Instruction> findObject(int x, int y) {
		ArrayList<Instruction> possibleObjects = new ArrayList<>();
		for (Instruction i : instructions) {
			if (i.isColliding(x,y) && !i.getName().contains("midpoint")) {
				if (i.getType().equals("Point")) {
					if (i.active) {
						possibleObjects.add(i);
					}
				} else {
					possibleObjects.add(i);
				}
			}
		}
		return possibleObjects;
	}
	
	public static double calculateDistance(int x1, int y1, int x2, int y2) {
	    int deltaX = x2 - x1;
	    int deltaY = y2 - y1;
	    return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
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
				
			case "Text":
				g2.setStroke(new BasicStroke(3));
				g2.drawString(i.getText(), i.getX() + Main.screen.width/100, i.getY() + Main.screen.height/100);
				break;
				
			case "Segment":
			    g2.setStroke(new BasicStroke(3));
				g2.drawLine(i.getP1().getX(), i.getP1().getY(), i.getP2().getX(), i.getP2().getY());
				break;
			
			case "Point":
				g2.setColor(i.getColor());
			    g2.setStroke(new BasicStroke(3));
			    if (i.getFilled())
					g2.fillOval(i.getX(), i.getY(), i.getDiameter(), i.getDiameter());
			    else
					g2.drawOval(i.getX(), i.getY(), i.getDiameter(), i.getDiameter());
			    break;
			case "Circle":
				g2.setStroke(new BasicStroke(3));
			    if (i.getFilled())
					g2.fillOval(i.getX(), i.getY(), i.getDiameter(), i.getDiameter());
			    else
					g2.drawOval(i.getX(), i.getY(), i.getDiameter(), i.getDiameter());
			    break;
			case "Rectangle":
				g2.setStroke(new BasicStroke(3));
				PlotPoint[] pointsInRectangle = i.getpList();
				g2.drawLine(pointsInRectangle[0].getX(), pointsInRectangle[0].getY(), pointsInRectangle[1].getX(), pointsInRectangle[1].getY());
				g2.drawLine(pointsInRectangle[1].getX(), pointsInRectangle[1].getY(), pointsInRectangle[2].getX(), pointsInRectangle[2].getY());
				g2.drawLine(pointsInRectangle[2].getX(), pointsInRectangle[2].getY(), pointsInRectangle[3].getX(), pointsInRectangle[3].getY());
				g2.drawLine(pointsInRectangle[3].getX(), pointsInRectangle[3].getY(), pointsInRectangle[0].getX(), pointsInRectangle[0].getY());
				break;
			}
		}
		g.dispose();
	}
}
