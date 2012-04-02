package com.clicker.client.desktop.widgets;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.clicker.client.desktop.Question;


public abstract class Widget extends JComponent {

    protected String[] parameters;
    protected JPanel layout;
    protected Question parentQuestion;
    protected int id;
    
    public void hide() {
        this.setVisible(false);
    }
    
    public void show() {
        this.setVisible(true);
    }
    
    public abstract String getValue();
    public abstract void setId(int id);
    public abstract int getType();
    
    public void setParameters(String[] parameterParts) {
        parameters = parameterParts;
    }
    
    public void setQuestion(Question parent) {
        parentQuestion = parent;
    }
    
    public int getId() {
        return id;
    }
    
    
}
