package com.smart.helpers;

public class Message {
	
	private String text;
	private String color;
	
	public Message(String text, String color) {
		super();
		this.text = text;
		this.color = color;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

}
