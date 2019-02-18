package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class BuildJobParameters {
	
	public String parameterName;
	public List<String> parameterValues;
	
	public BuildJobParameters() {
		
	}

	public BuildJobParameters(String parameterName, List<String> parameterValues) {
		super();
		this.parameterName = parameterName;
		this.parameterValues = parameterValues;
	}

	public String getParameterName() {
		return parameterName;
	}

	public void setParameterName(String parameterName) {
		this.parameterName = parameterName;
	}

	public List<String> getParameterValues() {
		return parameterValues;
	}

	public void setParameterValues(List<String> parameterValues) {
		this.parameterValues = parameterValues;
	}
	
	

}
