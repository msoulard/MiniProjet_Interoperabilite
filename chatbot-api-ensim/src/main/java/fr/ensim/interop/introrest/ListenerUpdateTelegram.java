package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.JokeRestController;
import fr.ensim.interop.introrest.controller.MessageRestController;
import fr.ensim.interop.introrest.controller.WeatherRestController;
import fr.ensim.interop.introrest.data.Joke;
import fr.ensim.interop.introrest.data.Meteo;
import fr.ensim.interop.introrest.data.OpenWeather;
import fr.ensim.interop.introrest.model.telegram.ApiResponseUpdateTelegram;
import fr.ensim.interop.introrest.data.MessageApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListenerUpdateTelegram implements CommandLineRunner {

	private static final String COMMAND_METEO = "meteo";
	private static final String COMMAND_BLAGUE = "blague";
	private Integer offset = 933058596;
	private int offsetBis = offset.intValue();
	@Autowired
	private MessageRestController controllerMessage;
	@Autowired
	private JokeRestController controllerJoke;
	@Autowired
	private WeatherRestController controllerWeather;

	@Override
	public void run(String... args) throws Exception {
		Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");
		TimerTask task = new TimerTask() {
			public void run() {
				List<String> messagesATraiter = new ArrayList<>();
				while (true) {
					ResponseEntity<ApiResponseUpdateTelegram> reponseGet = controllerMessage.getUpdates(offsetBis);
					if(reponseGet.getBody().getResult().size() > 0) {
						offset = reponseGet.getBody().getResult().get(0).getUpdateId();
						offsetBis = offset.intValue();
						for (int i = 0; i < reponseGet.getBody().getResult().size(); i++) {
							messagesATraiter.add(reponseGet.getBody().getResult().get(i).getMessage().getText());
						}

						while (!messagesATraiter.isEmpty()) {
							String command = messagesATraiter.get(0);
							Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "message à traiter = " + command);
							switch (command) {
								case COMMAND_METEO:
									/*Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "meteo");
									controller.sendMessage(new MessageApi(0L, "resultat météo"));*/
									//demander la ville
									controllerMessage.sendMessage(new MessageApi(0L, "La ville ?"));
									offsetBis++;
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
									}
									ResponseEntity<ApiResponseUpdateTelegram> reponseVille = controllerMessage.getUpdates(offsetBis);
									String ville = reponseVille.getBody().getResult().get(0).getMessage().getText();
									Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, ville);
									//demande pour combien de temps l'utilisateur veut la mété
									controllerMessage.sendMessage(new MessageApi(0L, "Pour combien de jours ? (entier)"));
									offsetBis++;
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
									}
									ResponseEntity<ApiResponseUpdateTelegram> reponseJours = controllerMessage.getUpdates(offsetBis);
									int jours = Integer.parseInt(reponseJours.getBody().getResult().get(0).getMessage().getText());
									Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, String.valueOf(jours));
									//envoie de la météo en fonction du nombre de jours choisi
									if(jours > 1){
										ResponseEntity<List<Meteo>> reponseWeather = controllerWeather.getWeather(ville);
										for(int i = 1 ; i <= jours ; i++){
											controllerMessage.sendMessage(new MessageApi(0L, "jour "+i));
											controllerMessage.sendMessage(new MessageApi(0L, reponseWeather.getBody().get(i).getDetails()));
											//float temperature = Float.parseFloat(reponseWeather.getBody().get(i).getTemperature());
											//temperature -= 273.15;
											controllerMessage.sendMessage(new MessageApi(0L, "température : "+reponseWeather.getBody().get(i).getTemperature()));
										}
									}
									else{
										ResponseEntity<OpenWeather> reponseWeather = controllerWeather.meteoByVille(ville);
										controllerMessage.sendMessage(new MessageApi(0L, reponseWeather.getBody().getWeather().get(0).getDescription()));
										//float temperature = Float.parseFloat(reponseWeather.getBody().getMain().getTemp());
										//temperature -= 273.15;
										controllerMessage.sendMessage(new MessageApi(0L, "température : "+reponseWeather.getBody().getMain().getTemp()));
									}
									offsetBis++;
									messagesATraiter.remove(0);
									break;
								case COMMAND_BLAGUE:
									/*Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "blague");
									controller.sendMessage(new MessageApi(0L, "resultat blague"));*/
									//envoie de la blague
									ResponseEntity<Joke> reponseJoke = controllerJoke.getJoke();
									controllerMessage.sendMessage(new MessageApi(0L, reponseJoke.getBody().getQuestion()));
									controllerMessage.sendMessage(new MessageApi(0L, reponseJoke.getBody().getAnswer()));
									controllerMessage.sendMessage(new MessageApi(0L, "Note : "+reponseJoke.getBody().getNote()));
									//demander s'il veut donner une note
									controllerMessage.sendMessage(new MessageApi(0L, "Voulez-vous donner une note ? (oui/non)"));
									offsetBis++;
									try {
										Thread.sleep(5000);
									} catch (InterruptedException e) {
										throw new RuntimeException(e);
									}
									ResponseEntity<ApiResponseUpdateTelegram> reponseAvis = controllerMessage.getUpdates(offsetBis);
									String donnerAvis = reponseAvis.getBody().getResult().get(0).getMessage().getText();
									switch (donnerAvis){
										case "oui":
											controllerMessage.sendMessage(new MessageApi(0L, "Donnez votre note entre 0 et 10 (entier)"));
											offsetBis++;
											try {
												Thread.sleep(5000);
											} catch (InterruptedException e) {
												throw new RuntimeException(e);
											}
											ResponseEntity<ApiResponseUpdateTelegram> reponseNote = controllerMessage.getUpdates(offsetBis);
											int note = Integer.parseInt(reponseNote.getBody().getResult().get(0).getMessage().getText());
											note = (note + reponseJoke.getBody().getNote())/2;
											reponseJoke.getBody().setNote(note);
											controllerMessage.sendMessage(new MessageApi(0L, "Merci d'avoir donné une note. Voici la nouvelle note : "+reponseJoke.getBody().getNote()));
											break;
										case "non":
											controllerMessage.sendMessage(new MessageApi(0L, "Merci d'avoir lu notre blague"));
											break;
										default :
											controllerMessage.sendMessage(new MessageApi(0L, "Votre réponse est incorrecte"));
											break;
									}
									offsetBis++;
									messagesATraiter.remove(0);
									break;
								default:
									Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "default");
									controllerMessage.sendMessage(new MessageApi(0L, "pour avoir une réponse, demandez meteo ou blague"));
									offsetBis++;
									messagesATraiter.remove(0);
									break;
							}
							Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "offset = " + offsetBis);
						}
						messagesATraiter.clear();
					}
				}
			}
		};
		Timer timer = new Timer("Timer");

		long delay = 10L;
		timer.schedule(task, delay);
	}

}
