package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LastBuildPassPercentage {

	int failCount;
	int skipCount;
	int total;
	int build;
	
	
	
	public LastBuildPassPercentage() {
		super();
		// TODO Auto-generated constructor stub
	}
	public LastBuildPassPercentage(int failCount, int skipCount, int total, int build) {
		super();
		this.failCount = failCount;
		this.skipCount = skipCount;
		this.total = total;
		this.build = build;
	}
	public int getFailCount() {
		return failCount;
	}
	public void setFailCount(int failCount) {
		this.failCount = failCount;
	}
	public int getSkipCount() {
		return skipCount;
	}
	public void setSkipCount(int skipCount) {
		this.skipCount = skipCount;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	public int getBuild() {
		return build;
	}
	public void setBuild(int build) {
		this.build = build;
	}
	
}
