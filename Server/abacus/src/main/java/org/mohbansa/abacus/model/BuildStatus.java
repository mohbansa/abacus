package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BuildStatus {

	private int number;
	private String result;
	
	public BuildStatus()
	{
		
	}
	
	public BuildStatus(int number, String result) {
		super();
		this.number = number;
		this.result = result;
	}
	
	public int getNumber() {
		return number;
	}
	public void setNumber(int number) {
		this.number = number;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	
	
}
