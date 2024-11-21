package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import com.example.demo.Controllers.ActiveMatches;
import com.example.demo.Repositories.MatchRepository;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final MatchRepository matchRepository;

    @Autowired
    public WebSocketConfig(MatchRepository matchRepository) {
        this.matchRepository = matchRepository;
    }
    
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(activeMatches(), "/websocket").setAllowedOrigins("*");
    }
    
    @Bean
    public ActiveMatches activeMatches() {
        return new ActiveMatches(matchRepository);
    }
}
