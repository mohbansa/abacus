package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TableLastBuildsTestFileInfo
{
    private List<TableLastBuildsResult> result;

    private String moduleName;

    private List<TableLastBuildsTestMethod> testMethod;

    
    public TableLastBuildsTestFileInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public TableLastBuildsTestFileInfo(List<TableLastBuildsResult> result, String moduleName, List<TableLastBuildsTestMethod> testMethod) {
		super();
		this.result = result;
		this.moduleName = moduleName;
		this.testMethod = testMethod;
	}

	public List<TableLastBuildsResult> getResult ()
    {
        return result;
    }

    public void setResult (List<TableLastBuildsResult> result)
    {
        this.result = result;
    }

    public String getModuleName ()
    {
        return moduleName;
    }

    public void setModuleName (String moduleName)
    {
        this.moduleName = moduleName;
    }

    public List<TableLastBuildsTestMethod> getTestMethod ()
    {
        return testMethod;
    }

    public void setTestMethod (List<TableLastBuildsTestMethod> testMethod)
    {
        this.testMethod = testMethod;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [result = "+result+", moduleName = "+moduleName+", testMethod = "+testMethod+"]";
    }
}