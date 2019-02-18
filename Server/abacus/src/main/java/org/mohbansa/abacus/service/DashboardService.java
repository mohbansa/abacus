package org.mohbansa.abacus.service;

import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.Date;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.core.MultivaluedMap;import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.ParseException;
import org.mohbansa.abacus.common.BaseClass;
import org.mohbansa.abacus.model.BuildTestInfo;
import org.mohbansa.abacus.model.Credentials;
import org.mohbansa.abacus.model.LastBuildInfoCollection;
import org.mohbansa.abacus.model.LastBuildTestInfo;
import org.mohbansa.abacus.model.TableLastBuildsHeader;
import org.mohbansa.abacus.model.TableLastBuildsInfo;
import org.mohbansa.abacus.model.TableLastBuildsResult;
import org.mohbansa.abacus.model.TableLastBuildsTestFileInfo;
import org.mohbansa.abacus.model.TableLastBuildsTestMethod;
import org.mohbansa.abacus.model.LastBuildPassPercentage;
import org.mohbansa.abacus.model.BuildInfoCollection;
import org.mohbansa.abacus.model.BuildJobParameters;
import org.mohbansa.abacus.model.BuildPassStatus;
import org.mohbansa.abacus.model.BuildStatus;

import java.util.Date.*;

import com.offbytwo.jenkins.JenkinsServer;
import com.offbytwo.jenkins.model.Build;

public class DashboardService  {
	
	Credentials credentials;
	
	JenkinsServer jenkins;
	static int  lastBuildRun=0;
	static List<Build> successfulBuilds=new ArrayList<>();
	//both should be same
	
	int buildStatusSize=10;
	int threshold=20;
	int x=1;
	
	public DashboardService(Credentials credentials, String startDate, String endDate,MultivaluedMap<String, String> parameters) {
		this.credentials=credentials;
		
		jenkins = new JenkinsServer(URI.create(credentials.getUrl()),credentials.getUsername(), credentials.getPassword());
		//System.out.println(startDate+" "+endDate);
		if((!startDate.toString().equals("undefined")) &&(!endDate.toString().equals("undefined")) && parameters.size()==2 )
			getSuccessfullyRunLastBuild(startDate,endDate);
		else
		if(!startDate.toString().equals("undefined") && !endDate.toString().equals("undefined") && parameters.size()>2)
			getSuccessfullyRunLastBuild(startDate,endDate,parameters);
		else
			getSuccessfullyRunLastBuild();
		
		//System.out.println("LastBuildNumber:"+lastBuildRun);
	}
	
	private void getSuccessfullyRunLastBuild()
	{
		
		try
		{
			
			List<Build> builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();	
			
			
			for (Build build : builds) {
				String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?depth=1";
				try
				{
					BaseClass.getResponse(URLS);
					lastBuildRun=build.getNumber();
					break;
				
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			////System.out.println("Last Successfully Run build:"+lastBuildRun);
		}
		catch(Exception e)
		{
			////System.out.println("Exception:"+e);
		}
		
	}



	private void getSuccessfullyRunLastBuild(String startDate, String endDate,
			MultivaluedMap<String, String> parameters) {
		try
		{
			List<Build> builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();	
			////System.out.println(builds.size());
			
			for (Build build : builds) {
				String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?depth=1";
				try
				{
					BaseClass.getResponse(URLS);
					if(checkBuildExistInRange(build, startDate, endDate))
					{
						if(BaseClass.checkUIParamtersExistInReport(parameters,build.details().getParameters(),credentials.getJobname()))
						{
							lastBuildRun=build.getNumber();
							break;
						}
					}
					else
					if(checkDateOutOfRange(build, startDate))
						break;
				
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			////System.out.println("Last Successfully Run build:"+lastBuildRun);
		}
		catch(Exception e)
		{
			////System.out.println("Exception:"+e);
		}
		
	}



	private void getSuccessfullyRunLastBuild(String startDate, String endDate) {
		try
		{
			List<Build> builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();	
			////System.out.println(builds.size());
			
			for (Build build : builds) {
				String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?depth=1";
				try
				{
					BaseClass.getResponse(URLS);
					if(checkBuildExistInRange(build, startDate, endDate))
					{
						//System.out.println("build:"+build.getNumber());
						
							lastBuildRun=build.getNumber();
							break;
					}
					else
					if(checkDateOutOfRange(build, startDate))
						break;
				
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			////System.out.println("Last Successfully Run build:"+lastBuildRun);
		}
		catch(Exception e)
		{
			////System.out.println("Exception:"+e);
		}
		
	}



	


	public List<BuildPassStatus> getAllBuildPassStatus(MultivaluedMap<String, String> uiParameters) 
	{
		List<BuildPassStatus> buildPassStatus=new ArrayList<>();
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, buildStatusSize);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			for (Build build : builds) {
				
				try
				{
					//System.out.println("URL:"+credentials.getUrl());
					//System.out.println("build number:"+build.getNumber());
					String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
					String responseInString=BaseClass.getResponse(URLS);
					
					
					JsonReader jsonReader = Json.createReader(new StringReader(responseInString));
					JsonObject object = jsonReader.readObject();
					jsonReader.close();
					
					//check build will be part of filter or not
					boolean flag=false;
				if(uiParameters.size()>2)
				
					flag=BaseClass.checkUIParamtersExistInReport(uiParameters,build.details().getParameters(),credentials.getJobname());
				
				else
					flag=true;
					if(flag)
					{
						int failCount=Integer.parseInt(object.get("failCount").toString());
						int skipCount=Integer.parseInt(object.get("skipCount").toString());
						int totalCount=Integer.parseInt(object.get("total").toString());
						int passCount=totalCount-skipCount-failCount;
						double y=((double)passCount/totalCount) * 100;
						
						buildPassStatus.add(new BuildPassStatus(x++, y, build.getNumber(), passCount, totalCount)	);
						//System.out.println("firstx="+x);
						if(x>=buildStatusSize)
							break;
					}
					
				}
				catch(Exception e)
				{
					//System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
			
	
		
		return buildPassStatus;
		
	}

	public List<BuildInfoCollection> getAllBuildPassFailSkipStatus(MultivaluedMap<String, String> uiParameters) {
		
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, buildStatusSize);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<BuildTestInfo> buildPassStatus=new ArrayList<>();
		List<BuildTestInfo> buildFailStatus=new ArrayList<>();
		List<BuildTestInfo> buildSkipStatus=new ArrayList<>();
		
		
		
			for (Build build : builds) {
				try
				{
					////System.out.println("url:"+credentials.getUrl());
					//System.out.println("build number2:"+build.getNumber());
					String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
					String responseInString=BaseClass.getResponse(URLS);
					
					
					JsonObject object = BaseClass.convertJSONObject(responseInString);
					boolean flag=false;
					if(uiParameters.size()>2)
					
						flag=BaseClass.checkUIParamtersExistInReport(uiParameters,build.details().getParameters(),credentials.getJobname());
					
					else
						flag=true;
					
					
					if(flag)
					{
						int failCount=Integer.parseInt(object.get("failCount").toString());
						int skipCount=Integer.parseInt(object.get("skipCount").toString());
						int totalCount=Integer.parseInt(object.get("total").toString());
						
						int passCount=totalCount-skipCount-failCount;
						
						double yPass=(double)passCount/totalCount * 100;
						double yFail=(double)failCount/totalCount * 100;
						double ySkip=(double)skipCount/totalCount * 100;
						
						buildPassStatus.add(new BuildTestInfo(("#"+x),yPass, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
					
						buildFailStatus.add(new BuildTestInfo(("#"+x),yFail, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
					
						buildSkipStatus.add(new BuildTestInfo(("#"+x),ySkip, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
						 x++;
						 //System.out.println("SecondX="+x);
						 if(x>=buildStatusSize)
								break;
					}
					
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
		
		 
		////System.out.println("Build collection");
		List<BuildInfoCollection> output=new ArrayList<BuildInfoCollection>(); 
		output.add(new BuildInfoCollection("Passed", buildPassStatus));
		output.add(new BuildInfoCollection("Failed", buildFailStatus));
		output.add(new BuildInfoCollection("Skipped", buildSkipStatus));
 		return output;
	}

	public List<LastBuildInfoCollection> getLastBuildInfoStatus() {
		
		List<LastBuildTestInfo> lastBuildTestInfoPass=new ArrayList<>();
		List<LastBuildTestInfo> lastBuildTestInfoFail=new ArrayList<>();
		List<LastBuildTestInfo> lastBuildTestInfoSkip=new ArrayList<>();
		
		
		
		String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+lastBuildRun+"/testngreports/api/json?tree=package[classs[name,fail,skip,totalCount]]";
		String responseInString=BaseClass.getResponse(URLS);
		////System.out.println(responseInString);
		
	
		JsonObject object = BaseClass.convertJSONObject(responseInString);
		JsonArray packageArray=BaseClass.convertJSONArray(object.get("package").toString());
		for (JsonValue packages : packageArray) {
			
			JsonObject classes=BaseClass.convertJSONObject(packages.toString());
			JsonArray classArray=BaseClass.convertJSONArray(classes.get("classs").toString());
			for (JsonValue tests : classArray) {
				JsonObject test=BaseClass.convertJSONObject(tests.toString());
				String testCaseName=test.get("name").toString().replaceAll("^\"|\"$", "");
				int failCount=Integer.parseInt(test.get("fail").toString());
				int skipCount=Integer.parseInt(test.get("skip").toString());
				int totalCount=Integer.parseInt(test.get("totalCount").toString());
				int passCount=totalCount-skipCount-failCount;
				
				double yPass=(double)passCount/totalCount * 100;
				double yFail=(double)failCount/totalCount * 100;
				double ySkip=(double)skipCount/totalCount * 100;
				
				////System.out.println("Test Case:"+testCaseName);
				////System.out.println("Fail:"+failCount+" Skip:"+skipCount+" Pass:"+passCount);
				lastBuildTestInfoPass.add(new LastBuildTestInfo(passCount, failCount, skipCount, testCaseName, yPass, totalCount));
				lastBuildTestInfoFail.add(new LastBuildTestInfo(passCount, failCount, skipCount, testCaseName, yFail, totalCount));
				lastBuildTestInfoSkip.add(new LastBuildTestInfo(passCount, failCount, skipCount, testCaseName, ySkip, totalCount));
			}
			
		}
		////System.out.println("Build collection");
		List<LastBuildInfoCollection> output=new ArrayList<LastBuildInfoCollection>(); 
		output.add(new LastBuildInfoCollection("Passed", lastBuildTestInfoPass));
		output.add(new LastBuildInfoCollection("Failed", lastBuildTestInfoFail));
		output.add(new LastBuildInfoCollection("Skipped", lastBuildTestInfoSkip));
 		return output;
		
	}

	public String getLastBuildTimeStamp() {
		int buildIndex=0;
		
		try
		{
			//System.out.println("BuildTime:"+lastBuildRun);
			List<Build> builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();	
			//System.out.println(builds.size());
			if(builds.size()>0)
				buildIndex=builds.get(0).getNumber()-lastBuildRun;			
			String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+lastBuildRun+"/testngreports/api/json?depth=1";
			BaseClass.getResponse(URLS);
			String timeStamp = new SimpleDateFormat("dd-M-yyyy hh:mm:ss").format(builds.get(buildIndex).details().getTimestamp());
			return timeStamp;
		}
		catch(Exception e)
		{
			//System.out.println("Exception:"+e);
		}
		
	
	return null;
	
	}

	public List<BuildJobParameters> getJobParameters() {
		List<BuildJobParameters> jobParameters=new ArrayList<BuildJobParameters>();
		
		try
		{
			String url=credentials.getUrl()+"/job/"+credentials.getJobname()+"/api/json?tree=actions[parameterDefinitions[name,choices,type]]";
			String response=BaseClass.getResponse(url);
			JsonArray filterParamter=BaseClass.convertJSONArray(BaseClass.convertJSONObject(response).get("actions").toString());
			for (JsonValue jsonValue : filterParamter) {
				if(BaseClass.convertJSONObject(jsonValue.toString()).get("parameterDefinitions")!=null)
				{
					JsonArray parameters=BaseClass.convertJSONArray(BaseClass.convertJSONObject(jsonValue.toString()).get("parameterDefinitions").toString());
					for (JsonValue parameter : parameters) {
						JsonObject choices=BaseClass.convertJSONObject(parameter.toString());
						String type=choices.get("type").toString().replace("\"", "");
						if(type.equals("ChoiceParameterDefinition"))
						{
							String name=choices.get("name").toString().replace("\"", "");
							//System.out.println("Name:"+name);
							Collection<JsonValue> nameOptions=BaseClass.convertJSONArray((choices.get("choices").toString()));
							//System.out.println(nameOptions);
							ArrayList<String> filterName=new ArrayList<>();
							for (JsonValue nameOption : nameOptions) 
								filterName.add(nameOption.toString().replace("\"", ""));
							jobParameters.add(new BuildJobParameters(name, filterName));
							//System.out.println(jobParameters.get(0).getParameterValues());
						}
					}
				}
			}
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
			//System.out.println("Exception:"+e);
		}
		return jobParameters;
	}
	
	

	public List<BuildPassStatus> getAllBuildPassStatus(String startDate, String endDate)  {
		List<BuildPassStatus> buildPassStatus=new ArrayList<>();
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, 10);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int x=1;
			for (Build build : builds) {
				
				
				try
				{
					if(checkBuildExistInRange(build, startDate, endDate))
					{
						//System.out.println("URL:"+credentials.getUrl());
						//System.out.println("build number:"+build.getNumber());
						String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
						String responseInString=BaseClass.getResponse(URLS);
						
						
						JsonReader jsonReader = Json.createReader(new StringReader(responseInString));
						JsonObject object = jsonReader.readObject();
						jsonReader.close();
						
						
						int failCount=Integer.parseInt(object.get("failCount").toString());
							int skipCount=Integer.parseInt(object.get("skipCount").toString());
							int totalCount=Integer.parseInt(object.get("total").toString());
							int passCount=totalCount-skipCount-failCount;
							double y=((double)passCount/totalCount) * 100;
							
							buildPassStatus.add(new BuildPassStatus(x++, y, build.getNumber(), passCount, totalCount)	);
							if(x>=threshold)
								break;
					}
					else
					if(checkDateOutOfRange(build, startDate))
						break;
					
					
				}
				catch(Exception e)
				{
					//System.out.println(e);
					//System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
			
	
		
		return buildPassStatus;
		
	}



	public List<BuildPassStatus> getAllBuildPassStatus(String startDate, String endDate,
			MultivaluedMap<String, String> parameters) {
		List<BuildPassStatus> buildPassStatus=new ArrayList<>();
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, 10);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		int x=1;
			for (Build build : builds) {
				
				
				try
				{
					
					if(checkBuildExistInRange(build, startDate, endDate))
					{
						//System.out.println("URL:"+credentials.getUrl());
						//System.out.println("build number:"+build.getNumber());
						String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
						String responseInString=BaseClass.getResponse(URLS);
						
						
						JsonReader jsonReader = Json.createReader(new StringReader(responseInString));
						JsonObject object = jsonReader.readObject();
						jsonReader.close();
						
						boolean flag=false;
						flag=BaseClass.checkUIParamtersExistInReport(parameters,build.details().getParameters(),credentials.getJobname());
						
						if(flag)
						{

							int failCount=Integer.parseInt(object.get("failCount").toString());
								int skipCount=Integer.parseInt(object.get("skipCount").toString());
								int totalCount=Integer.parseInt(object.get("total").toString());
								int passCount=totalCount-skipCount-failCount;
								double y=((double)passCount/totalCount) * 100;
								
								buildPassStatus.add(new BuildPassStatus(x++, y, build.getNumber(), passCount, totalCount)	);
								if(x>=threshold)
									break;
						}
					}
					else
					if(checkDateOutOfRange(build,startDate))
						break;
					
					
				}
				catch(Exception e)
				{
					//System.out.println(e);
					//System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
			
	
		
		return buildPassStatus;
	}



	private boolean checkDateOutOfRange(Build build, String startDate) {
		
		try
		{
			//System.out.println(build.getNumber());
			//System.out.println(build.details().getTimestamp());
			Date date=new Date(build.details().getTimestamp());
			Format format = new SimpleDateFormat("yyyy-MM-dd");
			format.format(date);
			//System.out.println(date);
			SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");
			Date start=simple.parse(startDate);
			if(date.before(start))
				return true;
		}
		catch(Exception e)
		{
			//System.out.println("Exception:"+e);
			return false;
		}
		return false;
	}

	private boolean checkBuildExistInRange(Build build, String startDate, String endDate) {
	try
	{
		//System.out.println(build.getNumber());
		//System.out.println(build.details().getTimestamp());
		Date date=new Date(build.details().getTimestamp());
		Format format = new SimpleDateFormat("yyyy-MM-dd");
		format.format(date);
		//System.out.println(date);
		SimpleDateFormat simple=new SimpleDateFormat("yyyy-MM-dd");
		Date start=simple.parse(startDate);
		Date end=simple.parse(endDate);
		format.format(end);
		//System.out.println(start+" "+end);
		//System.out.println(date.before(end) && date.after(start));
		if(date.before(end) && date.after(start))
			return true;
	}
	catch(Exception e)
	{
		//System.out.println("Exception:"+e);
		return false;
	}
	return false;
	}

	public List<BuildInfoCollection> getAllBuildPassFailSkipStatus(String startDate, String endDate) {
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, buildStatusSize);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<BuildTestInfo> buildPassStatus=new ArrayList<>();
		List<BuildTestInfo> buildFailStatus=new ArrayList<>();
		List<BuildTestInfo> buildSkipStatus=new ArrayList<>();
		
		
		
			for (Build build : builds) {
				try
				{
					
						if(checkBuildExistInRange(build, startDate, endDate))
						{
					//System.out.println("build number:"+build.getNumber());
					String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
					String responseInString=BaseClass.getResponse(URLS);
					
					
					JsonObject object = BaseClass.convertJSONObject(responseInString);
					
					
						int failCount=Integer.parseInt(object.get("failCount").toString());
						int skipCount=Integer.parseInt(object.get("skipCount").toString());
						int totalCount=Integer.parseInt(object.get("total").toString());
						
						int passCount=totalCount-skipCount-failCount;
						
						double yPass=(double)passCount/totalCount * 100;
						double yFail=(double)failCount/totalCount * 100;
						double ySkip=(double)skipCount/totalCount * 100;
						
						buildPassStatus.add(new BuildTestInfo(("#"+x),yPass, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
					
						buildFailStatus.add(new BuildTestInfo(("#"+x),yFail, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
					
						buildSkipStatus.add(new BuildTestInfo(("#"+x),ySkip, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
						 x++;
						 if(x>=threshold)
							 break;
						}
						else
						if(checkDateOutOfRange(build, startDate))
							break;
						
					
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
		
		 
		////System.out.println("Build collection");
		List<BuildInfoCollection> output=new ArrayList<BuildInfoCollection>(); 
		output.add(new BuildInfoCollection("Passed", buildPassStatus));
		output.add(new BuildInfoCollection("Failed", buildFailStatus));
		output.add(new BuildInfoCollection("Skipped", buildSkipStatus));
 		return output;
	}



	public List<BuildInfoCollection> getAllBuildPassFailSkipStatus(String startDate,
			String endDate, MultivaluedMap<String, String> parameters) {
		List<Build> builds = null;
		try {
			builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
		//	builds=builds.subList(0, buildStatusSize);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		List<BuildTestInfo> buildPassStatus=new ArrayList<>();
		List<BuildTestInfo> buildFailStatus=new ArrayList<>();
		List<BuildTestInfo> buildSkipStatus=new ArrayList<>();
		
		
		
			for (Build build : builds) {
				try
				{
					if(checkBuildExistInRange(build, startDate, endDate))
						{
					////System.out.println("url:"+credentials.getUrl());
					//System.out.println("build number:"+build.getNumber());
					String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+build.getNumber()+"/testngreports/api/json?tree=failCount,skipCount,total";
					String responseInString=BaseClass.getResponse(URLS);
					
					
					JsonObject object = BaseClass.convertJSONObject(responseInString);
					boolean flag=false;
										
						flag=BaseClass.checkUIParamtersExistInReport(parameters,build.details().getParameters(),credentials.getJobname());
				
					
					if(flag)
					{
						int failCount=Integer.parseInt(object.get("failCount").toString());
						int skipCount=Integer.parseInt(object.get("skipCount").toString());
						int totalCount=Integer.parseInt(object.get("total").toString());
						
						int passCount=totalCount-skipCount-failCount;
						
						double yPass=(double)passCount/totalCount * 100;
						double yFail=(double)failCount/totalCount * 100;
						double ySkip=(double)skipCount/totalCount * 100;
						
						buildPassStatus.add(new BuildTestInfo(("#"+x),yPass, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
					
						buildFailStatus.add(new BuildTestInfo(("#"+x),yFail, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
					
						buildSkipStatus.add(new BuildTestInfo(("#"+x),ySkip, build.getNumber(), passCount,failCount ,skipCount ,totalCount));
						
						 x++;
						if(x>=threshold)
							break;
					}
					else
						if(checkDateOutOfRange(build, startDate))
							break;
				}
					
				}
				catch(Exception e)
				{
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			}
			
		
		 
		////System.out.println("Build collection");
		List<BuildInfoCollection> output=new ArrayList<BuildInfoCollection>(); 
		output.add(new BuildInfoCollection("Passed", buildPassStatus));
		output.add(new BuildInfoCollection("Failed", buildFailStatus));
		output.add(new BuildInfoCollection("Skipped", buildSkipStatus));
 		return output;
	}

	public LastBuildPassPercentage getLastBuildPassPercentage() {
		LastBuildPassPercentage percentage=null;
		try
		{
			
			String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+lastBuildRun+"/testngreports/api/json?tree=failCount,skipCount,total";
			String responseInString=BaseClass.getResponse(URLS);
			
			
			JsonObject object = BaseClass.convertJSONObject(responseInString);
			
				int failCount=Integer.parseInt(object.get("failCount").toString());
				int skipCount=Integer.parseInt(object.get("skipCount").toString());
				int totalCount=Integer.parseInt(object.get("total").toString());
				percentage=new LastBuildPassPercentage(failCount, skipCount, totalCount, lastBuildRun);
			
	
		}
		catch(Exception e)
		{
			////System.out.println("Didn't find report for build number:"+build.getNumber());
		}
		return percentage;
		
	}

	public List<TableLastBuildsTestFileInfo> getTableLastBuildInfo() {
	
		List<TableLastBuildsInfo> tableLastBuildInfo=new ArrayList<>();
		List<TableLastBuildsTestFileInfo> tableLastBuildsTestFileInfo=new ArrayList<>();
		List<TableLastBuildsTestMethod> tableLastBuildsTestMethod=new ArrayList<>();
				try
				{
					
						
					//System.out.println("build number:"+lastBuildRun);
					String URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+lastBuildRun+"/testngreports/api/json?tree=package[classs[name,skip,fail,totalCount,test-method[parameters,exception,name,status]]]";
					String responseInString=BaseClass.getResponse(URLS);
					
					
					JsonObject object = BaseClass.convertJSONObject(responseInString);
					JsonArray packageArray=BaseClass.convertJSONArray(object.get("package").toString());
					//System.out.println("package:"+packageArray.size());
					for (JsonValue packages : packageArray) {
						
						JsonObject classes=BaseClass.convertJSONObject(packages.toString());
						JsonArray classArray=BaseClass.convertJSONArray(classes.get("classs").toString());
						////System.out.println("class:"+classArray.size());
						for (JsonValue tests : classArray) {
							
							JsonObject test=BaseClass.convertJSONObject(tests.toString());
							String testCaseName=test.get("name").toString().replaceAll("^\"|\"$", "");
							JsonArray methodArray=BaseClass.convertJSONArray(test.get("test-method").toString());
							tableLastBuildsTestMethod=new ArrayList<>();
							for(JsonValue methods:methodArray)
							{
								JsonObject method=BaseClass.convertJSONObject(methods.toString());
								String methodName=method.get("name").toString().replaceAll("^\"|\"$", "");
							//	//System.out.println("Method:"+methodName);
								tableLastBuildsTestMethod.add(new TableLastBuildsTestMethod(new ArrayList<TableLastBuildsResult>(), methodName));
							}
							////System.out.println(testCaseName);
							int failCount=Integer.parseInt(test.get("fail").toString());
							int skipCount=Integer.parseInt(test.get("skip").toString());
							
							TableLastBuildsTestFileInfo testFileInfo=new TableLastBuildsTestFileInfo(new ArrayList<TableLastBuildsResult>(), testCaseName, tableLastBuildsTestMethod);
							tableLastBuildsTestFileInfo.add(testFileInfo);
							//tableLastBuildInfo.add(new TableLastBuildsInfo(null, testFileInfo));
							
							
							
						}
						
					}
						
					List<Build> builds = null;
					try {
						builds=jenkins.getJob(credentials.getJobname()).getAllBuilds();
						builds=builds.subList(0, 3);
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					
					for (int i=0;i<builds.size();i++) {
						//System.out.println("build number:"+builds.get(i).getNumber());
						 URLS=credentials.getUrl()+"/job/"+credentials.getJobname()+"/"+builds.get(i).getNumber()+"/testngreports/api/json?tree=package[classs[name,skip,fail,totalCount,test-method[parameters,exception,name,status]]]";
						 responseInString=BaseClass.getResponse(URLS);
						
						
						 object = BaseClass.convertJSONObject(responseInString);
						 packageArray=BaseClass.convertJSONArray(object.get("package").toString());
						//System.out.println("package:"+packageArray.size());
						int module=0;
						for (JsonValue packages : packageArray) {
							
							JsonObject classes=BaseClass.convertJSONObject(packages.toString());
							JsonArray classArray=BaseClass.convertJSONArray(classes.get("classs").toString());
						//	//System.out.println("class:"+classArray.size());
							
							for (JsonValue tests : classArray) {
								
								JsonObject test=BaseClass.convertJSONObject(tests.toString());
								String testCaseName=test.get("name").toString().replaceAll("^\"|\"$", "");
								JsonArray methodArray=BaseClass.convertJSONArray(test.get("test-method").toString());
								
								for(JsonValue methods:methodArray)
								{
									module++;
									JsonObject method=BaseClass.convertJSONObject(methods.toString());
									String status=method.get("status").toString().replaceAll("^\"|\"$", "");
									////System.out.println("status:"+status);
									TableLastBuildsTestMethod moduleStatus=new TableLastBuildsTestMethod();
									//System.out.println("Module:"+module);
									//System.out.println("ModuleName:"+tableLastBuildsTestFileInfo.get(module).getModuleName());
									//tableLastBuildsTestMethod.get(module).getResult().add(new TableLastBuildsResult("abc", status, "fd", "dff"));
									//moduleStatus.setResult(tableLastBuildsTestMethod.get(i).getResult());
									
								//	tableLastBuildsTestMethod.set(module,moduleStatus);
								}
								////System.out.println(testCaseName);
								int failCount=Integer.parseInt(test.get("fail").toString());
								int skipCount=Integer.parseInt(test.get("skip").toString());
								
								TableLastBuildsTestFileInfo testFileInfo=new TableLastBuildsTestFileInfo(null, testCaseName, tableLastBuildsTestMethod);
								tableLastBuildsTestFileInfo.add(testFileInfo);
								//tableLastBuildInfo.add(new TableLastBuildsInfo(null, testFileInfo));
								
								
								
							}
							
						}
					}
					
				}
				catch(Exception e)
				{
					e.printStackTrace();
					////System.out.println("Didn't find report for build number:"+build.getNumber());
				}
			
			return tableLastBuildsTestFileInfo;
			
		
		 
	}

	
}


