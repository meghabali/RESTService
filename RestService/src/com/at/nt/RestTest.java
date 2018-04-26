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
 * 		[ {"numbers" : [2, 3.0, 5]}, {"numbers" : [6.3, 3, 7]} ]
 * 
 * Output Requirements:
 * 1. For each document display all the key names.
 * 2. Find the sum of all numbers of the key "numbers".
 * 3. Total all of the numbers through out document.
 */
public class RestTest {

	private final Gson GSON = new Gson();
	private final String KEY_NUMBERS = "numbers";

	public static void main(String[] args) {
		if(args[0] != null) {
			new RestTest(args[0]);	
		} else {
			System.err.println("Please provide the Rest Service's URL...");
		}
	}
	
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
			else
			{
				System.out.println("Given URL is not working..");
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
		System.out.println("Requirement 1: all the key names.");
		for (Map<Object, Object> map : docList) {
			System.out.println("\tKey's names: " + map.keySet());
		}
		System.out.println("Requirement 2: Sum of all numbers of the key \"numbers\".");
		double total = 0;
		for (Map<Object, Object> map : docList) {
			double sum = 0;
			List<Number> numbers = null;
			for (Object key : map.keySet()) {
				if (KEY_NUMBERS.equals(key)) {
					numbers = (List<Number>) map.get(key);
					for (Number number : numbers)
						if(number != null)
							sum += number.doubleValue();
				}
			}
			total += sum;
			System.out.println("\tAll numbers: " + numbers + ", Sum of numbers: " + sum);
		}
		System.out.println("Requirement 3: Total sum of the numbers through out the execution.");
		System.out.println("\tTotal sum: " + total);
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
