package com.andromeda.desc;

public class DrawingText {
	
	private int x,y,size;
	private float boldness;
	private boolean italics;
	private String text;
	
	public DrawingText(int x, int y, int size, String text, float boldness) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.text = text;
		this.boldness = boldness;
	}
	
	public DrawingText(int x, int y, int size, String text, float boldness, boolean italics) {
		this.x = x;
		this.y = y;
		this.size = size;
		this.text = text;
		this.boldness = boldness;
		this.italics = italics;
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
	public int getSize() {
		return size;
	}
	public void setSize(int size) {
		this.size = size;
	}
	public float getBoldness() {
		return boldness;
	}
	public void setBoldness(float boldness) {
		this.boldness = boldness;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public boolean isItalics() {
		return this.italics;
	}
	public void setItalics(boolean italics) {
		this.italics = italics;
	}
}