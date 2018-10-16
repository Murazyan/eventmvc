package com.example.eventmvc.socket;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.socket.WebSocketSession;

import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Info {
    private WebSocketSession WebSocketSession;
    private int participantUserId;
    private int eventCreaterUserId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Info info = (Info) o;
        return participantUserId == info.participantUserId &&
                eventCreaterUserId == info.eventCreaterUserId;
    }

    @Override
    public int hashCode() {

        return Objects.hash(participantUserId, eventCreaterUserId);
    }
}
