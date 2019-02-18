package org.mohbansa.abacus.common;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonValue;
import javax.ws.rs.core.MultivaluedMap;

import org.mohbansa.abacus.model.Credentials;




public class BaseClass {
	
	public static String getResponse(String myURL)
	{
		StringBuilder sb = new StringBuilder();
		URLConnection urlConn = null;
		InputStreamReader in = null;
		try {
			URL url = new URL(myURL);
			urlConn = url.openConnection();
			if (urlConn != null)
				urlConn.setReadTimeout(60 * 1000);
			if (urlConn != null && urlConn.getInputStream() != null) {
				in = new InputStreamReader(urlConn.getInputStream(),Charset.defaultCharset());
				BufferedReader bufferedReader = new BufferedReader(in);
				if (bufferedReader != null) {
					int cp;
					while ((cp = bufferedReader.read()) != -1) {
						sb.append((char) cp);
					}
					bufferedReader.close();
				}
			}
		in.close();
		} catch (Exception e) {
			throw new RuntimeException("Exception while calling URL:"+ myURL, e);
		}
	//	//System.out.println("Base Class Response:"+sb.toString());
		return sb.toString();
	}

	public static JsonObject convertJSONObject(String response)
	{
		JsonReader jsonReader = Json.createReader(new StringReader(response));
		JsonObject object = jsonReader.readObject();
		jsonReader.close();
		return object;
	}
	public static JsonArray convertJSONArray(String response)
	{
		JsonReader jsonReader = Json.createReader(new StringReader(response));
		JsonArray object = jsonReader.readArray();
		jsonReader.close();
		return object;
	}
	
	//convert object to map
	
	public static Map<String,String> convertJSONObjectToMap(JsonObject objects)
	{
		Map<String,String> output=new HashMap<>();
		for (Entry<String, JsonValue> object : objects.entrySet()) {
			//System.out.println(object.getKey()+" "+object.getValue().toString());
			output.put(object.getKey(), object.getValue().toString());
		}
		return output;
	}

	public static boolean checkUIParamtersExistInReport(MultivaluedMap<String, String> expected,
			Map<String, String> actual,String jobName) {
		
		for (Entry<String, String> ui:actual.entrySet()) {
			
			//System.out.println(ui.getKey()+" "+ui.getValue()+" "+expected.get(ui.getKey()));
			//System.out.println(expected.get(ui.getKey()).contains(ui.getValue()));
			if(!expected.get(ui.getKey()).contains(ui.getValue()))
				return false;
		}
		
		return true;
	}
	
}
