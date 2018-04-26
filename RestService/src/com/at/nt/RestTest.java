package com.at.nt;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

/**
 * 
 * @author Megha Bali
 * 
 * Assuming the restService will return the JSON in the form of:
 * 		[ { "key" : "value", "key" : "value" }, { "key" : "value" } ]
 * 
 * ex: 	[ { 3 : "doc1", 4.3 : "doc2" }, { 5 : "doc3" } ]
 * 
 * Output Requirements:
 * 1. For each document display all of the keys of the JSON.
 * 2. Keys are number(int/float etc), Add all numbers.
 * 3. Total of the numbers that were summed
 */
public class RestTest {

	private static final Gson GSON = new Gson();

	public RestTest(String restUrl) {
		consumeRest(restUrl);
	}

	private void consumeRest(String restUrl) {
		try {
			HttpURLConnection connection = getHttpConnection(restUrl);
			if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
				String jsonStr = getOutputString(connection.getInputStream());
				processOutput(jsonStr);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param jsonStr
	 * Print the output on console as per the requirement.
	 */
	private void processOutput(String jsonStr) {
		List<Map<Object, Object>> docList = GSON.fromJson(jsonStr, List.class);
		System.out.println("Requirement 1: For each document display all of the keys of the JSON.");
		for(Map<Object, Object> map : docList) {
			System.out.println("\tDocument's keys: "+map.keySet());
		}
		System.out.println("Requirement 2: Keys are number(int/float etc), Add all numbers.");
		double total = 0;
		for(Map<Object, Object> map : docList) {
			double sum = 0;
			for(Object number : map.keySet()) {
				sum += Double.parseDouble(number+"");
			}
			total += sum;
			System.out.println("\tDocument keys sum: "+ sum);
		}
		System.out.println("Requirement 3: Total of the numbers that were summed.");
		System.out.println("\tTotal sum: "+ total);
	}

	/**
	 * 
	 * @param restUrl
	 * @return connection after setting the basic configurations.
	 * @throws IOException
	 */
	private HttpURLConnection getHttpConnection(String restUrl) throws IOException {
		URL url = new URL(restUrl);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		return connection;
	}

	/**
	 * 
	 * @param inputStream
	 * @return the String form of inputStream.
	 * @throws IOException
	 */
	private String getOutputString(InputStream inputStream) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
		StringBuffer sb = new StringBuffer();
		String line;
		while ((line = reader.readLine()) != null) {
			sb.append(line);
		}
		return sb.toString();
	}

}
