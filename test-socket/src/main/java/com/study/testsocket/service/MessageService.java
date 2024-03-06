package com.study.testsocket.service;

import com.study.testsocket.dto.MessageDto;

public interface MessageService {

    void broadCastMessage(MessageDto messageDto);
}
