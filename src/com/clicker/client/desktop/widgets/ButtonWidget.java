package com.clicker.client.desktop.widgets;

import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import com.clicker.client.desktop.Question;


public class ButtonWidget extends Widget {

    protected JToggleButton button;
    
    public ButtonWidget() {
        layout.setLayout(new GridLayout(1,1));
        button = new JToggleButton(parameters[1]);
        if(parameters[2].equals("0")) {
            button.setSelected(false);
        } else if (parameters[2].equals("1")) {
            button.setSelected(true);
        }
        button.addActionListener(getActionListener() );
        layout.add(button);
    }

    protected ActionListener getActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                button.setSelected(true);
                parentQuestion.toggleButtons(id);
                parentQuestion.sendValues();
            }};
    }
    
    @Override
    public void paintComponent(Graphics g) {
        layout.paintComponents(g);
    }
    
    public void turnOff() {
        button.setSelected(false);
    }
    
    @Override
    public String getValue() {
        if (button.isSelected()) {
            return button.getText().toString();
        } else {
            return " ";
        }
    }

    @Override
    public void setId(int id){
        this.id = (id+150) * 171;
    }

    @Override
    public int getType() {
        return Question.BUTTON_ID;
    }
}
