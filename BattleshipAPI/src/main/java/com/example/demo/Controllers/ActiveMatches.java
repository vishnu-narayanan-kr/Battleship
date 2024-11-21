package com.example.demo.Controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.Models.Match;
import com.example.demo.Models.SocketMessage;
import com.example.demo.Repositories.MatchRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ActiveMatches extends TextWebSocketHandler {
    private static Map<String, WebSocketSession> sessions = new HashMap<>();
    private static Map<String, String> usernames = new HashMap<>();
    
    private static MatchRepository matchRepository;
    
    public ActiveMatches(MatchRepository matchRepository) {
    	ActiveMatches.matchRepository = matchRepository;
    }
    
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
        
                	String username = messageData.getUsername();
                	
                	if(!sessions.containsKey(username)) {
                		sessions.put(username, session);
                		usernames.put(session.getId(), username);
                	}
                	
                	TextMessage newMessage;
                	
                	Match match = matchRepository.findByIsActiveTrueAndP1OrP2(username, username).get();
                	String opponent = match.getP1().equals(username) ? match.getP2() : match.getP1();
                	
                	if(sessions.containsKey(opponent)) {
                    	newMessage = new TextMessage(mapper.writeValueAsString(match));
                    	
                    	sessions.get(opponent).sendMessage(newMessage);
                    	sessions.get(username).sendMessage(newMessage);
                	} else {
                		messageData.setMessage("Waiting for opponent to register");
                		newMessage = new TextMessage(mapper.writeValueAsString(messageData));
                		sessions.get(messageData.getUsername()).sendMessage(newMessage);
                	}
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                } 
                catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("Error");
                }
    }
}
