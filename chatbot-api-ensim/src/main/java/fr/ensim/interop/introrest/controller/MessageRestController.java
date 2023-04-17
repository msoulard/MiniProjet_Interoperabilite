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
	//private Integer offset;
	//private List<String> messagesATraiter;
	
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
		/*offset = response.getResult().get(0).getUpdateId();
		for(int i = 0 ; i < response.getResult().size()-1 ; i++) {
			messagesATraiter.add(response.getResult().get(i).getMessage().getText());
		}*/

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
		//ApiResponseTelegram response;
		ApiResponseTelegram response = restTemplate.postForObject(telegramApiUrl+"/sendMessage",message,
				ApiResponseTelegram.class);;
		/*switch (messagesATraiter.get(0)){
			case "meteo" :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					message.setText("resultat météo");
					response = restTemplate.postForObject(telegramApiUrl+"/sendMessage",message,
							ApiResponseTelegram.class);
					offset++;
				}
				else{
					offset++;
				}
				break;
			case "blaque" :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					message.setText("resultat blague");
					response = restTemplate.postForObject(telegramApiUrl+"/sendMessage",message,
							ApiResponseTelegram.class);
					offset++;
				}
				else{
					offset++;
				}
				break;
			default :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					response = restTemplate.postForObject(telegramApiUrl+"/sendMessage",message,
							ApiResponseTelegram.class);
					offset++;
				}
				else{
					offset++;
				}
				break;
		}*/

		return ResponseEntity.ok().body(response);
	}

}
