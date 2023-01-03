package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
    Page<Post> findByTitleContainingOrBodyContaining(Pageable pageable, String body, String title);
}
