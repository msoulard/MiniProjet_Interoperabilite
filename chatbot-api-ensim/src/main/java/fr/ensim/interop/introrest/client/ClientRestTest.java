package fr.ensim.interop.introrest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ClientRestTest {
	
	public static void main(String[] args) throws IOException {

		//Vous pouvez faire des tests d'appels d'API ici

		String city = "paris"; // Remplacez "paris" par la ville de votre choix.
		String apiUrl = "http://localhost:8080/weather/" + city + "?forecast=true";

		URL url = new URL(apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Erreur HTTP: " + responseCode);
		} else {
			String responseBody = "";
			Scanner scanner = new Scanner(url.openStream());
			while (scanner.hasNext()) {
				responseBody += scanner.nextLine();
			}
			scanner.close();

			List<String> weatherData = new ObjectMapper().readValue(responseBody, new TypeReference<List<String>>(){});

			for (String data : weatherData) {
				System.out.println(data);
			}


		}
	}


	}


