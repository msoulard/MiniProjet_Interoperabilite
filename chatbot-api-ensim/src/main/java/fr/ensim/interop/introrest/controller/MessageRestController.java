package fr.ensim.interop.introrest.controller;

import fr.ensim.interop.introrest.data.MessageApi;
import fr.ensim.interop.introrest.model.telegram.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

@RestController
public class MessageRestController {
	
	@Value("${telegram.api.url}")
	private String telegramApiUrl;
	@Value("${telegram.chat.id}")
	private String chatId;
	
	//Opérations sur la ressource Message
	@GetMapping(value = "/getUpdates")
	public ResponseEntity<ApiResponseUpdateTelegram> getUpdates(Integer offset) {

		RestTemplate restTemplate = new RestTemplate();
		ApiResponseUpdateTelegram response;
		if(offset != null) {
			 response = restTemplate.getForObject(telegramApiUrl + "/getUpdates?offset=" + offset,
					ApiResponseUpdateTelegram.class);
		}
		else{
			response = restTemplate.getForObject(telegramApiUrl + "/getUpdates",
					ApiResponseUpdateTelegram.class);
		}

		return ResponseEntity.ok().body(response);
	}

	@PostMapping("/message")
	public ResponseEntity<ApiResponseTelegram> sendMessage(@RequestBody MessageApi message) {
		// le text du message obligatoire, non vide, non blanc)
		if(!StringUtils.hasText(message.getText())) {
			return ResponseEntity.badRequest().build();
		}
		message.setChat_id(Long.parseLong(chatId));
		RestTemplate restTemplate = new RestTemplate();
		ApiResponseTelegram response = restTemplate.postForObject(telegramApiUrl+"/sendMessage",message,
				ApiResponseTelegram.class);;

		return ResponseEntity.ok().body(response);
	}

}
