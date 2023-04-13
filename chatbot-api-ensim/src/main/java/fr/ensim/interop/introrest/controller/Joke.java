package fr.ensim.interop.introrest.controller;

public class Joke {

    private String question;
    private String answer;

    public Joke() {}

    public Joke(String question, String answer) {
        this.question = question;
        this.answer = answer;
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
    }


