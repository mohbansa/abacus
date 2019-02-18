package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TableLastBuildsHeader
{
    private String buildNumber;

    private String mappingNumber;

    
    
    public TableLastBuildsHeader() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableLastBuildsHeader(String buildNumber, String mappingNumber) {
		super();
		this.buildNumber = buildNumber;
		this.mappingNumber = mappingNumber;
	}

	public String getBuildNumber ()
    {
        return buildNumber;
    }

    public void setBuildNumber (String buildNumber)
    {
        this.buildNumber = buildNumber;
    }

    public String getMappingNumber ()
    {
        return mappingNumber;
    }

    public void setMappingNumber (String mappingNumber)
    {
        this.mappingNumber = mappingNumber;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [buildNumber = "+buildNumber+", mappingNumber = "+mappingNumber+"]";
    }
}
