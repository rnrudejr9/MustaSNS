package com.example.mutsasnsproject.restcontroller.chat;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.chat.ChatMessageDTO;
import com.example.mutsasnsproject.domain.entity.chat.Chat;
import com.example.mutsasnsproject.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

@RequiredArgsConstructor
@RequestMapping("/api/v1/")
@Slf4j
public class ChatRestController {
    private final ChatService chatService;


    @PostMapping("/chat")
    public void addChat(@RequestBody ChatMessageDTO chatMessageDTO){
        log.info(chatMessageDTO.getMessage()+" "+chatMessageDTO.getWriter());
        chatService.addChat(chatMessageDTO);
    }

    @GetMapping("/chat/{roomId}")
    public Response listChat(@PathVariable Long roomId){
        log.info("roomId : " + roomId);
        List<Chat> list = chatService.listChat(roomId);
        return Response.success(list);
    }
}
