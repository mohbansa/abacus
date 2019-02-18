package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;



@XmlRootElement
public class BuildPassStatus {

	int x;
	double y;
	int buildNumber;
	int passCount;
	int totalCount;
	
	public BuildPassStatus()
	{
		
	}
	public BuildPassStatus(int x, double y, int buildNumber, int passCount, int totalCount) {
		
		super();
		this.x = x;
		this.y = y;
		this.buildNumber = buildNumber;
		this.passCount = passCount;
		this.totalCount = totalCount;
	}
	public int getX() {
		return x;
	}
	public void setX(int x) {
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
	public int getPassCount() {
		return passCount;
	}
	public void setPassCount(int passCount) {
		this.passCount = passCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	
}
