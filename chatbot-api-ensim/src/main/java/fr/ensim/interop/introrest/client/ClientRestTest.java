package fr.ensim.interop.introrest.client;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ensim.interop.introrest.controller.Joke;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ClientRestTest{

	public static void main(String[] args) throws IOException {

		String city = "paris";
		String apiUrl = "http://localhost:9090/weather/" + city + "?forecast=true";

		URL url = new URL(apiUrl);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		int responseCode = conn.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Erreur HTTP: " + responseCode);
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder responseBody = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				responseBody.append(inputLine);
			}
			in.close();

			// Conversion de la réponse JSON en un objet Map<String, Object>
			ObjectMapper objectMapper = new ObjectMapper();
			Map<String, Object> weatherData = objectMapper.readValue(responseBody.toString(), new TypeReference<Map<String, Object>>() {});

			// Récupération des informations de la ville
			Map<String, Object> cityData = (Map<String, Object>) weatherData.get("city");
			String cityName = (String) cityData.get("name");
			Double cityLat = (Double) ((Map<String, Object>) cityData.get("coord")).get("lat");
			Double cityLon = (Double) ((Map<String, Object>) cityData.get("coord")).get("lon");
			System.out.println("Informations sur la ville de " + cityName + " (latitude : " + cityLat + ", longitude : " + cityLon + ")");

			// Récupération des informations météo
			List<Map<String, Object>> weatherList = (List<Map<String, Object>>) weatherData.get("list");
			for (Map<String, Object> weatherItem : weatherList) {
				String dateStr = (String) weatherItem.get("dt_txt");
				LocalDateTime date = LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				Map<String, Object> mainData = (Map<String, Object>) weatherItem.get("main");
				Double temp = (Double) mainData.get("temp");
				Double feelsLike = (Double) mainData.get("feels_like");
				Double tempMin = (Double) mainData.get("temp_min");
				Double tempMax = (Double) mainData.get("temp_max");
				Integer pressure = (Integer) mainData.get("pressure");
				Integer humidity = (Integer) mainData.get("humidity");
				System.out.println("Météo le " + date + " : température = " + temp + "°C, ressentie = " + feelsLike + "°C, min = " + tempMin + "°C, max = " + tempMax + "°C, pression = " + pressure + " hPa, humidité = " + humidity + "%");
			}
		}

		String apiUrlJoke = "http://localhost:9090/joke";
		// Récupération d'une blague aléatoire depuis l'API Chuck Norris Jokes
		url = new URL(apiUrlJoke);
		conn = (HttpURLConnection) url.openConnection();
		conn.setRequestMethod("GET");
		conn.connect();

		responseCode = conn.getResponseCode();

		if (responseCode != 200) {
			throw new RuntimeException("Erreur HTTP: " + responseCode);
		} else {
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			StringBuilder responseBody = new StringBuilder();
			String inputLine;
			while ((inputLine = in.readLine()) != null) {
				responseBody.append(inputLine);
			}
			in.close();

			// Conversion de la réponse JSON en un objet Joke
			ObjectMapper objectMapper = new ObjectMapper();
			Joke joke = objectMapper.readValue(responseBody.toString(), Joke.class);

			System.out.println("Blague : " + joke.getJoke());
		}
	}
}