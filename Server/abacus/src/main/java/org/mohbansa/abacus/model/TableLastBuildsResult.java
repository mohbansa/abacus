package org.mohbansa.abacus.model;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TableLastBuildsResult
{
    private String buildNumber;

    private String status;

    private String exception;
    private String parameters;
    
    public TableLastBuildsResult() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableLastBuildsResult(String buildNumber, String status) {
		super();
		this.buildNumber = buildNumber;
		this.status = status;
	}

	public TableLastBuildsResult(String buildNumber, String status, String exception, String parameters) {
		super();
		this.buildNumber = buildNumber;
		this.status = status;
		this.exception = exception;
		this.parameters = parameters;
	}

	public String getException() {
		return exception;
	}

	public void setException(String exception) {
		this.exception = exception;
	}

	public String getParameters() {
		return parameters;
	}

	public void setParameters(String parameters) {
		this.parameters = parameters;
	}

	public String getBuildNumber ()
    {
        return buildNumber;
    }

    public void setBuildNumber (String buildNumber)
    {
        this.buildNumber = buildNumber;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [buildNumber = "+buildNumber+", status = "+status+"]";
    }
}