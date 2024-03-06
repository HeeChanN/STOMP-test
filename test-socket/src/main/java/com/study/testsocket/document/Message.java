package com.study.testsocket.document;

import com.study.testsocket.dto.MessageDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collation = "message")
@NoArgsConstructor
@Getter
public class Message {
    @Id
    private String id;

    private String nickname;
    private String content;
    private String roomId;

    public Message(MessageDto messageDto) {
        this.nickname = messageDto.getNickname();
        this.content = messageDto.getContent();
        this.roomId = messageDto.getRoomId();
    }
}
