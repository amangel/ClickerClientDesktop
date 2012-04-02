package com.clicker.client.desktop.widgets;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.clicker.client.desktop.Question;


public class ToggleWidget extends ButtonWidget {

    @Override
    protected ActionListener getActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                toggleButton();
                parentQuestion.sendValues();
            }};
    }
    
    private void toggleButton() {
        button.setSelected(!button.isSelected());
    }
    
    @Override
    public void setId(int id){
        this.id = (id+150) * 271;
    }
    
    @Override
    public int getType() {
        return Question.TOGGLE_ID;
    }
}
