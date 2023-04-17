package com.andromeda.vgraph;

public class PlotPoint {

	private int x, y;

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
	
	public void update(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public PlotPoint(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public String convert() {
		return String.valueOf(this.x)+","+String.valueOf(this.y);
	}
}