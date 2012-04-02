package com.clicker.client.desktop;


public class ClientModel {
    
    private static ClientModel _instance;
    
    private String group;
    private String name;
    private String color;
    
    private ClientModel() {
        
    }
    
    public static ClientModel getInstance() {
        if (_instance == null) {
            _instance = new ClientModel();
        }
        return _instance;
    }
    
    public String getGroup() {
        return group;
    }
    
    public void setGroup(String group) {
        this.group = group;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }

    public void setFlags(String flags) {
        //TODO: write in flag setting code from CustomizableQuestion
    }
    
    public void clearFlags() {
        //TODO: write in a means of clearing all flags
    }
    
    public boolean everyoneFlagIsSet() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
