package com.andromeda.vgraph;

import java.awt.Rectangle;
import java.io.Serializable;

public class Instruction implements Serializable {

	private static final long serialVersionUID = 1L;
	private int x, y, r;
	private String type,name;
	private boolean filled;
	private PlotPoint p1,p2;
	
	public Rectangle getBoundings() {
		Rectangle bound = null;
		if (p1 == null && p2 == null) {
			bound = new Rectangle(x, y, r, r);
		}
		return bound;
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
