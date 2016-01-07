package com.demo.qa.tests;

//import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import java.util.Map;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.Assert;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

public class TestFuelStationsApi {
		private static final String API_KEY = "uLNdwphVGNwEsVOalIfAYHUVddNi4J0OchvKNika";
		private static final String NEAREST_APIURL = "https://api.data.gov/nrel/alt-fuel-stations/v1/nearest.json";
		private static Long id = null;
		private static final String ACTUAL_STATION_NAME = "HYATT AUSTIN";
		private static final String ACTUAL_STATTION_ADDR = "208 Barton Springs, Austin, Texas, USA, 78704";
		
		public static void main(String[] args) throws IOException, ParseException {
			URL url;
			InputStream is = null;
			BufferedReader br = null, br1 = null;
			JSONParser parser = null;
			JSONObject obj = null;
			try {			
				url = new URL(getNearestApiUrl());			
				is = url.openStream() ;			
				br = new BufferedReader(new InputStreamReader(is));
				parser = new JSONParser() ;
				
				obj = (JSONObject) parser.parse(br);
				final JSONArray fuelStationsArry = (JSONArray) obj.get("fuel_stations");
				
				if (fuelStationsArry != null) {
					Map<String, Object> fsMap = null;
					String stationName = null;
					
					for (int i = 0; i < fuelStationsArry.size(); i++) {
						fsMap = (Map<String, Object>) fuelStationsArry.get(i);
						stationName = (String) fsMap.get("station_name");
						if (stationName == null || !(ACTUAL_STATION_NAME.equals(stationName.trim()))) {
							continue;
						}
						System.out.println("### Found HYATT station###");
						Assert.assertEquals(stationName.trim(), ACTUAL_STATION_NAME);
						id = (Long) fsMap.get("id");
						break;							
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
			
		try {
			//make a second API call
			url = new URL(getIdApiUrl(id));
			final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			br1 = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			obj = (JSONObject) parser.parse(br1);
			final JSONObject altStation = (JSONObject) obj.get("alt_fuel_station");
			
			//setting country = USA, since test is being performed for the city Austin		
			StringBuilder stationAddress = new StringBuilder().append((String)altStation.get("street_address")).append(", ").append((String)altStation.get("city")).append(", ")
					.append((String)altStation.get("state")).append(", USA, ").append(altStation.get("zip"));
			System.out.println("Station address is "+ stationAddress.toString());
			
			Assert.assertEquals(stationAddress.toString(), ACTUAL_STATTION_ADDR);
			
			br1.close();
			conn.disconnect();
		} catch (MalformedURLException e) {
		      e.printStackTrace();
		} catch (IOException e) {
		      e.printStackTrace();
		} finally {
			  
			  if (br1!=null)
				br1.close();				
		}		
			
		}



		private static String getNearestApiUrl() {
			StringBuilder str = new StringBuilder(NEAREST_APIURL).append("?api_key=").append(API_KEY).append("&location=AUSTIN+TX").append("&ev_network=ChargePoint+Network");
			System.out.println(str.toString());
			return str.toString();
		}
		
		private static String getIdApiUrl(Long id) {
			StringBuilder str = new StringBuilder(NEAREST_APIURL.replace("nearest", String.valueOf(id))).append("?api_key=").append(API_KEY);
			System.out.println(str.toString());
			return str.toString();
		}
		
		
	 private static <K,V> void iterateMap(Map<K, V> myMap) throws IllegalStateException {
		if (myMap != null) {
			
			for (Map.Entry<K, V> entry : myMap.entrySet()) {
				System.out.println("Key = " + entry.getKey());
				
				if (entry.getValue() instanceof String || entry.getValue() instanceof Long || entry.getValue() instanceof Integer) {
					System.out.println("Value = " + entry.getValue());
				} else {
					if (entry.getValue() instanceof Map) {
						iterateMap((Map<?, ?>)entry.getValue());
					}				
				}			
			}		
		}
		
	 }

}
