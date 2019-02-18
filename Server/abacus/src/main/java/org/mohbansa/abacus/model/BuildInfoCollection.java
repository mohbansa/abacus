package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class BuildInfoCollection {
	String key;
	List<BuildTestInfo> values;
	public BuildInfoCollection()
	{
		
	}
	
	public BuildInfoCollection(String key, List<BuildTestInfo> values) {
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

	public List<BuildTestInfo> getValues() {
		return values;
	}

	public void setValues(List<BuildTestInfo> values) {
		this.values = values;
	}
	

}
