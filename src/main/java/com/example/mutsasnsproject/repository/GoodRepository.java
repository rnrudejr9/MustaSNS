package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Good;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.Optional;

public interface GoodRepository extends JpaRepository<Good,Long> {
    Optional<Good> findByUserAndPost(User user, Post post);

    @Transactional
    @Modifying
    void deleteAllByPost(@Param("post") Post post);
}
