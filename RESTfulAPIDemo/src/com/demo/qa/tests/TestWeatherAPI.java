
//Testing the Weather API  http://api.openweathermap.org
// Obtain the API Key 
// Run as Java Application



package com.demo.qa.tests;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;

public class TestWeatherAPI {
	private static final String WEATHER_API_KEY = "711c7aec07004946cf08e39716019357";
	private static final String APIURL = "http://api.openweathermap.org/data/2.5/weather";


	public static void main(String[] args) throws IOException, ParseException {
		getWeatherByZipCode("94040", "us");
		getWeatherByCity("London","uk");
		
	}
	
	public static void getWeatherByZipCode(String zip, String country) throws IOException, ParseException {
		String expectedcityname = "Mountain View";
	
		URL url;
		InputStream is = null;
		BufferedReader br = null;
		JSONParser parser = null;
		JSONObject obj = null;
		try {			
			StringBuilder str1 = new StringBuilder().append(APIURL).append("?zip=").append(zip).append(",").append(country).append("&appid=").append(WEATHER_API_KEY);
			String concatURL = str1.toString();
			System.out.println(concatURL);
			url = new URL(concatURL);		
			is = url.openStream() ;			
			br = new BufferedReader(new InputStreamReader(is));
			parser = new JSONParser() ;
			obj = (JSONObject) parser.parse(br);
			//getting the values of the keys from the JSON response object
			String actualcityname = (String) obj.get("name");
			System.out.println(actualcityname);
		
			Assert.assertEquals(actualcityname, expectedcityname);
			
			// Weather is an array object inside JSON response
			final JSONArray weatherArray = (JSONArray) obj.get("weather");
		
			if (weatherArray != null) {
				Map<String, Object> WMap = null;
				String description = null;
				String maindescription = null;
			
				for (int i = 0; i < weatherArray.size(); i++) {
					WMap = (Map<String, Object>) weatherArray.get(i);
					description = (String) WMap.get("description");
					System.out.println("detailed description: " + description);
					maindescription = (String) WMap.get("main");
					System.out.println("main description: " + maindescription);				
				}				
			}
			
			br.close();
			is.close();

	  } catch (MalformedURLException e) {
	      e.printStackTrace();
	  } catch (IOException e) {
	      e.printStackTrace();
	  } finally {
		  if (br!=null)
			br.close();			
		  if (is!=null)
			is.close();			
	  }
		
		
	}
	
	
	
	public static void getWeatherByCity(String city, String country) throws IOException, ParseException {
		String expectedcityname = "London";
		
		URL url;
		InputStream is = null;
		BufferedReader br = null;
		JSONParser parser = null;
		JSONObject obj = null;
		try {			
			StringBuilder str1 = new StringBuilder().append(APIURL).append("?q=").append(city).append(",").append(country).append("&appid=").append(WEATHER_API_KEY);
			String concatURL = str1.toString();
			url = new URL(concatURL);
			System.out.println(concatURL);
			is = url.openStream() ;			
			br = new BufferedReader(new InputStreamReader(is));
			parser = new JSONParser() ;
			obj = (JSONObject) parser.parse(br);
			//getting the values of the keys from the JSON response object
			String actualcityname = (String) obj.get("name");
			System.out.println(actualcityname);
		
			Assert.assertEquals(actualcityname, expectedcityname);
			
			// Weather is an array object inside JSON response
			final JSONArray weatherArray = (JSONArray) obj.get("weather");
			if (weatherArray == null){
			  System.out.println("API did not fetch the results");
			  Assert.fail();
			}
		
			else if (weatherArray != null) {
				Map<String, Object> WMap = null;
				String description = null;
				String maindescription = null;
			
				for (int i = 0; i < weatherArray.size(); i++) {
					WMap = (Map<String, Object>) weatherArray.get(i);
					description = (String) WMap.get("description");
					System.out.println("detailed description: " + description);
					maindescription = (String) WMap.get("main");
					System.out.println("main description: " + maindescription);				
				}				
			}
			
			br.close();
			is.close();

	  } catch (MalformedURLException e) {
	      e.printStackTrace();
	  } catch (IOException e) {
	      e.printStackTrace();
	  } finally {
		  if (br!=null)
			br.close();			
		  if (is!=null)
			is.close();			
	  }
		
		
	}

}
