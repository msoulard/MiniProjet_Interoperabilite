package fr.ensim.interop.introrest.data;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class Joke {
    private String titre;
    private int id;
    private String question;
    private String answer;

    private int note;

    public Joke() {}

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

    public String getAnswer() {
        return answer;
    }
    public int getid()
    {
        return id;
    }
    public String getTitre()
    {
        return titre;
    }
    public int getNote()
    {
        return note;
    }
    public void setNote(int note)
    {
        this.note=note;
    }

    public void ChangeNoteBlague(int _note) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        ClassPathResource classPathResource = new ClassPathResource("Joke.json");
        InputStream inputStream = classPathResource.getInputStream();
        JsonNode jsonNode = objectMapper.readTree(inputStream);
        int noteChange = (note+_note)/2;
        objectMapper.writeValue(new File(classPathResource.getFile().toURI()), noteChange);

    }
}


