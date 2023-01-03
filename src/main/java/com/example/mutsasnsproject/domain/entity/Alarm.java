package com.example.mutsasnsproject.domain.entity;

import com.example.mutsasnsproject.domain.entity.BaseEntity;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

//public class Alarm extends BaseEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//
//    // 알람을 받은 사람
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//
//    @Enumerated(EnumType.STRING)
//    private AlarmType alarmType;
// like인지 comment 인지
//
//    private Long fromUserId;
// 누가 좋아요했는지
//    private Long targetId;
// 어떤글에 달렸는지
//    private String text;
// 내용
//}