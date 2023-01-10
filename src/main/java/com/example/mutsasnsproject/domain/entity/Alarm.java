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
import java.time.format.DateTimeFormatter;

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

    // 게시글 주인장 아이디 찾을때
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Long targetUserId;

    private Long fromUserId;
    private String fromUserName;
    private Long targetId;
    @Enumerated(EnumType.STRING)
    private AlarmType alarmType;
// like인지 comment 인지

// 어떤글에 달렸는지
    private String text;

    private boolean readCheck = false;
// 내용

    public void updateRead(){
        this.readCheck = true;
    }
    public static Alarm makeByType(AlarmType alarmType,User user,Post post){
        String message = "";
        if (alarmType == AlarmType.NEW_COMMENT_ON_POST){
            message = "new comment!";
        }
        else if(alarmType == AlarmType.NEW_LIKE_ON_POST){
            message = "new like!";
        }
        return Alarm.builder()
                .alarmType(alarmType)
                .targetId(post.getId())
                .fromUserId(user.getId())
                .fromUserName(user.getUserName())
                .post(post)
                .user(user)
                .targetUserId(post.getUser().getId())
                .text(message)
                .readCheck(false)
                .build();
    }

    public AlarmResponse toResponse(){
        return AlarmResponse.builder()
                .id(id)
                .readCheck(readCheck)
                .createdAt(getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
                .text(text)
                .fromUserName(fromUserName)
                .alarmType(alarmType)
                .build();
    }
}