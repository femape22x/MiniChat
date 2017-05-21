package com.minitwit.config;

import org.eclipse.jetty.websocket.api.*;
import org.eclipse.jetty.websocket.api.annotations.*;

import com.minitwit.App;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;

import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

@WebSocket
public class ChatWebSocketHandler {

    private String sender, msg;

    @OnWebSocketConnect
    public void onConnect(Session user) throws Exception {
    	System.out.println("hola Mundo"+user);
        String username = "User" + App.nextUserNumber++;
        App.userUsernameMap.put(user, username);
       
    }

    @OnWebSocketClose
    public void onClose(Session user, int statusCode, String reason) {
//        String username = Chat.userUsernameMap.get(user);
//        Chat.userUsernameMap.remove(user);
//        Chat.broadcastMessage(sender = "Server", msg = (username + " left the chat"));
    }

    @OnWebSocketMessage
    public void onMessage(Session user, String message) {
        JSONParser parser = new JSONParser();
        
          Object obj;
            System.out.println("ssss"+message);
        try {
            obj = parser.parse(message);
             JSONObject jsonObject = (JSONObject) obj;
         App.broadcastMessage(sender =(String) jsonObject.get("user") , msg = (String) jsonObject.get("message"));
        } catch (ParseException ex) {
            Logger.getLogger(ChatWebSocketHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

           
       
    }

}
  