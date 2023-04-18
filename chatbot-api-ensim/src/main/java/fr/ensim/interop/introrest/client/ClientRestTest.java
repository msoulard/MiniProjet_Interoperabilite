package fr.ensim.interop.introrest.client;

import fr.ensim.interop.introrest.data.Meteo;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;


import java.io.IOException;

import java.util.Arrays;


public class ClientRestTest {

	public static void main(String[] args) throws IOException {


		// Test de la méthode meteoByVille
		RestTemplate restTemplate = new RestTemplate();
		String url = "http://localhost:9090/meteo?ville=Nantes";
		String response = restTemplate.getForObject(url, String.class);
		System.out.println("Météo à Nantes : " + response);

		// Test de la méthode getWeather
		String ville = "Nantes";
		String urlforcast = "http://localhost:9090/forecast/" + ville + "?forecast=true";
		RestTemplate restTemplate2 = new RestTemplate();

		ResponseEntity<Meteo[]> responseEntity = restTemplate2.getForEntity(urlforcast, Meteo[].class);
		Meteo[] meteos = responseEntity.getBody();

		System.out.println(Arrays.toString(meteos));

	}
}





