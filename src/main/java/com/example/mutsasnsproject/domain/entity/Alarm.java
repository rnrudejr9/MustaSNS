package com.example.mutsasnsproject.domain.entity;


import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.role.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder
public class Alarm extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 알람을 받은 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
// like인지 comment 인지

    private Long fromUserId;
// 누가 좋아요했는지
    private Long targetId;
// 어떤글에 달렸는지
    private String text;
// 내용

    public static Alarm makeByType(AlarmType alarmType,User user,Long postId){
        String message = "";
        if (alarmType == AlarmType.NEW_COMMENT_ON_POST){
            message = "new comment!";
        }
        else if(alarmType == AlarmType.NEW_LIKE_ON_POST){
            message = "new like!";
        }
        return Alarm.builder()
                .user(user)
                .alarmType(alarmType)
                .fromUserId(user.getId())
                .targetId(postId)
                .text(message)
                .build();
    }
}