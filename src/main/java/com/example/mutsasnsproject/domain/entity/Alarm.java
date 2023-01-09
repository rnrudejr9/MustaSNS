package com.example.mutsasnsproject.domain.entity;


import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.entity.BaseEntity;
import com.example.mutsasnsproject.domain.role.AlarmType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Entity
@Builder

@Where(clause = "deleted_at is NULL")
@SQLDelete(sql = "UPDATE alarm SET deleted_at = now()  WHERE id=?")
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

    private boolean readCheck = false;
// 내용

    public void updateRead(){
        this.readCheck = true;
    }
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
                .readCheck(false)
                .build();
    }

    public AlarmResponse toResponse(){
        return AlarmResponse.builder()
                .id(id)
                .readCheck(readCheck)
                .createdAt(getCreatedAt())
                .targetId(targetId)
                .fromUserId(fromUserId)
                .text(text)
                .alarmType(alarmType)
                .build();
    }
}