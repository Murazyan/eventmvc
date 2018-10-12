package com.example.eventmvc.dto;


import com.example.eventmvc.model.Message;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class MessageDto {
    private Message message;
    private int notReadingCount;
}
