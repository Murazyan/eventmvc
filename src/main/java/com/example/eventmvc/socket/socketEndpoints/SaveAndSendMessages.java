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
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

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


    //erb tvyal e uxarkvum frontic
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage textMessage) throws Exception {
        System.out.println("miacav");
        JSONObject jsonObject = new JSONObject(textMessage.getPayload());
        try {
            int CURRENT_USER_ID = Integer.parseInt((String) jsonObject.get("CURRENT_USER_ID"));
            webSocketSessions.put(CURRENT_USER_ID, Info.builder()
                    .WebSocketSession(session)
                    .eventCreaterUserId(0)
                    .participantUserId(0)
                    .build());
            System.out.println("avelacav lriv nor sessia datark arjeqov");
        } catch (JSONException e) {
        }
        try {
            int currentUserId = Integer.parseInt((String) jsonObject.get("currentUserId"));
            int participantUserId = Integer.parseInt((String) jsonObject.get("participantUserId"));
            int eventCreaterUserId = Integer.parseInt((String) jsonObject.get("eventCreaterUserId"));
            Info info = webSocketSessions.get(currentUserId);
            if (info == null) {
                webSocketSessions.put(currentUserId, Info.builder()
                        .participantUserId(participantUserId)
                        .WebSocketSession(session)
                        .eventCreaterUserId(eventCreaterUserId)
                        .build());
            }
            if (info != null /*&& info.getEventCreaterUserId() == 0 && info.getParticipantUserId() == 0*/) {
                System.out.println("katarvec popoxutyun map-i mej` popoxvec arjeqy");
                for (Map.Entry<Integer, Info> entry : webSocketSessions.entrySet()) {
                    if (entry.getKey() == currentUserId) {
                        entry.setValue(Info.builder()
                                .participantUserId(participantUserId)
                                .WebSocketSession(session)
                                .eventCreaterUserId(eventCreaterUserId)
                                .build());
                    }

                }
            }
        } catch (JSONException e) {}
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
                Info info = webSocketSessions.get(id);
                if (info != null && info.getParticipantUserId() ==currentUserId && info.getEventCreaterUserId()==event.getCreaterUser().getId()) {
                    WebSocketSession webSocketSession = info.getWebSocketSession();
                    if (webSocketSession.isOpen()) {
                        String res = messageText + "_:_" + currentUserId + "_:_" + event.getCreaterUser().getId() + "_:_" + 0+"_:_"+event.getId();
                        webSocketSession.sendMessage(new TextMessage(res));
                        List<Message> changingMesages = messageRepository.findAllByToUserAndEventAndReadingStatusOrderByIdDesc(currentUser, event, false);//քանի որ ուղարկվող օգտատերը հենց այս պահին մեջն է readingStatus-ը պետք է true դառնա
                        for (Message changingMesage : changingMesages) {
                            changingMesage.setReadingStatus(true);
                        }
                        messageRepository.saveAll(changingMesages);
                    }
                }
                if(info != null && ((info.getEventCreaterUserId()==0 && info.getParticipantUserId()==0) ||(info.getParticipantUserId() !=currentUserId && info.getEventCreaterUserId()!=event.getCreaterUser().getId()))){
                    WebSocketSession webSocketSession = info.getWebSocketSession();
                    System.out.println("uxarkvum e stexcoxin ^_^");
                    if (webSocketSession.isOpen()) {
                        String res = event.getId() + "^_^" + currentUserId;
                        webSocketSession.sendMessage(new TextMessage(res));
                    }
                }
            }
            if (currentUserId == event.getCreaterUser().getId()) { //սեսայով ուղարկելու ենք իրադարձության մասնակցին
                messageRepository.save(Message.builder()
                        .craterSendStatus(true)
                        .event(event)
                        .readingStatus(false)
                        .text(messageText)
                        .toUser(participantUser)
                        .build());
                Info info = webSocketSessions.get(participantUserId);
                if (info != null && info.getParticipantUserId()== participantUserId && info.getEventCreaterUserId()==event.getCreaterUser().getId()) {
                    WebSocketSession webSocketSession = info.getWebSocketSession();
                    if (webSocketSession.isOpen()) {
                        String res = messageText + "_:_" + participantUserId + "_:_" + event.getCreaterUser().getId() + "_:_" + 1+"_:_"+event.getId();
                        webSocketSession.sendMessage(new TextMessage(res));
                        List<Message> changingMesages = messageRepository.findAllByToUserAndEventAndReadingStatusOrderByIdDesc(participantUser, event, false);//քանի որ ուղարկվող օգտատերը հենց այս պահին մեջն է readingStatus-ը պետք է true դառնա
                        for (Message changingMesage : changingMesages) {
                            changingMesage.setReadingStatus(true);
                        }
                        messageRepository.saveAll(changingMesages);
                    }
                }
                if(info != null && ((info.getEventCreaterUserId()==0 && info.getParticipantUserId()==0) || (info.getParticipantUserId()!= participantUserId && info.getEventCreaterUserId()!=event.getCreaterUser().getId()))){
                    WebSocketSession webSocketSession = info.getWebSocketSession();
                    System.out.println("uxarkvum e masnakcin ^_^");
                    if (webSocketSession.isOpen()) {
                        String res = event.getId() + "^_^" + participantUserId;
                        webSocketSession.sendMessage(new TextMessage(res));
                    }
                }
            }
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





