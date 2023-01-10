package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.*;
import com.example.mutsasnsproject.domain.role.AlarmType;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.bytebuddy.asm.Advice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final InValidChecker inValidChecker;

//    게시글 CRUD -----------------------------------------------
//            add 등록;
//            modify 수정;
//            delete 삭제;
//            get 상세조회;
//            list 전체조회;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,"이름없음"));
    }



    public PostResponse add(String userName, PostRequest postRequest){
        // #1 토큰으로 로그인한 아이디 비교
        User user = inValidChecker.userCheck(userName);
        Post post = postRequest.toEntity();
        post.setUser(user);

        postRepository.save(post);
        return post.toResponse("게시글 작성완료");
    }

    @Transactional
    public PostResponse modify(String userName, Long postId, PostRequest postRequest){
        // #1 토큰으로 로그인한 아이디 없을 경우
        User user = inValidChecker.userCheck(userName);
        // #2 수정할 포스트가 없을 경우
        Post post = inValidChecker.postCheckById(postId);
        // #3 사용자와 수정할 포스트의 작성자가 다를 경우 + 계정이 ADMIN 이 아닐 경우
        inValidChecker.isInvalidPermission(post,user);
        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        post.update(postRequest.toEntity());
        return post.toResponse("게시글 수정완료");
    }

    @Transactional
    public PostResponse delete(String userName, Long postId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = inValidChecker.userCheck(userName);
        // #2 삭제할 포스트가 없을 경우
        Post post = inValidChecker.postCheckById(postId);
        // #3 사용자와 삭제할 포스트의 작성자가 다를 경우
        inValidChecker.isInvalidPermission(post,user);
        postRepository.delete(post);
        return post.toResponse("게시글 삭제완료");
    }

    public PostDetailResponse get(String userName, Long postId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = inValidChecker.userCheck(userName);
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = inValidChecker.postCheckById(postId);
        return post.toDetailResponse();
    }
    public PostDetailResponse get(Long postId){
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = inValidChecker.postCheckById(postId);
        return post.toDetailResponse();
    }


    public Page<PostDetailResponse> list(Pageable pageable){
        Page<Post> page = postRepository.findAll(pageable);
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }

//    UI 기능 -------------------------------------------------------

    public Page<PostDetailResponse> findList(Pageable pageable, String body,String title) {
        Page<Post> page = postRepository.findByTitleContainingOrBodyContaining(pageable,body,title);
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }



//    마이 피드 기능 --------------------------------------------------
    public Page<PostDetailResponse> myPages(String userName, Pageable pageable){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = inValidChecker.userCheck(userName);
        Page<Post> page = postRepository.findByUserId(pageable,user.getId());
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }



}
