package fr.ensim.interop.introrest.controller;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ensim.interop.introrest.model.telegram.City;
import fr.ensim.interop.introrest.model.telegram.Meteo;

import fr.ensim.interop.introrest.model.telegram.OpenWeather;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController


public class weatherRestController {

    @Value("${telegram.api.url}")
    private String telegramApiUrl;
    @Value("${open.weather.api.url}")
    private  String weatherApiUrl;
    @Value("${open.weather.api.token}")
    private String token;
    @Value("${open.weather.api.url.forecast}")
    private String weatherApiUrlForecast;

    //Opérations sur la ressource Weather




    @GetMapping("/meteo")
    public ResponseEntity<OpenWeather> meteoByVille(
            @RequestParam("ville") String nomVille) {

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<City[]> responseEntity = restTemplate.getForEntity("http://api.openweathermap.org/geo/1.0/direct?q={ville}&limit=3"
                        + "&appid=" + token,
                City[].class, nomVille);
        City[] cities = responseEntity.getBody();
        City city = cities[0];

        OpenWeather openWeather = restTemplate.getForObject("http://api.openweathermap.org/data/2.5/weather?lat={lat}"
                        + "&lon={longitude}&appid=" + token,
                OpenWeather.class, city.getLat(), city.getLon());

        Meteo meteo = new Meteo();
        meteo.setMeteo(openWeather.getWeather().get(0).getMain());
        meteo.setDetails(openWeather.getWeather().get(0).getDescription());
        meteo.setTemperature(openWeather.getMain().getTemp());

        return ResponseEntity.ok().body(openWeather);
    }
    ///forecast

    @GetMapping("/forecast/{ville}")
    public ResponseEntity<List<Meteo>> getWeather(@PathVariable("ville") String ville,
                                                  @RequestParam(required = false) boolean forecast) {
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<City[]> responseEntity = restTemplate.getForEntity(
                "http://api.openweathermap.org/geo/1.0/direct?q={ville}&limit=3&appid=" + token,
                City[].class, ville);
        City[] cities = responseEntity.getBody();
        City city = cities[0];

        String url = "https://api.openweathermap.org/data/2.5/forecast?q=" + city.getName() + "&appid=" + token;

        ResponseEntity<String> response = restTemplate.exchange(url,
                HttpMethod.GET, null, String.class);

        String body = response.getBody();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode root = null;
        try {
            root = objectMapper.readTree(body);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        List<Meteo> meteos = new ArrayList<>();

        if (root != null) {
            JsonNode list = root.get("list");
            for (JsonNode node : list) {
                LocalDateTime date = LocalDateTime.parse(node.get("dt_txt").asText(),
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                Meteo meteo = new Meteo();
                meteo.setDate(LocalDateTime.parse(String.valueOf(date)));
                meteo.setMeteo(node.get("weather").get(0).get("main").asText());
                meteo.setDetails(node.get("weather").get(0).get("description").asText());
                meteo.setTemperature(String.valueOf(node.get("main").get("temp").asDouble()));
                meteos.add(meteo);
            }
        }

        Collections.sort(meteos, (m1, m2) -> m1.getDate().compareTo(m2.getDate()));

        return ResponseEntity.ok().body(meteos);
    }




}








