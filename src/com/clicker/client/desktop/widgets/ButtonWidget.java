package com.clicker.client.desktop.widgets;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JToggleButton;

import com.clicker.client.desktop.Question;


public class ButtonWidget extends Widget {

    protected JToggleButton button;
    protected boolean selected;
    
    public ButtonWidget() {
        super();
        selected = false;
    }

    protected ActionListener getActionListener() {
        return new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                System.out.println("button "+id+" clicked. button is selected? "+button.isSelected() + " button has text: "+button.getText());
                selected = !selected;
                button.setSelected(selected);
                parentQuestion.toggleButtons(id);
                parentQuestion.sendValues();
            }
        };
    }
    
    public void turnOff() {
        selected = false;
        button.setSelected(false);
//        button.invalidate();
    }
    
    @Override
    public String getValue() {
        if (button.isSelected()) {
            System.out.println("button is selected");
            return button.getText();
        } else {
            System.out.println("button is not selected");
            return " ";
        }
    }

    @Override
    public void setId(int newId){
        this.id = (newId+150) * 171;
    }

    @Override
    public int getType() {
        return Question.BUTTON_ID;
    }

    @Override
    public void buildWidget() {
        this.setLayout(new GridLayout(1,1));
        button = new JToggleButton(parameters[1]);
        if(parameters[2].equals("0")) {
            button.setSelected(false);
        } else if (parameters[2].equals("1")) {
            button.setSelected(true);
        }
        button.addActionListener(getActionListener() );
        this.add(button);        
    }
}
