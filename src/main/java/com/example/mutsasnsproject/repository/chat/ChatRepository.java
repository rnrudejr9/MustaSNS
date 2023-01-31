package com.example.mutsasnsproject.repository.chat;

import com.example.mutsasnsproject.domain.entity.chat.Chat;
import com.example.mutsasnsproject.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRepository extends JpaRepository<Chat,Long> {
    List<Chat> findByChatRoom(ChatRoom chatRoom);
}
