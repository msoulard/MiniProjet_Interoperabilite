package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.MessageRestController;
import fr.ensim.interop.introrest.model.telegram.ApiResponseTelegram;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.model.telegram.MessageApi;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListenerUpdateTelegram implements CommandLineRunner {
	
	@Override
	public void run(String... args) throws Exception {
		Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");
		
		// Operation de pooling pour capter les evenements Telegram
		MessageRestController controller = new MessageRestController();
		ResponseEntity<ApiResponseUpdateTelegram> reponseGet = null;
		ResponseEntity<ApiResponseTelegram> reponsePost = null;
		MessageApi messagePost;
		Integer offset = null;
		List<String> messagesATraiter = new ArrayList<String>();

		reponseGet = controller.getUpdates(offset);
		offset = reponseGet.getBody().getResult().get(0).getUpdateId();
		for(int i = 0 ; i < reponseGet.getBody().getResult().size()-1 ; i++) {
			messagesATraiter.add(reponseGet.getBody().getResult().get(i).getMessage().getText());
		}

		switch (messagesATraiter.get(0)){
			case "meteo" :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					messagePost = new MessageApi(0L,"resultat météo");
					reponsePost = controller.sendMessage(messagePost);
					offset++;
				}
				else{
					offset++;
				}
				break;
			case "blaque" :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					messagePost = new MessageApi(0L,"resultat blague");
					reponsePost = controller.sendMessage(messagePost);
					offset++;
				}
				else{
					offset++;
				}
				break;
			default :
				if(messagesATraiter.size() == 0){
					messagesATraiter.remove(0);
					messagePost = new MessageApi(0L, "bonjour");
					reponsePost = controller.sendMessage(messagePost);
					offset++;
				}
				else{
					offset++;
				}
				break;
		}

	}
}
