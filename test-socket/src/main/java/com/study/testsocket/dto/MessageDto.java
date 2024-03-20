package com.study.testsocket.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    private String nickname;
    private String content;
    private String message;

    public MessageDto(String nickname, String content) {
        this.nickname = nickname;
        this.content = content;
    }
}
