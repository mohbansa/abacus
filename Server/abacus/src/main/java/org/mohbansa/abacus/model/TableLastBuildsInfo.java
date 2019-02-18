package org.mohbansa.abacus.model;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class TableLastBuildsInfo {
	
	 private List<TableLastBuildsHeader> header;

	    private List<TableLastBuildsTestFileInfo> testFileData;

	    
	    public TableLastBuildsInfo() {
			super();
			// TODO Auto-generated constructor stub
		}

		public TableLastBuildsInfo(List<TableLastBuildsHeader> header, List<TableLastBuildsTestFileInfo> testFileData) {
			super();
			this.header = header;
			this.testFileData = testFileData;
		}

		public List<TableLastBuildsHeader> getHeader ()
	    {
	        return header;
	    }

	    public void setHeader (List<TableLastBuildsHeader> header)
	    {
	        this.header = header;
	    }

	    public List<TableLastBuildsTestFileInfo> getTestFileData ()
	    {
	        return testFileData;
	    }

	    public void setTestFileData (List<TableLastBuildsTestFileInfo> testFileData)
	    {
	        this.testFileData = testFileData;
	    }

	    @Override
	    public String toString()
	    {
	        return "ClassPojo [header = "+header+", testFileData = "+testFileData+"]";
	    }
	
}








			
			
			
			