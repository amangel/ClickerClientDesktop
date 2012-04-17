package com.clicker.client.desktop;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;



// TODO: Auto-generated Javadoc
/**
 * The Class ClickerClientVisual.
 */
public class ClickerClientVisual {
    
    private static final String ALERT = "Alert";

    private static final String UNGROUPED = "Ungrouped";

    private static final String COMMA_SEPARATOR = "`/,";

    private static final String SEMI_COLON_SEPARATOR = "`/;";

    private static final String DEFAULT_CONNECTION_IP = "134.161.41.198";

    /** The interaction frame. */
    private JFrame interactionFrame;
    
    /** The login frame. */
    private JFrame loginFrame;
    
    /** The login ip text. */
    private JTextField loginIPText;
    
    /** The hub. */
    private CommunicationHub hub;
    
    /** The client model. */
    private ClientModel clientModel;
    
    /** The login port text. */
    private JTextField loginPortText;
    
    /** The admin name text. */
    private JTextField adminNameText;
    
    /** The group name text. */
    private JTextField groupNameText;
    
    /** The name to use text. */
    private JTextField nameToUseText;
    
    /** The group name label. */
    private JLabel groupNameLabel;
    
    /**
     * Instantiates a new clicker client visual.
     */
    public ClickerClientVisual() {
        hub = CommunicationHub.getInstance();
        hub.setVisual(this);
        clientModel = ClientModel.getInstance();
        buildLoginFrame();
        buildInteractionFrame();
    }
    
    /**
     * Builds the login frame.
     */
    private void buildLoginFrame() {
        loginFrame = new JFrame();
        loginFrame.setTitle("ClickerClient");
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setSize(400,400);
        JPanel loginPanel = new JPanel();
        loginPanel.setLayout(new GridLayout(6,1));
        JPanel loginIPPanel = new JPanel();
        JPanel loginPortPanel = new JPanel();
        JPanel adminNamePanel = new JPanel();
        JPanel groupNamePanel = new JPanel();
        JPanel nameToUsePanel = new JPanel();
        
        GridLayout oneByTwoGridLayout = new GridLayout(1,2);
        loginIPPanel.setLayout(oneByTwoGridLayout);
        loginPortPanel.setLayout(oneByTwoGridLayout);
        adminNamePanel.setLayout(oneByTwoGridLayout);
        groupNamePanel.setLayout(oneByTwoGridLayout);
        nameToUsePanel.setLayout(oneByTwoGridLayout);
        
        JLabel loginIPLabel = new JLabel("Login IP: ");
        JLabel loginPortLabel = new JLabel("Login port: ");
        JLabel adminNameLabel = new JLabel("Teacher: ");
        JLabel groupNameLabel = new JLabel("Group: ");
        JLabel nameToUseLabel = new JLabel("Name to use: ");
        
        loginIPText = new JTextField();
        loginPortText = new JTextField();
        adminNameText = new JTextField();
        groupNameText = new JTextField();
        nameToUseText = new JTextField();
        
        loginIPText.setText(DEFAULT_CONNECTION_IP);
        loginPortText.setText("4321");
        adminNameText.setText("frederis");
        groupNameText.setText("");
        nameToUseText.setText("testClient");
        
        loginIPPanel.add(loginIPLabel);
        loginIPPanel.add(loginIPText);
        
        loginPortPanel.add(loginPortLabel);
        loginPortPanel.add(loginPortText);
        
        adminNamePanel.add(adminNameLabel);
        adminNamePanel.add(adminNameText);
        
        groupNamePanel.add(groupNameLabel);
        groupNamePanel.add(groupNameText);
        
        nameToUsePanel.add(nameToUseLabel);
        nameToUsePanel.add(nameToUseText);
        
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener(){
            
            @Override
            public void actionPerformed(ActionEvent arg0) {
                doLogin(loginIPText.getText(),
                        loginPortText.getText(),
                        adminNameText.getText(),
                        groupNameText.getText(),
                        nameToUseText.getText());
            }});
        
        loginPanel.add(loginIPPanel);
        loginPanel.add(loginPortPanel);
        loginPanel.add(adminNamePanel);
        loginPanel.add(groupNamePanel);
        loginPanel.add(nameToUsePanel);
        loginPanel.add(loginButton);
        //loginPanel.add(loadConsumersButton);
        
        //loadConsumersFromSubdirectory();
        
        loginFrame.add(loginPanel);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginFrame.setVisible(true);
    }
    
    /**
     * Do login.
     *
     * @param ip the ip
     * @param port the port
     * @param admin the admin
     * @param group the group
     * @param name the name
     */
    private void doLogin(String ip, String port, String admin, String group, String name) {
        hub.setLoginInfo(buildLoginString(ip, port, admin, group, name));
        hub.setIp(ip); 
        loginFrame.setVisible(false);
        interactionFrame.setVisible(true);
        
    }
    
    /**
     * Builds the login string.
     *
     * @param ip the ip
     * @param port the port
     * @param admin the admin
     * @param group the group
     * @param name the name
     * @return the string
     */
    private String buildLoginString(String ip, String port, String admin, String group, String name) {
        // name`/;mac`/;admin`/,group
        String toReturn = name;
        try {
            toReturn += SEMI_COLON_SEPARATOR + NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        toReturn += SEMI_COLON_SEPARATOR + admin + COMMA_SEPARATOR;
        if(group.length() == 0) {
            toReturn += UNGROUPED;
            clientModel.setGroup(UNGROUPED);
        } else {
            toReturn += group;
            clientModel.setGroup(group);
        }
        System.out.println(toReturn);
        return toReturn;
    }
    
    /**
     * Builds the interaction frame.
     */
    private void buildInteractionFrame() {
        interactionFrame = new JFrame();
        interactionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        interactionFrame.setSize(400,400);
        interactionFrame.setLayout(new GridLayout(3,1));
        
        groupNameLabel = new JLabel();
        JLabel waiting = new JLabel("Waiting for questions...");
        JButton disconnectButton = new JButton("Disconnect from server");
        disconnectButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent arg0) {
                disconnectFromServer();
                interactionFrame.setVisible(false);
                interactionFrame.dispose();
            }});
        
        interactionFrame.add(groupNameLabel);
        interactionFrame.add(waiting);
        interactionFrame.add(disconnectButton);
    }

    /**
     * Disconnect from server.
     */
    protected void disconnectFromServer() {
        hub.closeConnections();
    }

    /**
     * Alert user.
     *
     * @param message the message
     */
    public void alertUser(String message) {
        JOptionPane.showMessageDialog(interactionFrame, message,
                ALERT, JOptionPane.WARNING_MESSAGE);
    }
    
    /**
     * Redraw.
     */
    public void redraw() {
        groupNameLabel.setText(clientModel.getGroup());
    }
}
