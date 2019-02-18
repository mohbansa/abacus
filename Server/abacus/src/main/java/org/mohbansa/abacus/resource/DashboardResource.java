package org.mohbansa.abacus.resource;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.PostConstruct;
import javax.json.JsonObject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriInfo;

import org.mohbansa.abacus.model.BuildTestInfo;
import org.mohbansa.abacus.model.Credentials;
import org.mohbansa.abacus.model.LastBuildInfoCollection;
import org.mohbansa.abacus.model.LastBuildPassPercentage;
import org.mohbansa.abacus.model.TableLastBuildsInfo;
import org.mohbansa.abacus.model.TableLastBuildsTestFileInfo;
import org.jvnet.hk2.annotations.Optional;
import org.mohbansa.abacus.common.BaseClass;
import org.mohbansa.abacus.model.BuildInfoCollection;
import org.mohbansa.abacus.model.BuildJobParameters;
import org.mohbansa.abacus.model.BuildPassStatus;
import org.mohbansa.abacus.model.BuildStatus;
import org.mohbansa.abacus.service.DashboardService;





@Path("/dashboard")
public class DashboardResource {

DashboardService service=null;
 Credentials credentials=new Credentials();

	@POST
	@Path("/buildPassStatus")
	@Consumes("application/x-www-form-urlencoded")
	@Produces(MediaType.APPLICATION_JSON)
	public List<BuildPassStatus> getBuildPassStatus(InputStream data,
			@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@Context UriInfo ui)
	{
		System.out.println("Hello");
		System.out.println(startDate);
		System.out.println(endDate);
		System.out.println(ui.getQueryParameters().keySet().size());
		MultivaluedMap<String, String> parameters=ui.getQueryParameters();
		for (Entry<String, List<String>> parameter : parameters.entrySet()) {
			System.out.println(parameter.getKey()+" "+parameter.getValue());
		}
	
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,parameters);
		
		
		if((!startDate.toString().equals("undefined")) &&(!endDate.toString().equals("undefined")) && parameters.size()==2 )
				return service.getAllBuildPassStatus(startDate,endDate);
		if(!startDate.toString().equals("undefined") && !endDate.toString().equals("undefined") && parameters.size()>2)
			return service.getAllBuildPassStatus(startDate,endDate,parameters);
	
		
		return service.getAllBuildPassStatus(parameters);
	}
	
	@POST
	@Path("/buildPassFailSkipStatus")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<BuildInfoCollection> getBuildPassFailSkipStatus(
			@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@Context UriInfo ui
			)
	{
		MultivaluedMap<String, String> parameters=ui.getQueryParameters();
		System.out.println(parameters);
		System.out.println(parameters.values());
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,parameters);
		System.out.println(url+" "+username);
		if((!startDate.toString().equals("undefined")) &&(!endDate.toString().equals("undefined")) && parameters.size()==2 )
			return service.getAllBuildPassFailSkipStatus(startDate,endDate);
		if(!startDate.toString().equals("undefined") && !endDate.toString().equals("undefined") && parameters.size()>2)
		return service.getAllBuildPassFailSkipStatus(startDate,endDate,parameters);

		return service.getAllBuildPassFailSkipStatus(parameters);
	}
	
	
	@POST
	@Path("/lastBuildTestInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<LastBuildInfoCollection> getLastBuildTestInfo(InputStream data,
			@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@Context UriInfo ui)
	{
	
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,ui.getQueryParameters());
		MultivaluedMap<String, String> parameters=ui.getQueryParameters();
		
		return service.getLastBuildInfoStatus();
	}
	
	
	@POST
	@Path("/timeStamp")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public String getLastBuildTimeStamp(
			@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@Context UriInfo ui)
		{
	
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,ui.getQueryParameters());
		return service.getLastBuildTimeStamp();
	}
	
	@POST
	@Path("/lastBuildPassPercentage")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public LastBuildPassPercentage getLastBuildPassPercentage(@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start") String startDate,
			@QueryParam("end") String endDate,
			@Context UriInfo ui)
		{
	
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,ui.getQueryParameters());
		return service.getLastBuildPassPercentage();
	}
	
	@POST
	@Path("/tableLastTenBuildInfo")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	
	public List<TableLastBuildsTestFileInfo> getTableLastBuildInfo(@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password,
			@QueryParam("start")  String startDate ,
			@QueryParam("end") String endDate,
			@Context UriInfo ui)
		{
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,ui.getQueryParameters());
	
		return service.getTableLastBuildInfo();
		
	}
	/*
	@POST
	@Path("/jobParameters")
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.APPLICATION_JSON)
	public List<BuildJobParameters> getJobParameters(
			@HeaderParam("jenkinURL") String url,
			@HeaderParam("jobName") String jobName,
			@HeaderParam("userName") String username,
			@HeaderParam ("passWord") String password)
	{
		credentials.setUrl(url);
		credentials.setUsername(username);
		credentials.setPassword(password);
		credentials.setJobname(jobName);
		service=new DashboardService(credentials,startDate,endDate,parameters);
		return service.getJobParameters();
	}
	
	*/
	
}
