package com.example.mutsasnsproject.domain.dto.alarm;

import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.role.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class AlarmResponse {
    private Long id;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
    private Long fromUserId;
    // 누가 좋아요했는지
    private Long targetId;
    // 어떤글에 달렸는지
    private String text;
    private LocalDateTime createdAt;

    public static Page<AlarmResponse> toDtoList(Page<Alarm> alarms){
        Page<AlarmResponse> alarmResponsePage = alarms.map(m -> AlarmResponse.builder()
                .alarmType(m.getAlarmType())
                .id(m.getId())
                .createdAt(m.getCreatedAt())
                .text(m.getText())
                .fromUserId(m.getFromUserId())
                .targetId(m.getTargetId())
                .build());
        return alarmResponsePage;
    }
}

