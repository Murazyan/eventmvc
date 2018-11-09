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
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import org.springframework.web.socket.server.support.DefaultHandshakeHandler;

import java.util.concurrent.Executors;

@Configuration
@EnableWebSocket
public class WebsocketConfig implements WebSocketConfigurer {

//    @Bean
//    public ServletServerContainerFactoryBean createWebSocketContainer() {
//        ServletServerContainerFactoryBean container = new ServletServerContainerFactoryBean();
//        container.setMaxTextMessageBufferSize(8192);
//        container.setMaxBinaryMessageBufferSize(8192);
//        return container;
//    }

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