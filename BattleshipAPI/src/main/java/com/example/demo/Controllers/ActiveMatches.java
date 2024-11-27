package com.example.demo.Controllers;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.example.demo.Utility;
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
    	String username = usernames.get(session.getId());
    	
    	sessions.remove(username);
    	usernames.remove(session.getId());
    	
    	// finalize match if both players disconnect
    	try {
    		Match match = matchRepository.findByIsActiveTrueAndP1OrIsActiveTrueAndP2(username, username).get();
    		
    		String opponent;
        	
        	if(match.getP1().equals(username)) {
        		opponent = match.getP2();
        	} else {
        		opponent =  match.getP1();
        	}
        	
        	if(!sessions.containsKey(opponent)) {
        		if(match.getCurrentPlayer().equals(opponent)) {
        			match.setWinner(username);
        		} else {
        			match.setWinner(opponent);
        		}
        		
        		match.setActive(false);
        		matchRepository.save(match);
        	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
    	
    			ObjectMapper mapper = new ObjectMapper();
                try {
                	SocketMessage messageData = mapper.readValue(message.getPayload(), SocketMessage.class);
        
                	String username = messageData.getUsername();
                	
                	if(!sessions.containsKey(username)) {
                		sessions.put(username, session);
                		usernames.put(session.getId(), username);
                	}
                	
                	TextMessage newMessage;
                	
                	Match match = matchRepository.findByIsActiveTrueAndP1OrIsActiveTrueAndP2(username, username).get();
       
                	boolean isP1;
                	String opponent;
                	
                	if(match.getP1().equals(username)) {
                		isP1 = true;
                		opponent = match.getP2();
                	} else {
                		isP1 = false;
                		opponent =  match.getP1();
                	}
                	
                	if (messageData.getMessageType().equals("move")) {
                    	if (!match.getCurrentPlayer().equals(username)) {
                    		throw new Exception("Current Player: " + opponent);
                    	}
                    	
                    	int x = messageData.getX();
                    	int y = messageData.getY();
                    	
                	
                		int[][] opponentGrid = null;
                		String opponentGridString = "";
                		
                		if (isP1) {
                			opponentGrid = Utility.getGridArray(match.getP2Grid());
                			opponentGridString = Utility.getUpdatedGridString(x, y, opponentGrid);
                			match.setP2Grid(opponentGridString);
                		} else {
                			opponentGrid = Utility.getGridArray(match.getP1Grid());
                			opponentGridString = Utility.getUpdatedGridString(x, y, opponentGrid);
                			match.setP1Grid(opponentGridString);
                		}
                		
                		match.setLastMovedAt(Utility.getTimeStamp());
                		
                		if(Utility.isWinner(opponentGrid)) {
                			match.setWinner(username);
                			match.setActive(false);
                		} else {
                			match.setCurrentPlayer(opponent);                			
                		}
                		
                		matchRepository.save(match);
                	}
                	
                	if(sessions.containsKey(opponent)) {
                    	newMessage = new TextMessage(mapper.writeValueAsString(match.getMatchWithHiddenGrid(opponent)));
                    	sessions.get(opponent).sendMessage(newMessage);
                    	
                    	newMessage = new TextMessage(mapper.writeValueAsString(match.getMatchWithHiddenGrid(username)));
                    	sessions.get(username).sendMessage(newMessage);
                	} else {
                		messageData.setMessage("Waiting for opponent to register");
                		newMessage = new TextMessage(mapper.writeValueAsString(messageData));
                		sessions.get(messageData.getUsername()).sendMessage(newMessage);
                	}
                } catch (NoSuchElementException e) {
                    e.printStackTrace();
                    session.sendMessage(new TextMessage(mapper.writeValueAsString("No Match Found")));           
                } catch (Exception e) {
                    e.printStackTrace();
                    session.sendMessage(new TextMessage(mapper.writeValueAsString(e.getMessage()))); 
                }
    }
}
