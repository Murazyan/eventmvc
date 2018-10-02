package com.example.eventmvc.dto;

import com.example.eventmvc.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Result {
    private int userId;
    private String username;
    private String nickname;
    private String picUrl;
    private int isNull;

}