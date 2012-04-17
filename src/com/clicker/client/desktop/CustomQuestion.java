package com.clicker.client.desktop;


// TODO: Auto-generated Javadoc
/**
 * The Class CustomQuestion.
 */
public class CustomQuestion extends ActiveQuestion {
    
    /** The question. */
    private Question question;
    
    /**
     * Instantiates a new custom question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public CustomQuestion(String questionNumber, String flags, String parameters, String color) {
        super(questionNumber, flags, parameters, color);
        question = new Question(parameters);
        question.setParent(this);
        this.add(question);
        setVisible();
    }
}
