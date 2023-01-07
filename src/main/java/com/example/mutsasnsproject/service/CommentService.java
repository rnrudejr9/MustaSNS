package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.comment.CommentListResponse;
import com.example.mutsasnsproject.domain.dto.comment.CommentRequest;
import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.AlarmType;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.AlarmRepository;
import com.example.mutsasnsproject.repository.CommentRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final AlarmRepository alarmRepository;

    private final InValidChecker inValidChecker;

//    댓글 CRUD -----------------------------------------------
//            comment_add 등록;
//            comment_modify 수정;
//            comment_delete 삭제;
//            comment_list 전체조회;

    public CommentResponse commentAdd(String userName, Long postId, CommentRequest commentRequest) {
        User user = inValidChecker.userCheck(userName);
        Post post = inValidChecker.postCheckById(postId);

        Comment savedcomment = commentRequest.toEntity();
        savedcomment.setPost(post);
        savedcomment.setUser(user);
        commentRepository.save(savedcomment);

        // #4 알람 추가
        Alarm alarm = Alarm.makeByType(AlarmType.NEW_COMMENT_ON_POST,user,postId);
        alarmRepository.save(alarm);

        CommentResponse commentResponse = savedcomment.toResponse();
        return commentResponse;
    }

    public Page<CommentListResponse> commentList(Long postId, Pageable pageable){
        Page<Comment> page = commentRepository.findByPostId(postId,pageable);
        Page<CommentListResponse> commentListResponse = CommentListResponse.toDtoList(page);
        return commentListResponse;
    }

    @Transactional
    public CommentResponse commentModify(String userName, Long postId, CommentRequest commentRequest, Long commentId){
        User user = inValidChecker.userCheck(userName);
        Post post = inValidChecker.postCheckById(postId);
        Comment comment = inValidChecker.commentCheckById(commentId);
        inValidChecker.isInValidPermission(comment,user);

        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        comment.update(commentRequest.toEntity());
        return comment.toResponse();
    }

    public CommentResponse commentDelete(String userName, Long postId, Long commentId){
        User user = inValidChecker.userCheck(userName);
        Post post = inValidChecker.postCheckById(postId);
        Comment comment = inValidChecker.commentCheckById(commentId);
        inValidChecker.isInValidPermission(comment,user);
        commentRepository.delete(comment);
        return comment.toResponse();
    }


    public CommentResponse findById(Long id){
        Comment comment = inValidChecker.commentCheckById(id);
        return comment.toResponse();
    }
}
