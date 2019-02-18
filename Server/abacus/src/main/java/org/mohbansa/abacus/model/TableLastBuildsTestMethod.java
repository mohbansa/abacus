package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;


@XmlRootElement
public class TableLastBuildsTestMethod
{
   private List<TableLastBuildsResult > result;

   private String testName;
  

   public TableLastBuildsTestMethod(List<TableLastBuildsResult > result, String testName) {
	super();
	this.result = result;
	this.testName = testName;
	
}

public TableLastBuildsTestMethod() {
	super();
	// TODO Auto-generated constructor stub
}


public List<TableLastBuildsResult > getResult ()
   {
       return result;
   }

   public void setResult (List<TableLastBuildsResult > result)
   {
       this.result = result;
   }

   public String getTestName ()
   {
       return testName;
   }

   public void setTestName (String testName)
   {
       this.testName = testName;
   }

   @Override
   public String toString()
   {
       return "ClassPojo [result = "+result+", testName = "+testName+"]";
   }
}
