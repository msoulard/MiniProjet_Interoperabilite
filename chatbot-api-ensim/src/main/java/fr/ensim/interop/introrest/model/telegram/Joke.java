package fr.ensim.interop.introrest.model.telegram;

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


