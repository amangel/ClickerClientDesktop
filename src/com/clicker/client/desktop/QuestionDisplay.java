package com.clicker.client.desktop;


public class QuestionDisplay {

    private ActiveQuestion question;
    
    public QuestionDisplay() {}
    
    public void removeOption(int widgetNumber) {
        question.remove(widgetNumber);
    }

    public void closeQuestion() {
        question.close();
    }

    public void openMouseQuestion(String questionNumber,
            String flags,
            String parameters,
            String color) {
        question = new MouseQuestion(questionNumber, flags, parameters, color);
    }

    public void openCustomQuestion(String questionNumber,
            String flags,
            String parameters,
            String color) {
        question = new CustomQuestion(questionNumber, flags, parameters, color);
    }
    
}
