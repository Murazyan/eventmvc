package com.example.eventmvc.socket.socketEndpoints;

import com.example.eventmvc.model.Event;
import com.example.eventmvc.model.Message;
import com.example.eventmvc.model.User;
import com.example.eventmvc.repository.EventRepository;
import com.example.eventmvc.repository.MessageRepository;
import com.example.eventmvc.repository.UserRepository;
import com.example.eventmvc.socket.Info;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Component
public class SaveAndSendMessages extends TextWebSocketHandler {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;


    static Map<Integer, Info> webSocketSessions = new TreeMap<>();

    //Երբ WebSocketSession է փակվում
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        for (Map.Entry<Integer, Info> entry : webSocketSessions.entrySet()) {
            if (entry.getValue().getWebSocketSession().getId().equals(session.getId())) {
                System.out.println("Փակվեց " + userRepository.findAllById(entry.getKey()).getUsername() + "-էլ հասցեով օգտատերի սեսիան ");
                webSocketSessions.remove(entry.getKey());

            }
        }
    }

    //erb tvyal e uxarkvum
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        JSONObject jsonObject = new JSONObject(textMessage.getPayload());
        JSONObject responseJsonObject = new JSONObject();
        try {
            int currentUserId = Integer.parseInt((String) jsonObject.get("currentUserId"));
            int participantUserId = Integer.parseInt((String) jsonObject.get("participantUserId"));
            int eventCreaterUserId = Integer.parseInt((String) jsonObject.get("eventCreaterUserId"));
            webSocketSessions.put(currentUserId, Info.builder()
                    .participantUserId(participantUserId)
                    .WebSocketSession(session)
                    .eventCreaterUserId(eventCreaterUserId)
                    .build());
        } catch (JSONException e) {
        }
        try {
            String messageText = (String) jsonObject.get("messageText");
            int currentEventId = Integer.parseInt((String) jsonObject.get("currentEventId"));
            int currentUserId = Integer.parseInt((String) jsonObject.get("currentUserId"));
            int participantUserId = Integer.parseInt((String) jsonObject.get("participantUserId"));
            Event event = eventRepository.findAllById(currentEventId);
            User currentUser = userRepository.findAllById(currentUserId);
            User participantUser = userRepository.findAllById(participantUserId);
            if (currentUser.getId() == participantUser.getId()) { // սեսայով ուղարկելու ենք իրադարձության ստեղծողին
                messageRepository.save(Message.builder()
                        .craterSendStatus(false)
                        .event(event)
                        .readingStatus(false)
                        .text(messageText)
                        .toUser(currentUser)
                        .build());
                int id = event.getCreaterUser().getId();

                responseJsonObject.put("currentUserId", currentUserId);
                responseJsonObject.put("messageToUserId", currentUser.getId());
                responseJsonObject.put("messageEventCreaterUserId", event.getCreaterUser().getId());
                responseJsonObject.put("createrSendStatus", false);
                responseJsonObject.put("messageToUserNickname", currentUser.getNickname());
                responseJsonObject.put("messageEventCreaterUserNickname", event.getCreaterUser().getNickname());
                responseJsonObject.put("messageText", messageText);


                System.out.println(" namakay uxarkvum e " + currentUser.getNickname() + "i koxmic " + event.getCreaterUser().getNickname() + "in");
                String res = messageText + "_:_" + currentUserId + "_:_" + event.getCreaterUser().getId() + "_:_" + 0;
                webSocketSessions.get(id).getWebSocketSession().sendMessage(new TextMessage(res));
            }
            if (currentUserId == event.getCreaterUser().getId()) {

                messageRepository.save(Message.builder()
                        .craterSendStatus(true)
                        .event(event)
                        .readingStatus(false)
                        .text(messageText)
                        .toUser(participantUser)
                        .build());
                Response response = new Response();
                response.setCreaterSendStatus(true);
                response.setCurrentUserId(currentUserId);
                response.setMessageEventCreaterUserId(event.getCreaterUser().getId());
                response.setMessageToUserId(participantUser.getId());
                response.setMessageText(messageText);
                response.setMessageEventCreaterUserNickname(event.getCreaterUser().getNickname());
                response.setMessageToUserNickname(participantUser.getNickname());
                System.out.println(response.toString());
            }
            System.out.println("---- messageText - " + messageText);


//            String responseJson = "{"+"messageText"+":"+messageText+","+""

//            webSocketSessions.get(participantUserId).getWebSocketSession().sendMessage();


        } catch (JSONException e) {

        }


    }
}

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
class Response {
    private int currentUserId;
    private int messageToUserId;
    private int messageEventCreaterUserId;
    private boolean createrSendStatus;
    private String messageToUserNickname;
    private String messageEventCreaterUserNickname;
    private String messageText;

    @Override
    public String toString() {
        char ch = '"';
        return "{" + "\n" + ch +
                "currentUserId" + ch + " : " + ch + currentUserId + ch +
                "," + "\n" + ch + "messageToUserId" + ch + " : " + ch + messageToUserId + ch +
                "," + "\n" + ch + "messageEventCreaterUserId" + ch + " : " + ch + messageEventCreaterUserId + ch +
                "," + "\n" + ch + "createrSendStatus" + ch + " : " + ch + createrSendStatus + ch +
                "," + "\n" + ch + "messageToUserNickname" + ch + " : " + ch + messageToUserNickname + ch +
                "," + "\n" + ch + "messageEventCreaterUserNickname" + ch + " : " + ch + messageEventCreaterUserNickname + ch +
                "," + "\n" + ch + "messageText" + ch + " : " + ch + messageText + ch + "\n" +
                '}';
    }
}





