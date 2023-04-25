package com.andromeda.vgraph;

import java.awt.Rectangle;
import java.io.Serializable;

public class Instruction implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x, y, r;
	private String type,name;
	private boolean filled;
	private PlotPoint p1,p2;
	
	public boolean isColliding(int x, int y) {
		if (this.type.equals("Point")) {
			Rectangle bound = new Rectangle(this.x, this.y, r, r);
			if (bound.contains(x,y)) return true;
			else return false;
		} else {
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
		}
		return false;
	}
	
	public double[] getLineData() {
		double A = p1.getY() - p2.getY();
		double B = p2.getX() - p1.getX();
		double C = p1.getX() * p2.getY() - p2.getX() * p1.getY();
		
		return new double[] {A,B,C};
	}
	
	public Instruction(int x, int y, String type, String name) {
		this.x = x;
		this.y = y;
		this.type = type;
		this.name = name;
	}
	
	public Instruction(PlotPoint p1, PlotPoint p2, String type, String name) {
		this.p1 = p1;
		this.p2 = p2;
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
	public int getRadius() {
		return r;
	}
	public void setRadius(int r) {
		this.r = r;
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
