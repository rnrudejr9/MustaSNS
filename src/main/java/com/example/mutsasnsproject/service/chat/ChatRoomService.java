package com.example.mutsasnsproject.service.chat;

import com.example.mutsasnsproject.domain.dto.chat.ChatRoomDTO;
import com.example.mutsasnsproject.domain.entity.chat.ChatRoom;

import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.chat.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final ChatRoomRepository chatRoomRepository;

    public List<ChatRoomDTO> findAllRooms(){
        List<ChatRoom> result = chatRoomRepository.findAll();
        List<ChatRoomDTO> list =  ChatRoomDTO.createList(result);
        return list;
    }

    public ChatRoom findRoomById(Long id){
        ChatRoom chatRoom = chatRoomRepository.findByRoomId(id).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,""));
        return chatRoom;
    }

    public ChatRoom createChatRoomDTO(String name){
        ChatRoom savedRoom = chatRoomRepository.save(ChatRoom.builder().name(name).build());
        return savedRoom;
    }
}
