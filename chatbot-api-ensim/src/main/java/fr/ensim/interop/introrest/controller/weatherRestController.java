package fr.ensim.interop.introrest.controller;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class weatherRestController {

    @Value("${telegram.api.url}")
    private String telegramApiUrl;

    //Opérations sur la ressource Weather

    private static final String API_URL_CURRENT = "https://api.openweathermap.org/data/2.5/weather?q=%s&appid=%s";
    private static final String API_URL_FORECAST = "https://api.openweathermap.org/data/2.5/forecast?q=%s&appid=%s";

    private final RestTemplate restTemplate = new RestTemplate();

    @GetMapping("/weather/{city}")
    public String getWeather(@PathVariable String city, @RequestParam(required = false) boolean forecast) {
        String apiKey = "cc524ed951c7ba09d9b8046d9a721628";

        String url;
        if (forecast) {
            url = String.format(API_URL_FORECAST, city, apiKey);
        } else {
            url = String.format(API_URL_CURRENT, city, apiKey);
        }

        String response = restTemplate.getForObject(url, String.class);

        return response;
    }


}
