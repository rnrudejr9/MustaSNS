package com.example.mutsasnsproject.configuration;

import com.example.mutsasnsproject.chat.ChatHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@RequiredArgsConstructor
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {
    private final ChatHandler chatHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(chatHandler,"ws/chat").setAllowedOrigins("*");
        //도메인이 다른 서버에서도 접속 가능하도록 cors : setAllowedOrigins
        // ws://localhost:8080/chat 으로 커넥션 연결 및 통신 준비
    }
}
