package com.clicker.client.desktop;


// TODO: Auto-generated Javadoc
/**
 * The Class QuestionDisplay.
 */
public class QuestionDisplay {

    /** The question. */
    private ActiveQuestion question;
    
    /**
     * Instantiates a new question display.
     */
    public QuestionDisplay() {}
    
    /**
     * Removes the option.
     *
     * @param widgetNumber the widget number
     */
    public void removeOption(int widgetNumber) {
        question.remove(widgetNumber);
    }

    /**
     * Close question.
     */
    public void closeQuestion() {
        question.close();
    }

    /**
     * Open mouse question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public void openMouseQuestion(String questionNumber,
            String flags,
            String parameters,
            String color) {
        question = new MouseQuestion(questionNumber, flags, parameters, color);
    }

    /**
     * Open custom question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public void openCustomQuestion(String questionNumber,
            String flags,
            String parameters,
            String color) {
        question = new CustomQuestion(questionNumber, flags, parameters, color);
    }
    
}
