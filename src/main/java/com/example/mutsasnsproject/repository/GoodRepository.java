package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Good;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good,Long> {
    Optional<Good> findByUserAndPost(User user, Post post);
}
