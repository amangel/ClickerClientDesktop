package com.clicker.client.desktop;

import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;

import com.clicker.client.desktop.widgets.ButtonWidget;
import com.clicker.client.desktop.widgets.ComboWidget;
import com.clicker.client.desktop.widgets.QrTextWidget;
import com.clicker.client.desktop.widgets.SliderWidget;
import com.clicker.client.desktop.widgets.TextBoxWidget;
import com.clicker.client.desktop.widgets.TextQWidget;
import com.clicker.client.desktop.widgets.TextViewButtonWidget;
import com.clicker.client.desktop.widgets.TextViewWidget;
import com.clicker.client.desktop.widgets.ToggleWidget;
import com.clicker.client.desktop.widgets.Widget;


public class Question extends JPanel {
    
    //tags for widget creation
    protected final static String   BUTTON    = "B";
    public    final static int      BUTTON_ID = 0;
    protected final static String   SLIDER    = "SLIDE";
    public    final static int      SLIDER_ID = 1;
    protected final static String   TOGGLE    = "TOG";
    public    final static int      TOGGLE_ID = 2;
    protected final static String   COMBO     = "COMBO";
    public    final static int      COMBO_ID  = 3;
    protected final static String   TEXTBOX   = "TEXTBOX";
    public    final static int      TEXTBOX_ID = 4;
    protected final static String   TEXTQ     = "TEXTQ";
    public    final static int      TEXTQ_ID  = 5;
    protected final static String   TEXTVIEW  = "TEXTVIEW";
    public    final static int      TEXTVIEW_ID = 6;
    protected final static String   TVBUTTON  = "TVBUTTON";
    public    final static int      TVBUTTON_ID = 7;
    protected final static String   QRTEXT    = "QRTEXT";
    public    final static int      QRTEXT_ID = 8;
    
    private ArrayList<Widget> widgets;
    private HashMap<String, Class<?>> widgetMap;
    private ActiveQuestion parentQuestion;
    
    public Question(String parameters) {
        this.setLayout(new GridLayout(8, 1));
        buildHashmap();
        for (String parameter : parameters.split(CommunicationHub.COMMA_SEPARATOR)) {
            String[] parameterParts = parameter.split(CommunicationHub.COLON_SEPARATOR);
            try {
                Widget widget = (Widget) widgetMap.get(parameterParts[0]).newInstance(); 
                widget.setParameters(parameterParts);
                widget.setQuestion(this);
                widgets.add(widget);
                this.add(widget);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
    
    public void setParent(ActiveQuestion parent) {
        parentQuestion = parent;
    }

    private void buildHashmap() {
        widgetMap.put(BUTTON, ButtonWidget.class);
        widgetMap.put(SLIDER, SliderWidget.class);
        widgetMap.put(TOGGLE, ToggleWidget.class);
        widgetMap.put(COMBO,  ComboWidget.class);
        widgetMap.put(TEXTBOX, TextBoxWidget.class);
        widgetMap.put(TEXTQ,  TextQWidget.class);
        widgetMap.put(TEXTVIEW, TextViewWidget.class);
        widgetMap.put(TVBUTTON, TextViewButtonWidget.class);
        widgetMap.put(QRTEXT, QrTextWidget.class);
        widgets = new ArrayList<Widget>(10);
    }

    public void remove(int widgetNumber) {
        widgets.get(widgetNumber).hide();
    }

    public void toggleButtons(int id) {
        for (Widget widget : widgets) {
            if (widget.getType() == BUTTON_ID && widget.getId() != id) {
                ((ButtonWidget)widget).turnOff();
            }
        }
    }

    public void sendValues() {
        parentQuestion.sendResponse(getValues());
    }
    
    private String getValues() {
        String str = "";
        if(widgets.size() > 0){
            for (Widget widget : widgets) {
                str += widget.getValue() + CommunicationHub.COMMA_SEPARATOR;
            }
        } else { //if there are no widgets with values to retrieve
            str += " ";
        }
        int length = 0;
        if (str.length() > 1){
            length = str.length() - 3;
        } else {
            length = 1;
        }
        return str.substring(0, length);
    }
}
