package com.example.eventmvc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDto1 {
    private String messageText;
    private String messageDate;
    private boolean createrSendStatus;
    private String toUserNickname;
    private String eventCreaterUserNickname;
    private int currentUserId;
    private int participatingUserId;
    private int eventCreaterUserId;

    @Override
    public String toString() {
        return "MessageDto1{" +
                "messageText='" + messageText + '\'' +
                ", messageDate='" + messageDate + '\'' +
                ", createrSendStatus=" + createrSendStatus +
                ", toUserNickname='" + toUserNickname + '\'' +
                ", eventCreaterUserNickname='" + eventCreaterUserNickname + '\'' +
                ", currentUserId=" + currentUserId +
                ", participatingUserId=" + participatingUserId +
                '}';
    }
}
