package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LastBuildTestInfo {

	int pass;
	int fail;
	int skip;
	String x;
	double y;
	int total;
	
	public LastBuildTestInfo() {
		
	}


	public LastBuildTestInfo(int pass, int fail, int skip, String x, double y, int total) {
		super();
		this.pass = pass;
		this.fail = fail;
		this.skip = skip;
		this.x = x;
		this.y = y;
		this.total=total;
	}


	public int getPass() {
		return pass;
	}


	public void setPass(int pass) {
		this.pass = pass;
	}


	public int getFail() {
		return fail;
	}


	public void setFail(int fail) {
		this.fail = fail;
	}


	public int getSkip() {
		return skip;
	}


	public void setSkip(int skip) {
		this.skip = skip;
	}


	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}


	public int getTotal() {
		return total;
	}


	public void setTotal(int total) {
		this.total = total;
	}
	
	
}
