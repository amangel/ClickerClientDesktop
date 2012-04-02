package com.clicker.client.desktop;



public class CustomQuestion extends ActiveQuestion {

    private Question question;
    
    public CustomQuestion(String questionNumber, String flags, String parameters, String color) {
        super(questionNumber, flags, parameters, color);
        question = new Question(parameters);
        this.add(question);
    }
    

}
