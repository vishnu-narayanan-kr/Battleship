package com.example.demo;

import java.io.IOException;
import java.util.*;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.Models.SocketMessage;
import com.fasterxml.jackson.databind.ObjectMapper;

public class WebSocketHandler extends TextWebSocketHandler {

    private static Map<String, WebSocketSession> sessions = new HashMap<>();
    private static Map<String, String> usernames = new HashMap<>();
    
    /*
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        
    }
    */
    
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
    	sessions.remove(usernames.get(session.getId()));
    	usernames.remove(session.getId());
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	
                try {
                	ObjectMapper mapper = new ObjectMapper();
                	SocketMessage messageData = mapper.readValue(message.getPayload(), SocketMessage.class);
        
                	if(!sessions.containsKey(messageData.getUsername())) {
                		sessions.put(messageData.getUsername(), session);
                		usernames.put(session.getId(), messageData.getUsername());
                	} 
                	TextMessage newMessage;
                	
                	if(sessions.containsKey(messageData.getOpponent())) {
                		//if(sessions.get(messageData.getOpponent()).isOpen()) {
                    		newMessage = new TextMessage(mapper.writeValueAsString(messageData));
                    		sessions.get(messageData.getOpponent()).sendMessage(newMessage);
                		//} else {
                			//sessions.remove(messageData.getOpponent());
                		//}
                	} else {
                		messageData.setMessage("Waiting for opponent to register");
                		newMessage = new TextMessage(mapper.writeValueAsString(messageData));
                		sessions.get(messageData.getUsername()).sendMessage(newMessage);
                	}
                } catch (Exception e) {
                    e.printStackTrace();
                }
    }
}
