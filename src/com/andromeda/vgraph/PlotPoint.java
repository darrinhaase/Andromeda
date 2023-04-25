package com.andromeda.vgraph;

import java.io.Serializable;

public class PlotPoint implements Serializable {

	private static final long serialVersionUID = 1L;
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
