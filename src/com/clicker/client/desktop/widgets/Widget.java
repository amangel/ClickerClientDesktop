package com.clicker.client.desktop.widgets;

import javax.swing.JPanel;

import com.clicker.client.desktop.Question;


public abstract class Widget extends JPanel {

    protected String[] parameters;
    protected Question parentQuestion;
    protected int id;
    
    public Widget() {
    }
    
    public void hide() {
        this.setVisible(false);
    }
    
    public void show() {
        this.setVisible(true);
    }
    
    public abstract String getValue();
    public abstract void setId(int id);
    public abstract int getType();
    public abstract void buildWidget();
    
    public void setParameters(String[] parameterParts) {
        parameters = parameterParts;
        buildWidget();
    }
    
    public void setQuestion(Question parent) {
        parentQuestion = parent;
    }
    
    public int getId() {
        return id;
    }
    
    
}
