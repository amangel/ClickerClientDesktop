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



public class ClickerClientVisual {
    
    private JFrame interactionFrame;
    private JFrame loginFrame;
    private JTextField loginIPText;
    private CommunicationHub hub;
    private ClientModel clientModel;
    private JTextField loginPortText;
    private JTextField adminNameText;
    private JTextField groupNameText;
    private JTextField nameToUseText;
    private JLabel groupNameLabel;
    
    public ClickerClientVisual() {
        hub = CommunicationHub.getInstance();
        hub.setVisual(this);
        clientModel = ClientModel.getInstance();
        buildLoginFrame();
        buildInteractionFrame();
    }
    
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
        
        loginIPText.setText("134.161.43.255");
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
    
    private void doLogin(String ip, String port, String admin, String group, String name) {
        hub.setLoginInfo(buildLoginString(ip, port, admin, group, name));
        hub.setIp(ip); 
        loginFrame.setVisible(false);
        interactionFrame.setVisible(true);
        
    }
    
    private String buildLoginString(String ip, String port, String admin, String group, String name) {
        // name`/;mac`/;admin`/,group
        String toReturn = name;
        try {
            toReturn += "`/;" + NetworkInterface.getByInetAddress(InetAddress.getLocalHost()).getHardwareAddress();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        toReturn += "`/;" + admin + "`/," + group;
        System.out.println(toReturn);
        return toReturn;
    }
    
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

    protected void disconnectFromServer() {
        hub.closeConnections();
    }

    public void alertUser(String message) {
        JOptionPane.showMessageDialog(interactionFrame, message,
                "Alert", JOptionPane.WARNING_MESSAGE);
    }
    
    public void redraw() {
        groupNameLabel.setText(clientModel.getGroup());
    }
}
