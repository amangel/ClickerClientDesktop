package com.clicker.client.desktop;

import javax.swing.JFrame;
import javax.swing.JPanel;


// TODO: Auto-generated Javadoc
/**
 * The Class ActiveQuestion.
 */
public abstract class ActiveQuestion extends JPanel {

    /** The question number. */
    protected String questionNumber;
    
    /** The flags. */
    protected String flags;
    
    /** The parameters. */
    protected String parameters;
    
    /** The color. */
    protected String color;
    
    /** The hub. */
    protected CommunicationHub hub;
    
    /** The frame. */
    protected JFrame frame;
    
    /**
     * Instantiates a new active question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public ActiveQuestion(String questionNumber,
            String flags,
            String parameters,
            String color) {
        this.questionNumber = questionNumber;
        this.flags = flags;
        this.parameters = parameters;
        this.color = color;
        hub = CommunicationHub.getInstance();
        frame = new JFrame();
        frame.setTitle("ActiveQuestion");
        frame.setSize(300,400);
        frame.add(this);
    }
    
    /**
     * Close.
     */
    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    /**
     * Send response.
     *
     * @param values the values
     */
    public void sendResponse(String values) {
        hub.sendQuestionResponse(values, questionNumber);
    }

    /**
     * Sets the visible.
     */
    public void setVisible() {
        frame.setVisible(true);
    }
    
    public void forceRedraw() {
        frame.repaint();
    }
    
}
