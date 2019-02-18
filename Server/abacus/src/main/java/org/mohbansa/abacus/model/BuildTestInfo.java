package org.mohbansa.abacus.model;



import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BuildTestInfo {
	
	 String key;
	String x;
	double y;
	int buildNumber;
	int pass;
	int total;
	int fail;
	int skip;
	
	public BuildTestInfo() {
		
	}
	
	public BuildTestInfo(String x, double y, int buildNumber, int pass,  int fail, int skip,int total) {
		super();
		
		this.x = x;
		this.y = y;
		this.buildNumber = buildNumber;
		this.pass = pass;
		this.total = total;
		this.fail = fail;
		this.skip = skip;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
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

	public int getBuildNumber() {
		return buildNumber;
	}

	public void setBuildNumber(int buildNumber) {
		this.buildNumber = buildNumber;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = pass;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
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
	
	
	
	
	

}
