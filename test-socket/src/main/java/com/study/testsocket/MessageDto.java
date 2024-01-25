package com.study.testsocket;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class MessageDto {
    String nickname;
    String content;
    String roomId;

    public MessageDto(String nickname, String content, String roomId) {
        this.nickname = nickname;
        this.content = content;
        this.roomId = roomId;
    }
}
