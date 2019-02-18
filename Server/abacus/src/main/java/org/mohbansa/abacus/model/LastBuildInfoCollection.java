package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class LastBuildInfoCollection {
	String key;
	List<LastBuildTestInfo> values;
	public LastBuildInfoCollection() {
		
	}
	public LastBuildInfoCollection(String key, List<LastBuildTestInfo> values) {
		super();
		this.key = key;
		this.values = values;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public List<LastBuildTestInfo> getValues() {
		return values;
	}
	public void setValues(List<LastBuildTestInfo> values) {
		this.values = values;
	}
	
	
	
}
