package com.clicker.client.desktop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

public class CommunicationHub {
    
    private static CommunicationHub _instance;
    
    private BufferedReader reader;
    private PrintWriter writer;
    private String loginString;
    private Socket socket;
    private Reconnecter reconnecter;
    private boolean waitingForHeartbeat = false;
    private Timer heartbeatTimer;
    private int heartbeatSeconds = 15;
    private InputManagementThread inputManager;
    private ClickerClientVisual visual;
    private ClientModel clientModel;
    private QuestionDisplay questionDisplay;
    
    public static String SEMI_COLON_SEPARATOR  = "`/;";
    public static String COLON_SEPARATOR       = "`/:";
    public static String GRAVE_SEPARATOR       = "`/`";
    public static String AMPERSAND_SEPARATOR   = "`/&";
    public static String AT_SEPARATOR          = "`/@";
    public static String COMMA_SEPARATOR       = "`/,";
    
    public static final String OPEN_MC_QUESTION = "OpenMC";
    public static final String CLOSE_QUESTION = "CloseQuestion";
    public static final String REQUEST_ID = "RequestID";
    public static final String REMOVE_OPTION = "Remove";
    public static final String CHECK_CONNECTION = "CheckConnect";
    public static final String RESEND_ANSWER = "ResendAnswer";
    public static final String DUPLICATE_ID = "DuplicateID";
    public static final String OPEN_CLICKPAD = "OpenClickPad";
    
    public static final String OPEN_COMMAND = "Open";
    public static final String CLOSE_COMMAND = "Close";
    public static final String SYSTEM_COMMAND = "System";
    public static final String CLICKPAD_OPTION = "ClickPad";
    public static final String INVALID_RESPONSE = "InvalidResponse";
    public static final String INVALID_ADMIN = "InvalidAdmin";
    public static final String INVALID_INFORMATION = "InvalidInformation";
    public static final String ADD_WIDGET = "ADD";
    public static final String SET_GROUP = "GROUP";
    public static final String SET_COLOR = "COLOR";
    public static final String STILL_CONNECTED_REQUEST = "AreYouStillThere";
    public static final String STILL_CONNECTED_RESPONSE = "YesImHere";

    private static final String EVERYONE_GROUP_STRING = null;
    
    
    private CommunicationHub() {
        clientModel = ClientModel.getInstance();
    }
    
    public static synchronized CommunicationHub getInstance() {
        if (_instance == null) {
            _instance = new CommunicationHub();
        }
        return _instance;
    }
    
    public void setVisual(ClickerClientVisual visual) {
        this.visual = visual;
    }
    
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();        
    }
    
    public void setLoginInfo(String login) {
        loginString = login;
    }
    
    public void setIp(String ip) {
        reconnecter = new Reconnecter(ip);
        new Thread(reconnecter).start();
    }
    
    public String readMessage() throws IOException {
        return reader.readLine();
    }

    public void closeConnections() {
        inputManager.stop();
        waitingForHeartbeat = false;
        heartbeatTimer.cancel();
    }
    
    public void gotDisconnected() {
        closeConnections();  
        new Thread(reconnecter).start();
    }
    
    private class Reconnecter implements Runnable {
        String ip;
        int adminPort = 7700;
        int timeout = 5000;
        boolean isConnected;
        
        public Reconnecter(String ip) {
            this.ip = ip;
            isConnected = false;
        }
        
        public void run() {
            int retryCount = 0;
            while (!isConnected && retryCount < 2) {
                try {               
                    SocketAddress sockaddr = new InetSocketAddress(ip, adminPort);
                    Socket newSocket = new Socket();
                    newSocket.connect(sockaddr, timeout);
                    newSocket.setKeepAlive(true);
                    socket = newSocket;
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), false);
                    sendMessage(loginString);
                    System.out.println("sent username ");
                    isConnected = true;
                    startListening();
                } catch (Exception e) {
                    try {
                        System.out.println("failed to connect, waiting and trying again");
                        retryCount++;
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {}
                }
            }
            //     Log.d("RECONNECT", "Out of reconnection loop");
            //     reconnectingDialog.dismiss();
            if (isConnected) {
                //         Log.d("RECONNECT", "Calling success message");
                System.out.println("connected");
                //startListening();
                //subHandler.sendEmptyMessage(RECONNECT_SUCCESS);     
            } else {
                System.out.println("not connected");
                //          Log.d("RECONNECT", "Calling failed message");
                //subHandler.sendEmptyMessage(RECONNECT_FAILED);
            }
        }
        
    }
    
    public void receivedHeartbeat() {
        waitingForHeartbeat = false;
    }
    
    
    public void startListening() {
        heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new HeartbeatTask(), 15000, heartbeatSeconds * 1000);
        inputManager = new InputManagementThread();
    }
    
    
    private class HeartbeatTask extends TimerTask {
        public void run() {
            if (waitingForHeartbeat) {
                try {
                    socket.close();
                } catch (IOException e) {}
                gotDisconnected();
            } else {
                waitingForHeartbeat = true;
                sendMessage(STILL_CONNECTED_REQUEST);
            }
        }
    }

    public void closeQuestion() {
        questionDisplay.closeQuestion();
    }
    
    public void openCustomQuestion(String questionNumber, String flags, String parameters, String color) {
        questionDisplay.openCustomQuestion(questionNumber, flags, parameters, color);
    }

    public void openMouseControl(String questionNumber, String flags, String parameters, String color) {
        questionDisplay.openMouseQuestion(questionNumber, flags, parameters, color);
    }

    public void removeOption(int widgetNumber) {
        questionDisplay.removeOption(widgetNumber);
    }

    public void setDefaultColor(String color) {
        clientModel.setColor(color);
    }

    public void setGroup(String group) {
        clientModel.setGroup(group);
    }

    public void alertUser(String message) {
        visual.alertUser(message);
    }

    public void sendQuestionResponse(String values, String questionNumber) {
        String group = "";
        if(clientModel.everyoneFlagIsSet()){
            group = EVERYONE_GROUP_STRING;
        } else {
            group = clientModel.getGroup();
        }
           sendMessage(group+SEMI_COLON_SEPARATOR + questionNumber 
                    + SEMI_COLON_SEPARATOR + values);

    }

    
}
