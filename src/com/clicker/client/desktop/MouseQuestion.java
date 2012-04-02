package com.clicker.client.desktop;



public class MouseQuestion extends ActiveQuestion {

    private MouseQuestionInteraction question;
    
    public MouseQuestion(String questionNumber, String flags, String parameters, String color) {
        super(questionNumber, flags, parameters, color);
        question = new MouseQuestionInteraction(parameters);
        this.add(question);
    }
}
