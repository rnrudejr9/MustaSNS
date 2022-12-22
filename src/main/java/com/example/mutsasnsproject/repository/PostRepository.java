package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post,Long> {
}
