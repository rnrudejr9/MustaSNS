package com.example.mutsasnsproject.domain.entity;

import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

public class Good {
    private Long id;
    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;
}
