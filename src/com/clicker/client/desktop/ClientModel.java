package com.clicker.client.desktop;


// TODO: Auto-generated Javadoc
/**
 * The Class ClientModel.
 */
public class ClientModel {
    
    /** The _instance. */
    private static ClientModel _instance;
    
    /** The group. */
    private String group;
    
    /** The name. */
    private String name;
    
    /** The color. */
    private String color;
    
    /**
     * Instantiates a new client model.
     */
    private ClientModel() {
        
    }
    
    /**
     * Gets the single instance of ClientModel.
     *
     * @return single instance of ClientModel
     */
    public static ClientModel getInstance() {
        if (_instance == null) {
            _instance = new ClientModel();
        }
        return _instance;
    }
    
    /**
     * Gets the group.
     *
     * @return the group
     */
    public String getGroup() {
        return group;
    }
    
    /**
     * Sets the group.
     *
     * @param group the new group
     */
    public void setGroup(String group) {
        this.group = group;
    }
    
    /**
     * Gets the name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }
    
    /**
     * Sets the name.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Gets the color.
     *
     * @return the color
     */
    public String getColor() {
        return color;
    }
    
    /**
     * Sets the color.
     *
     * @param color the new color
     */
    public void setColor(String color) {
        this.color = color;
    }

    /**
     * Sets the flags.
     *
     * @param flags the new flags
     */
    public void setFlags(String flags) {
        //TODO: write in flag setting code from CustomizableQuestion
    }
    
    /**
     * Clear flags.
     */
    public void clearFlags() {
        //TODO: write in a means of clearing all flags
    }
    
    /**
     * Everyone flag is set.
     *
     * @return true, if successful
     */
    public boolean everyoneFlagIsSet() {
        // TODO Auto-generated method stub
        return false;
    }
    
}
