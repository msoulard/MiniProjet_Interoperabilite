package fr.ensim.interop.introrest.data;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;

import java.io.FileReader;
import java.util.Random;

public class Joke {
    private String titre;
    private int id;
    private String question;
    private String answer;

    private int note;

    public Joke(String question, String answer) {}

    public Joke(String question, String answer,int id, String titre,int note) {
        this.titre=titre;
        this.id=id;
        this.question = question;
        this.answer = answer;
        this.note=note;

    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
    public int getid()
    {
        return id;
    }
    public void setid(int id)
    {
        this.id=id;
    }
    public String getTitre()
    {
        return titre;
    }
    public void setTitre(String titre){
        this.titre=titre;
    }
    public int getNote()
    {
        return note;
    }
    public void setNote(int note)
    {
        this.note=note;
    }

    public static Joke getRandomJoke() {
        try {
            // Ouverture du fichier JSON contenant les blagues
            FileReader reader = new FileReader("path/to/your/json/file.json");
            JSONParser jsonParser = new JSONParser(reader);
            Object obj = jsonParser.parse();
            JSONArray jokesArray = (JSONArray) obj;

            // Sélection aléatoire d'une blague
            Random rand = new Random();
            int randomIndex = rand.nextInt(jokesArray.length());
            JSONObject jokeObj = (JSONObject) jokesArray.get(randomIndex);

            // Création de l'objet Joke
            String question = (String) jokeObj.get("question");
            String answer = (String) jokeObj.get("answer");
            return new Joke(question, answer);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}


