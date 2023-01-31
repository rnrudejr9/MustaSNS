package com.example.mutsasnsproject.domain.entity.chat;

import com.example.mutsasnsproject.domain.dto.chat.ChatRoomDTO;
import com.example.mutsasnsproject.domain.entity.Post;
import lombok.*;
import org.springframework.web.socket.WebSocketSession;

import javax.persistence.*;
import java.util.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChatRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roomId;
    private String name;
//    @Builder.Default
//    @OneToMany(mappedBy = "chat_room")
//    private List<Chat> chats = new ArrayList<>();

}
