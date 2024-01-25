package com.study.testsocket;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MessageService {

    private final Map<String, ChatRoom> users = new HashMap<>();

    // 채팅방 생성시  임시로 데이터 값 넣어놓기
    public MessageService(){
        ChatRoom chatRoom = new ChatRoom("1",List.of("lee","hee","kim"));
        users.put("1",chatRoom);
    }

    public Integer findAllUser(String roomNo){
        Integer response = users.get(roomNo).getUsers().size();
        return response;
    }
}
