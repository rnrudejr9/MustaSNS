package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.spi.ServiceRegistry;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public Response<?> createPost(String userName,String body, String title){
        //인증으로 받은 userName으로 통한 객체반환
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));;

        Post savedPost = Post.builder()
                .body(body)
                .title(title)
                .user(user)
                .build();
        postRepository.save(savedPost);
        PostResponse postResponse = PostResponse.builder()
                .postId(savedPost.getId())
                .message("포스트 작성완료")
                .build();
        return Response.success(postResponse);
    }

    @Transactional
    public Response<?> modifyPost(String userName, Long postId, PostRequest postRequest){
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"해당포스트가 없습니다."));
        //접속한 아이디에 해당 포스트의 아이디가 포함되어있지않는다면

        if(!user.getPost().contains(post)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }
        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        post.update(postRequest.toEntity());
        PostResponse postResponse = PostResponse.builder().postId(postId).message("게시글 수정완료").build();
        return Response.success(postResponse);
    }

    public Response<?> delete(String userName, Long postId){

        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,"해당포스트가 없습니다."));

        if(!user.getPost().contains(post)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 삭제할 수 없는 아이디입니다");
        }

        postRepository.delete(post);
        PostResponse postResponse = PostResponse.builder().message("게시글 삭제완료").build();
        return Response.success(postResponse);
    }

    public Response<?> detail(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));
        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(postId)
                .title(post.getTitle())
                .body(post.getBody())
                .lastModifiedAt(post.getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .userName(post.getUser().getUserName())
                .build();
        return Response.success(postDetailResponse);
    }
}