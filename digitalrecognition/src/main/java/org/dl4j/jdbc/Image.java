package org.dl4j.jdbc;

public class Image {
	private String ImagePath;
	private String Base64;
	public Image() {}
	public Image(String ImagePath,String Base64) {
		this.ImagePath=ImagePath;
		this.Base64=Base64;
	}
	
	public void setBase64(String setBase64) {
		this.Base64=setBase64;
	}
	public String getBase64() {
		return this.Base64;
	}
	
	public void setImagePath(String ImagePath) {
		this.ImagePath=ImagePath;
	}
	public String getImagePath() {
		return this.ImagePath;
	}
	public void display() {
		System.out.println("Base64: "+this.Base64);
	}
	
}
