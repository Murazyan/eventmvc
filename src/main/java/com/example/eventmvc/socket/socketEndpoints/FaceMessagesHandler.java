//package com.example.eventmvc.socket.socketEndpoints;
//
//import org.json.JSONObject;
//import org.springframework.web.socket.CloseStatus;
//import org.springframework.web.socket.TextMessage;
//import org.springframework.web.socket.WebSocketSession;
//import org.springframework.web.socket.handler.TextWebSocketHandler;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class FaceMessagesHandler extends TextWebSocketHandler {
//
////erb soket e pakvum
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        System.out.println("pakvac sessia " + session.getId());
//    }
//
//// erb apahwvvum e miacum
//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        // The WebSocket has been opened
//        // I might save this session object so that I can send messages to it outside of this method
//        // Let's send the first message
//        for (int i = 0; i < 10; i++) {
//            Thread.sleep(1000);
//            session.sendMessage(new TextMessage("Սերվերից ստացվող թիվ" + i));
//        }
//    }
//
//    //    @Override
////    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
////        Request payload = (Request) message.getPayload();
////
////        System.out.println("--------------- " +payload.toString());
////    }
//    static List<WebSocketSession> webSocketSessions = new ArrayList<>();
//
//
//    //erb tvyal e uxarkvum
//    @Override
//    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
//
//        webSocketSessions.add(session);
//        String id = session.getId();
//        System.out.println(id);
//        System.out.println("Message received: " + textMessage.getPayload());
//        JSONObject jsonObject =new JSONObject(textMessage.getPayload());
//        String name = (String)jsonObject.get("name");
//        int age = (int)jsonObject.get("age");
//        System.out.println("Json name: "+ name);
//        System.out.println("Json age: "+ age);
//        int a = 0;
//        try{
//            a =Integer.parseInt(textMessage.getPayload());
//            if (a > 0) {
//                for (WebSocketSession webSocketSession : webSocketSessions) {
//
//                    if (webSocketSession.getId().equals(textMessage.getPayload())) {
//                        webSocketSession.sendMessage(new TextMessage("barev Lilitic"));
//                    }
//                }
//            }
//        }catch (NumberFormatException ex){
//            System.err.println("----");
//        }
//
//
//    }
//}
//
