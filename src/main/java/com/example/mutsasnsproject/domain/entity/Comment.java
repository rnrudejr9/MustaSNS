package com.example.mutsasnsproject.domain.entity;

import javax.persistence.*;
import java.time.LocalDateTime;
//
//@Entity
public class Comment extends BaseEntity{
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String comment;


}
