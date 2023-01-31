package com.example.mutsasnsproject.repository.chat;

import com.example.mutsasnsproject.domain.entity.chat.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom,Long> {
    Optional<ChatRoom> findByRoomId(Long id);
}
