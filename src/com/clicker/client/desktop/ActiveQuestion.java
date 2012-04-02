package com.clicker.client.desktop;

import javax.swing.JComponent;
import javax.swing.JFrame;


public abstract class ActiveQuestion extends JComponent {

    protected String questionNumber;
    protected String flags;
    protected String parameters;
    protected String color;
    protected CommunicationHub hub;
    protected JFrame frame;
    
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
        frame.setSize(300,400);
        frame.add(this);
        frame.setVisible(true);
    }
    
    public void close() {
        frame.setVisible(false);
        frame.dispose();
    }

    public void sendResponse(String values) {
        hub.sendQuestionResponse(values, questionNumber);
    }

    
    
}
