package com.example.mutsasnsproject.customutils;

import com.example.mutsasnsproject.domain.dto.comment.CommentResponse;
import com.example.mutsasnsproject.domain.entity.Comment;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.UserRole;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.CommentRepository;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class InValidChecker {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final BCryptPasswordEncoder encoder;
    // userCheck : userName이 repo에 존재하는가?
    public User userCheck(String userName){
        return userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
    }

    public void passwordCheck(String first, String second){
        if(!encoder.matches(first,second)){
            throw new AppException(ErrorCode.INVALID_PASSWORD,"패스워드가 일치하지 않습니다.");
        }
    }

    public void isDuplicatedUserName(String userName){
        userRepository.findByUserName(userName)
                .ifPresent(user -> {
                    throw new AppException(ErrorCode.USERNAME_DUPLICATED,userName + " 이 중복됩니다.");
                });
    }

    public User userCheckById(Long id){
        return userRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,"해당 USER 가 존재하지않습니다."));
    }

    // postid가 repo에 존재하는가?
    public Post postCheckById(Long postId){
        return postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지 않습니다!"));
    }

    //메소드 오버로딩
    public void isInvalidPermission(Post post, User user){
        if(!Objects.equals(post.getUser().getUserName(),user.getUserName()) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }
    }
    public void isInValidPermission(Comment comment,User user){
        if(!Objects.equals(comment.getUser().getUserName(),user.getUserName()) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }
    }

    public Comment commentCheckById(Long commentId){
        return commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.COMMENT_NOT_FOUND,"댓글이 존재하지 않습니다."));
    }

}
