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

// TODO: Auto-generated Javadoc
/**
 * The Class CommunicationHub.
 */
public class CommunicationHub {
    
    /** The _instance. */
    private static CommunicationHub _instance;
    
    /** The reader. */
    private BufferedReader reader;
    
    /** The writer. */
    private PrintWriter writer;
    
    /** The login string. */
    private String loginString;
    
    /** The socket. */
    private Socket socket;
    
    /** The reconnecter. */
    private Reconnecter reconnecter;
    
    /** The waiting for heartbeat. */
    private boolean waitingForHeartbeat = false;
    
    /** The heartbeat timer. */
    private Timer heartbeatTimer;
    
    /** The heartbeat seconds. */
    private int heartbeatSeconds = 15;
    
    /** The input manager. */
    private InputManagementThread inputManager;
    
    /** The visual. */
    private ClickerClientVisual visual;
    
    /** The client model. */
    private ClientModel clientModel;
    
    /** The question display. */
    private QuestionDisplay questionDisplay;
    
    /** The SEM i_ colo n_ separator. */
    public static String SEMI_COLON_SEPARATOR  = "`/;";
    
    /** The COLO n_ separator. */
    public static String COLON_SEPARATOR       = "`/:";
    
    /** The GRAV e_ separator. */
    public static String GRAVE_SEPARATOR       = "`/`";
    
    /** The AMPERSAN d_ separator. */
    public static String AMPERSAND_SEPARATOR   = "`/&";
    
    /** The A t_ separator. */
    public static String AT_SEPARATOR          = "`/@";
    
    /** The COMM a_ separator. */
    public static String COMMA_SEPARATOR       = "`/,";
    
    /** The Constant OPEN_MC_QUESTION. */
    public static final String OPEN_MC_QUESTION = "OpenMC";
    
    /** The Constant CLOSE_QUESTION. */
    public static final String CLOSE_QUESTION = "CloseQuestion";
    
    /** The Constant REQUEST_ID. */
    public static final String REQUEST_ID = "RequestID";
    
    /** The Constant REMOVE_OPTION. */
    public static final String REMOVE_OPTION = "Remove";
    
    /** The Constant CHECK_CONNECTION. */
    public static final String CHECK_CONNECTION = "CheckConnect";
    
    /** The Constant RESEND_ANSWER. */
    public static final String RESEND_ANSWER = "ResendAnswer";
    
    /** The Constant DUPLICATE_ID. */
    public static final String DUPLICATE_ID = "DuplicateID";
    
    /** The Constant OPEN_CLICKPAD. */
    public static final String OPEN_CLICKPAD = "OpenClickPad";
    
    /** The Constant OPEN_COMMAND. */
    public static final String OPEN_COMMAND = "Open";
    
    /** The Constant CLOSE_COMMAND. */
    public static final String CLOSE_COMMAND = "Close";
    
    /** The Constant SYSTEM_COMMAND. */
    public static final String SYSTEM_COMMAND = "System";
    
    /** The Constant CLICKPAD_OPTION. */
    public static final String CLICKPAD_OPTION = "ClickPad";
    
    /** The Constant INVALID_RESPONSE. */
    public static final String INVALID_RESPONSE = "InvalidResponse";
    
    /** The Constant INVALID_ADMIN. */
    public static final String INVALID_ADMIN = "InvalidAdmin";
    
    /** The Constant INVALID_INFORMATION. */
    public static final String INVALID_INFORMATION = "InvalidInformation";
    
    /** The Constant ADD_WIDGET. */
    public static final String ADD_WIDGET = "ADD";
    
    /** The Constant SET_GROUP. */
    public static final String SET_GROUP = "GROUP";
    
    /** The Constant SET_COLOR. */
    public static final String SET_COLOR = "COLOR";
    
    /** The Constant STILL_CONNECTED_REQUEST. */
    public static final String STILL_CONNECTED_REQUEST = "AreYouStillThere";
    
    /** The Constant STILL_CONNECTED_RESPONSE. */
    public static final String STILL_CONNECTED_RESPONSE = "YesImHere";

    /** The Constant EVERYONE_GROUP_STRING. */
    private static final String EVERYONE_GROUP_STRING = null;
    
    
    /**
     * Instantiates a new communication hub.
     */
    private CommunicationHub() {
        clientModel = ClientModel.getInstance();
    }
    
    /**
     * Question is active.
     *
     * @return true, if successful
     */
    private boolean questionIsActive() {
        return questionDisplay != null;
    }
    
    /**
     * Gets the single instance of CommunicationHub.
     *
     * @return single instance of CommunicationHub
     */
    public static synchronized CommunicationHub getInstance() {
        if (_instance == null) {
            _instance = new CommunicationHub();
        }
        return _instance;
    }
    
    /**
     * Sets the visual.
     *
     * @param visual the new visual
     */
    public void setVisual(ClickerClientVisual visual) {
        this.visual = visual;
    }
    
    /**
     * Send message.
     *
     * @param message the message
     */
    public void sendMessage(String message) {
        writer.println(message);
        writer.flush();        
    }
    
    /**
     * Sets the login info.
     *
     * @param login the new login info
     */
    public void setLoginInfo(String login) {
        loginString = login;
        
    }
    
    /**
     * Sets the ip.
     *
     * @param ip the new ip
     */
    public void setIp(String ip) {
        reconnecter = new Reconnecter(ip);
        new Thread(reconnecter).start();
    }
    
    /**
     * Read message.
     *
     * @return the string
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public String readMessage() throws IOException {
        System.out.println("trying to read");
        String s = reader.readLine();
        System.out.println("past readline, got: "+s);
        return s;
    }

    /**
     * Close connections.
     */
    public void closeConnections() {
        inputManager.stop();
        waitingForHeartbeat = false;
        heartbeatTimer.cancel();
    }
    
    /**
     * Got disconnected.
     */
    public void gotDisconnected() {
        closeConnections();  
        new Thread(reconnecter).start();
    }
    
    /**
     * The Class Reconnecter.
     */
    private class Reconnecter implements Runnable {
        
        /** The ip. */
        String ip;
        
        /** The server port. */
        int serverPort = 4321;
        
        /** The timeout. */
        int timeout = 5000;
        
        /** The is connected. */
        boolean isConnected;
        
        /**
         * Instantiates a new reconnecter.
         *
         * @param ip the ip
         */
        public Reconnecter(String ip) {
            this.ip = ip;
            isConnected = false;
        }
        
        /* (non-Javadoc)
         * @see java.lang.Runnable#run()
         */
        public void run() {
            int retryCount = 0;
            while (!isConnected && retryCount < 2) {
                try {               
                    SocketAddress sockaddr = new InetSocketAddress(ip, serverPort);
                    Socket newSocket = new Socket();
                    newSocket.connect(sockaddr, timeout);
                    newSocket.setKeepAlive(true);
                    socket = newSocket;
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    writer = new PrintWriter(socket.getOutputStream(), false);
                    sendMessage(loginString);
                    System.out.println("sent login: \n"+loginString);
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
    
    /**
     * Received heartbeat.
     */
    public void receivedHeartbeat() {
        waitingForHeartbeat = false;
    }
    
    
    /**
     * Start listening.
     */
    public void startListening() {
        heartbeatTimer = new Timer();
        heartbeatTimer.scheduleAtFixedRate(new HeartbeatTask(), heartbeatSeconds * 1000, heartbeatSeconds * 1000);
        inputManager = new InputManagementThread();
    }
    
    
    /**
     * The Class HeartbeatTask.
     */
    private class HeartbeatTask extends TimerTask {
        
        /* (non-Javadoc)
         * @see java.util.TimerTask#run()
         */
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

    /**
     * Builds the new question.
     */
    public void buildNewQuestion() {
        if (questionIsActive()) {
            questionDisplay.closeQuestion();
            questionDisplay = null;
        }
        questionDisplay = new QuestionDisplay();
    }
    
    /**
     * Close question.
     */
    public void closeQuestion() {
        questionDisplay.closeQuestion();
    }
    
    /**
     * Open custom question.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public void openCustomQuestion(String questionNumber, String flags, String parameters, String color) {
        buildNewQuestion();
        questionDisplay.openCustomQuestion(questionNumber, flags, parameters, color);
    }

    /**
     * Open mouse control.
     *
     * @param questionNumber the question number
     * @param flags the flags
     * @param parameters the parameters
     * @param color the color
     */
    public void openMouseControl(String questionNumber, String flags, String parameters, String color) {
        buildNewQuestion();
        questionDisplay.openMouseQuestion(questionNumber, flags, parameters, color);
    }

    /**
     * Removes the option.
     *
     * @param widgetNumber the widget number
     */
    public void removeOption(int widgetNumber) {
        questionDisplay.removeOption(widgetNumber);
    }

    /**
     * Sets the default color.
     *
     * @param color the new default color
     */
    public void setDefaultColor(String color) {
        clientModel.setColor(color);
    }

    /**
     * Sets the group.
     *
     * @param group the new group
     */
    public void setGroup(String group) {
        clientModel.setGroup(group);
    }

    /**
     * Alert user.
     *
     * @param message the message
     */
    public void alertUser(String message) {
        visual.alertUser(message);
    }

    /**
     * Send question response.
     *
     * @param values the values
     * @param questionNumber the question number
     */
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
