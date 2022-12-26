package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.domain.dto.Response;
import com.example.mutsasnsproject.domain.dto.post.PostDetailResponse;
import com.example.mutsasnsproject.domain.dto.post.PostListResponse;
import com.example.mutsasnsproject.domain.dto.post.PostRequest;
import com.example.mutsasnsproject.domain.dto.post.PostResponse;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.PostRepository;
import com.example.mutsasnsproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    public PostResponse add(String userName,String body, String title){
        log.info("포스트 작성 서비스");
        // 토큰으로 로그인한 아이디 비교
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
        return postResponse;
    }

    @Transactional
    public PostResponse modify(String userName, Long postId, PostRequest postRequest){
        // #1 토큰으로 로그인한 아이디 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));
        // #2 수정할 포스트가 없을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"해당포스트가 없습니다."));
        // #3 사용자와 수정할 포스트의 작성자가 다를 경우
        if(!user.getPost().contains(post)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }

        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        post.update(postRequest.toEntity());
        PostResponse postResponse = PostResponse.builder().postId(postId).message("게시글 수정완료").build();
        return postResponse;
    }

    public PostResponse delete(String userName, Long postId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));
        // #2 삭제할 포스트가 없을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.DATABASE_ERROR,"해당포스트가 없습니다."));
        // #3 사용자와 삭제할 포스트의 작성자가 다를 경우
        if(!user.getPost().contains(post)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 삭제할 수 없는 아이디입니다");
        }

        postRepository.delete(post);
        PostResponse postResponse = PostResponse.builder().message("게시글 삭제완료").build();
        return postResponse;
    }

    public PostDetailResponse get(String userName, Long postId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName+"없습니다.!"));
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));

        PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                .id(postId)
                .title(post.getTitle())
                .body(post.getBody())
                .lastModifiedAt(post.getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .userName(post.getUser().getUserName())
                .build();
        return postDetailResponse;
    }


    public PostListResponse list(Pageable pageable){
        Page<Post> page = postRepository.findAll(pageable);
        List<PostDetailResponse> list = new ArrayList<>();
        for(Post post : page){
            PostDetailResponse postDetailResponse = PostDetailResponse.builder()
                    .id(post.getId())
                    .body(post.getBody())
                    .title(post.getTitle())
                    .userName(post.getUser().getUserName())
                    .lastModifiedAt(post.getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                    .createdAt(post.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                    .build();
            list.add(postDetailResponse);
        }

        PostListResponse postListResponse = PostListResponse.builder()
                .content(list)
                .pageable(pageable)
                .build();
        return postListResponse;
    }
}
