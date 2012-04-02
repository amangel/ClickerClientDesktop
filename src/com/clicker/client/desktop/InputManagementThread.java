package com.clicker.client.desktop;

import java.io.IOException;


public class InputManagementThread {
    

    Thread thread;
    protected boolean threadCanRun;
    private CommunicationHub hub;
    
    public InputManagementThread() {
        threadCanRun = true;
        hub = CommunicationHub.getInstance();
        thread = new Thread( new InputThread() );
        thread.start();
    }
    
    public void stop() {
        threadCanRun = false;
    }
    
    private class InputThread implements Runnable {
        
        @Override
        public void run() {
            String str = "";
            while (threadCanRun) {
                try {
                    str = hub.readMessage();
                    if (str == null) {
                        hub.gotDisconnected();
                        break;
                    } else if (str.equals(CommunicationHub.STILL_CONNECTED_RESPONSE)){
                        hub.receivedHeartbeat();
                    } else {  
                        String color = "";
                        String[] input = str.split(CommunicationHub.SEMI_COLON_SEPARATOR);
                        
                        if(input.length >= 1){
                            if(input[0].equals(CommunicationHub.OPEN_COMMAND)){
                                if(input.length<5){
                                    color="default";
                                }
                                else {
                                    color = input[4];
                                }
                                if(isQuestionActive()){
                                    hub.closeQuestion();
                                }
                                //                                    openCustom(input[1], input[2], input[3], col);
                                hub.openCustomQuestion(input[1],input[2], input[3], color);
                            } else if(input[0].equals(CommunicationHub.OPEN_CLICKPAD)){
                                if(input.length<5){
                                    color="default";
                                }
                                else {
                                    color = input[4];
                                }
                                if(isQuestionActive()){
                                    hub.closeQuestion();
                                }
                                //                                    openMouseControl(input[1], input[2], input[3], color);
                                hub.openMouseControl(input[1], input[2], input[3], color);
                            }
                            else if (input[0].equals(CommunicationHub.CLOSE_COMMAND)){
                                hub.closeQuestion();
                                //Toast.makeText(this, "received close", Toast.LENGTH_SHORT).show();
                            }
                            else if (input[0].equals(CommunicationHub.SYSTEM_COMMAND)){
                                if(input[1].equals(CommunicationHub.REMOVE_OPTION)){
                                    //                                        Message mess = Message.obtain(subHandler, 
                                    //                                                REMOVE_MC_OPTION, 
                                    //                                                Integer.parseInt(input[2]), 0); //last 0 to properly
                                    //create the message type
                                    //                                        subHandler.sendMessage(mess);
                                    hub.removeOption(Integer.parseInt(input[2]));
                                } else if (input[1].equals(CommunicationHub.SET_COLOR)){
                                    hub.setDefaultColor(input[2]);
                                } else if (input[1].equals(CommunicationHub.SET_GROUP)){
                                    hub.setGroup(input[2]);
                                }
                            }
                            else if (input[0].equals(CommunicationHub.INVALID_RESPONSE)){
                                hub.alertUser("The administrator received an invalid response that it could not process."); 
                            }
                            else if (input[0].equals(CommunicationHub.INVALID_ADMIN)){
                                hub.alertUser("You attempted to connect to an invalid administrator." 
                                            + "\nThe connection was refused.");
                                hub.closeConnections();
                            }
                            else if (input[0].equals(CommunicationHub.INVALID_INFORMATION)){
                                hub.alertUser("The admin received an invalid response that it could not process.");
                                
                                
                            } else {
                                hub.alertUser("Client received an invalid command that it could not process:\n"+str);
                            }
                            
                        } else {
                            System.out.println("Not connected, but still trying to run inputManagementThread. Received: \n"+str);
                        }
                    }
                    Thread.sleep(100);
                } catch (IOException e) {
                } catch (InterruptedException e) {}
                System.out.println("Client message thread should be dying");
            }
            
        } 
    }
    
    public boolean isQuestionActive() {
        // TODO Auto-generated method stub
        return false;
    }
    
}