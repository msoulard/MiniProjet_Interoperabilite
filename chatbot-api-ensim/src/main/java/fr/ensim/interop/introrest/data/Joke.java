package fr.ensim.interop.introrest.data;

import java.io.File;

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
}


