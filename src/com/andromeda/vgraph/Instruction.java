package com.andromeda.vgraph;

import java.awt.Color;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.io.Serializable;

public class Instruction implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x, y, d;
	private String type,name,text;
	private boolean filled;
	private PlotPoint[] pList;
	private String superObject = "";
	public boolean active = false;
	private boolean square = false;
	private Polygon quad = new Polygon();
	
	public Polygon getQuad() {
		return quad;
	}

	public void setQuad(Polygon quad) {
		this.quad = quad;
	}

	public boolean isSquare() {
		return square;
	}

	public void setSquare(boolean square) {
		this.square = square;
	}

	public String getSuperObject() {
		return superObject;
	}

	public void setSuperObject(String superObject) {
		this.superObject = superObject;
	}

	public PlotPoint[] getpList() {
		return pList;
	}

	public void setpList(PlotPoint[] pList) {
		this.pList = pList;
	}
	private PlotPoint p1,p2;
	public String midpt = "";
	private Color color = new Color(0,0,0);
	
	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public boolean isColliding(int x, int y) {
		if (this.type.equals("Point") || this.type.equals("Text")) {
			Rectangle bound = new Rectangle(this.x, this.y, d, d);
			if (bound.contains(x,y)) return true;
			else return false;
		} else if (this.type.equals("Segment")) {
			double minX = Math.min(p1.getX(), p2.getX());
			double maxX = Math.max(p1.getX(), p2.getX());
			double minY = Math.min(p1.getY(), p2.getY());
			double maxY = Math.max(p1.getY(), p2.getY());

			if (x >= minX && x <= maxX && y >= minY && y <= maxY) {
				double A = p1.getY() - p2.getY();
				double B = p2.getX() - p1.getX();
				double C = p1.getX() * p2.getY() - p2.getX() * p1.getY();

				double distance = Math.abs(A * x + B * y + C) / Math.sqrt(A * A + B * B);

				if (distance < 3) {
				    return true;
				} else {
				    return false;
				}
			}
		} else if (this.type.equals("Circle")) {
			double distance = GraphGUI.calculateDistance(x, y, this.x+d/2, this.y+d/2);
			return Math.abs(distance - d/2) <= 5;
		} else if (this.type.equals("Quadrilateral")) {
			 for (int i = 0; i < this.quad.npoints; i++) {
                 int j = (i + 1) % this.quad.npoints;

                 int x1 = this.quad.xpoints[i];
                 int y1 = this.quad.ypoints[i];
                 int x2 = this.quad.xpoints[j];
                 int y2 = this.quad.ypoints[j];

                 double distance = pointToLineSegmentDistance(x, y, x1, y1, x2, y2);

                 double threshold = 5.0;

                 if (distance <= threshold) return true;
             }
		}
		return false;
	}
	
	private static double pointToLineSegmentDistance(int x, int y, int x1, int y1, int x2, int y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
    
        if (dx == 0 && dy == 0) {
            return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        }
    
        double t = ((x - x1) * dx + (y - y1) * dy) / (dx * dx + dy * dy);
    
        if (t < 0) { 
            return Math.sqrt(Math.pow((x - x1), 2) + Math.pow((y - y1), 2));
        } else if (t > 1) {
            return Math.sqrt(Math.pow((x - x2), 2) + Math.pow((y - y2), 2));
        } else {
            double closestX = x1 + t * dx;
            double closestY = y1 + t * dy;
            return Math.sqrt(Math.pow((x - closestX), 2) + Math.pow((y - closestY), 2));
        }
    }

	
	public static float slope(float x1, float y1, float x2, float y2) {
		if (x2 - x1 != 0) return (y2 - y1)*-1 / (x2 - x1);
		return Integer.MAX_VALUE;
	}
	
	public float[] getLineData() {
		float m = slope(p1.getX(), p1.getY(), p2.getX(), p2.getY());
		if (m == Integer.MAX_VALUE) {
			return null;
		} else {
			float b = p1.getY() - m * p1.getX();
			return new float[] {m, b, (p2.getY()-p1.getY()) * -1, p2.getX()-p1.getX()};
		}
	}
	
	public Instruction(int x, int y, String type, String name) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.name = name;
	}
	
	public Instruction(int x, int y, String text, String type, String name) {
    		this.x = x;
    		this.y = y;
    		this.type = type;
    		this.name = name;
    		this.text = text;
    	}
	
	public Instruction(PlotPoint p1, PlotPoint p2, String type, String name) {
		this.p1 = p1;
		this.p2 = p2;
		this.type = type;
		this.name = name;
	}
	
	public Instruction(String type, String name, PlotPoint... plotList) {
		this.pList = plotList;
		this.type = type;
		this.name = name;
	}
	
	public Instruction() {}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getText() {
	    return this.text;
	}
	
	public void setText(String text) {
     	 this.text = text;
     }
	
	
	public PlotPoint getP1() {
		return p1;
	}

	public void setP1(PlotPoint p1) {
		this.p1 = p1;
	}
	public PlotPoint getP2() {
		return p2;
	}
	public void setP2(PlotPoint p2) {
		this.p2 = p2;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getDiameter() {
		return d;
	}
	public void setDiameter(int d) {
		this.d = d;
	}
	public boolean getFilled() {
		return filled;
	}
	public void setFilled(boolean filled) {
		this.filled = filled;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}

}
