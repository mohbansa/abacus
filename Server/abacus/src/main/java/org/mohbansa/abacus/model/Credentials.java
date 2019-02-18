package org.mohbansa.abacus.model;

public class Credentials {

	public String url;
	public String jobname;
	public String username;
	public String password;
	public Credentials() {
	
	}
	
	public Credentials(String url, String jobname, String username, String password) {
		super();
		this.url = url;
		this.jobname = jobname;
		this.username = username;
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getJobname() {
		return jobname;
	}

	public void setJobname(String jobname) {
		this.jobname = jobname;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
}
