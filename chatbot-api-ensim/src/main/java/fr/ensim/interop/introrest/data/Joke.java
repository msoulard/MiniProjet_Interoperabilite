package fr.ensim.interop.introrest.data;

import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.core.io.ClassPathResource;

import java.io.FileReader;
import java.io.InputStream;
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
    

}


