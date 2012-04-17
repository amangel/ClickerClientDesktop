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


// TODO: Auto-generated Javadoc
/**
 * The Class Question.
 */
public class Question extends JPanel {
    
    //tags for widget creation
    /** The Constant BUTTON. */
    protected final static String   BUTTON    = "B";
    
    /** The Constant BUTTON_ID. */
    public    final static int      BUTTON_ID = 0;
    
    /** The Constant SLIDER. */
    protected final static String   SLIDER    = "SLIDE";
    
    /** The Constant SLIDER_ID. */
    public    final static int      SLIDER_ID = 1;
    
    /** The Constant TOGGLE. */
    protected final static String   TOGGLE    = "TOG";
    
    /** The Constant TOGGLE_ID. */
    public    final static int      TOGGLE_ID = 2;
    
    /** The Constant COMBO. */
    protected final static String   COMBO     = "COMBO";
    
    /** The Constant COMBO_ID. */
    public    final static int      COMBO_ID  = 3;
    
    /** The Constant TEXTBOX. */
    protected final static String   TEXTBOX   = "TEXTBOX";
    
    /** The Constant TEXTBOX_ID. */
    public    final static int      TEXTBOX_ID = 4;
    
    /** The Constant TEXTQ. */
    protected final static String   TEXTQ     = "TEXTQ";
    
    /** The Constant TEXTQ_ID. */
    public    final static int      TEXTQ_ID  = 5;
    
    /** The Constant TEXTVIEW. */
    protected final static String   TEXTVIEW  = "TEXTVIEW";
    
    /** The Constant TEXTVIEW_ID. */
    public    final static int      TEXTVIEW_ID = 6;
    
    /** The Constant TVBUTTON. */
    protected final static String   TVBUTTON  = "TVBUTTON";
    
    /** The Constant TVBUTTON_ID. */
    public    final static int      TVBUTTON_ID = 7;
    
    /** The Constant QRTEXT. */
    protected final static String   QRTEXT    = "QRTEXT";
    
    /** The Constant QRTEXT_ID. */
    public    final static int      QRTEXT_ID = 8;
    
    /** The widgets. */
    private ArrayList<Widget> widgets;
    
    /** The widget map. */
    private HashMap<String, Class<? extends Widget>> widgetMap;
    
    /** The parent question. */
    private ActiveQuestion parentQuestion;
    
    /**
     * Instantiates a new question.
     *
     * @param parameters the parameters
     */
    public Question(String parameters) {
        this.setSize(300, 400);
        this.setLayout(new GridLayout(8, 1));
        buildHashmap();
        int count = 0;
        for (String parameter : parameters.split(CommunicationHub.COMMA_SEPARATOR)) {
            String[] parameterParts = parameter.split(CommunicationHub.COLON_SEPARATOR);
            try {
                Widget widget = widgetMap.get(parameterParts[0]).newInstance(); 
                widget.setSize(300, 50);
                widget.setId(count++);
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
    
    /**
     * Sets the parent.
     *
     * @param parent the new parent
     */
    public void setParent(ActiveQuestion parent) {
        parentQuestion = parent;
    }

    /**
     * Builds the hashmap.
     */
    private void buildHashmap() {
        widgetMap = new HashMap<String, Class<? extends Widget>>();
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

    /* (non-Javadoc)
     * @see java.awt.Container#remove(int)
     */
    public void remove(int widgetNumber) {
        widgets.get(widgetNumber).hide();
    }

    /**
     * Toggle buttons.
     *
     * @param id the id
     */
    public void toggleButtons(int id) {
        for (Widget widget : widgets) {
            System.out.println("checking toggle for button "+widget.getId());
            if (widget.getType() == BUTTON_ID && widget.getId() != id) {
                System.out.println("toggling button "+widget.getId() + " off");
                ((ButtonWidget)widget).turnOff();
            }
        }
        parentQuestion.forceRedraw();
    }

    /**
     * Send values.
     */
    public void sendValues() {
        parentQuestion.sendResponse(getValues());
    }
    
    /**
     * Gets the values.
     *
     * @return the values
     */
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
