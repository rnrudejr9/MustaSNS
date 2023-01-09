package com.example.mutsasnsproject.restcontroller;

import com.example.mutsasnsproject.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    /**
     * @title 로그인 한 유저 sse 연결
     */
    @GetMapping(value = "/subscribe/{id}", produces = "text/event-stream")
    public SseEmitter subscribe(@PathVariable String id,
                                @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId) {
        return notificationService.subscribe(id, lastEventId);
    }

    @GetMapping("/sends/{id}")
    public ResponseEntity<?> sendTo(@PathVariable String id,  @RequestHeader(value = "Last-Event-ID", required = false, defaultValue = "") String lastEventId){
        notificationService.send(id,"hello","t","/view/v1/list");
        return ResponseEntity.ok().body("hello");
    }


}