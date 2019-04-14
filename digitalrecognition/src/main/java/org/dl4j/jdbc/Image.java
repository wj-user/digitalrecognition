package org.dl4j.jdbc;

public class Image {
	private String ImagePath;
	
	public Image() {}
	public Image(String ImagePath) {
		this.ImagePath=ImagePath;
	}
	
	public void setImagePath(String ImagePath) {
		this.ImagePath=ImagePath;
	}
	public String getImagePath() {
		return this.ImagePath;
	}
	public void display() {
		System.out.println("ImagePath: "+this.ImagePath);
	}
	
}
