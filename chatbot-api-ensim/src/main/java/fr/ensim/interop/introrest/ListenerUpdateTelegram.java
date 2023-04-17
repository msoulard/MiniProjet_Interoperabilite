package fr.ensim.interop.introrest;

import fr.ensim.interop.introrest.controller.MessageRestController;
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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class ListenerUpdateTelegram implements CommandLineRunner {

	private static final String COMMAND_METEO = "meteo";
	private static final String COMMAND_BLAGUE = "blague";
	@Autowired
	private MessageRestController controller;
	private ScheduledExecutorService executor;

	@Override
	public void run(String... args) throws Exception {
		Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "Démarage du listener d'updates Telegram...");
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(this::pollUpdates, 0, 5, TimeUnit.SECONDS);
	}
	private Integer offset = 933058503;
	private int offsetBis = offset.intValue();
	private void pollUpdates() {

		List<String> messagesATraiter = new ArrayList<>();
		ResponseEntity<ApiResponseUpdateTelegram> reponseGet = controller.getUpdates(offsetBis);
		//if (reponseGet.getBody().getResult().size() > 0) {
		offset = reponseGet.getBody().getResult().get(0).getUpdateId();
		offsetBis = offset.intValue();

		/*} else {
			offset = null;
		}*/
		for (int i = 0; i < reponseGet.getBody().getResult().size(); i++) {
			messagesATraiter.add(reponseGet.getBody().getResult().get(i).getMessage().getText());
		}

		while (!messagesATraiter.isEmpty()) {
			String command = messagesATraiter.get(0);
			Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "message à traiter = "+ command);
			switch (command) {
				case COMMAND_METEO:
					Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "meteo");
					controller.sendMessage(new MessageApi(0L, "resultat météo"));
					offsetBis++;
					messagesATraiter.remove(0);
					break;
				case COMMAND_BLAGUE:
					Logger.getLogger("ListenerUpdateTelegram").log(Level.INFO, "blague");
					controller.sendMessage(new MessageApi(0L, "resultat blague"));
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
		//offsetBis--;
	}
}
