package com.example.mutsasnsproject.service.chat;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.chat.ChatMessageDTO;
import com.example.mutsasnsproject.domain.dto.chat.ChatRoomDTO;
import com.example.mutsasnsproject.domain.entity.chat.Chat;
import com.example.mutsasnsproject.domain.entity.chat.ChatRoom;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.chat.ChatRepository;
import com.example.mutsasnsproject.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatRepository chatRepository;
    private final ChatRoomRepository chatRoomRepository;
    @Transactional
    public Response addChat(ChatMessageDTO chatMessageDTO){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(chatMessageDTO.getRoomId()).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,""));
        Chat chater = chatRepository.save(chatMessageDTO.toChat(chatRoom));
        return Response.success(chater);
    }

    public List<Chat> listChat(Long id){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,""));
        List<Chat> list = chatRepository.findByChatRoom(chatRoom);
        return list;
    }
}
