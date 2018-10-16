package com.example.eventmvc.socket.socketConfig;

import com.example.eventmvc.socket.socketEndpoints.SaveAndSendMessages;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ConcurrentTaskScheduler;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

import java.util.concurrent.Executors;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {


    @Bean
    public WebSocketHandler saveAndSendMessages() {
        return new SaveAndSendMessages();
    }


    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(saveAndSendMessages(), "/saveAndSendMessages");
//        registry.addHandler(new MyMessageHandler2(), "/websocket2");
    }
}