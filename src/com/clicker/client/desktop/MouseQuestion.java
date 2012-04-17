package com.clicker.client.desktop;



// TODO: Auto-generated Javadoc
/**
 * The Class MouseQuestion.
 */
public class MouseQuestion extends ActiveQuestion {

    /** The question. */
    private MouseQuestionInteraction question;
    
    /**
     * Instantiates a new mouse question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public MouseQuestion(String questionNumber, String flags, String parameters, String color) {
        super(questionNumber, flags, parameters, color);
        question = new MouseQuestionInteraction(parameters);
        this.add(question);
    }
}
