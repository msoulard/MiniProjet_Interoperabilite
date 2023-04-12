package fr.ensim.interop.introrest.controller;

import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class MessageRestController {
	
	@Value("${telegram.api.url}")
	private String telegramApiUrl;
	
	//Opérations sur la ressource Message
	@GetMapping(value = "/getUpdates")
	public ResponseEntity<ApiResponseUpdateTelegram> getUpdates() {

		RestTemplate restTemplate = new RestTemplate();
		ApiResponseUpdateTelegram response = restTemplate.getForObject(telegramApiUrl+"/getUpdates",
				ApiResponseUpdateTelegram.class);

		return ResponseEntity.ok().body(response);
	}

}
