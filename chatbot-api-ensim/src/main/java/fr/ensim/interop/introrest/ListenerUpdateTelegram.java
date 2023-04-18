package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.MessageRestController;
import fr.ensim.interop.introrest.data.Joke;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.data.MessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListenerUpdateTelegram implements CommandLineRunner {
	@Value("${open.weather.api.token}")
	private String token;
	private static final String COMMAND_METEO = "meteo";
	private static final String COMMAND_BLAGUE = "blague";
	private Integer offset = 933058519;
	private int offsetBis = offset.intValue();
	@Autowired
	private MessageRestController controller;

	@Override
	public void run(String... args) throws Exception {
		Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");
		TimerTask task = new TimerTask() {
			public void run() {
				List<String> messagesATraiter = new ArrayList<>();
				ResponseEntity<ApiResponseUpdateTelegram> reponseGet = controller.getUpdates(offsetBis);
				offset = reponseGet.getBody().getResult().get(0).getUpdateId();
				offsetBis = offset.intValue();
				for (int i = 0; i < reponseGet.getBody().getResult().size(); i++) {
					messagesATraiter.add(reponseGet.getBody().getResult().get(i).getMessage().getText());
				}

				while (!messagesATraiter.isEmpty()) {
					String command = messagesATraiter.get(0);
					Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "message à traiter = "+ command);
					switch (command) {
						case COMMAND_METEO:
							if (messagesATraiter.size() < 2) {
								// Si la commande "meteo" n'a pas de paramètre de ville, envoyez un message d'erreur
								controller.sendMessage(new MessageApi(0L, "Veuillez entrer une ville pour la météo, ex : meteo Paris"));
							} else {
								// Sinon, recuperez le paramètre de ville et utilisez-le dans l'appel à l'API météo
								String city = messagesATraiter.get(1);
								String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid="+token;
								RestTemplate restTemplate = new RestTemplate();
								ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);
								controller.sendMessage(new MessageApi(0L, response.getBody()));
							}
							offsetBis++;
							messagesATraiter.remove(0);
							break;
						case COMMAND_BLAGUE:
							Joke blague = Joke.getRandomJoke();
							String question = blague.getQuestion();
							controller.sendMessage(new MessageApi(0L, question));
							String reponse=blague.getAnswer();
							controller.sendMessage(new MessageApi(0L, reponse));
							offsetBis++;
							messagesATraiter.remove(0);
							break;

						default:
							Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "default");
							controller.sendMessage(new MessageApi(0L, "bonjour"));
							offsetBis++;
							messagesATraiter.remove(0);
							break;
					}
					Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "offset = "+offsetBis);
				}
				messagesATraiter.clear();
			}
		};
		Timer timer = new Timer("Timer");

		long delay = 10L;
		timer.schedule(task, delay);
	}

}
