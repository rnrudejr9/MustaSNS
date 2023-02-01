package com.example.mutsasnsproject.domain.dto.chat;

import com.example.mutsasnsproject.domain.entity.chat.Chat;
import com.example.mutsasnsproject.domain.entity.chat.ChatRoom;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ChatMessageDTO {

    private Long roomId;
    private String writer;
    private String message;

    private LocalDateTime createdAt;
    public Chat toChat(ChatRoom chatRoom){
        return Chat.builder().message(message)
                .writer(writer)
                .chatRoom(chatRoom)
                .isChecked(false)
                .build();
    }
}