package com.example.mutsasnsproject.service;

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
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final GoodRepository goodRepository;
    private final AlarmRepository alarmRepository;

//    게시글 CRUD -----------------------------------------------
//            add 등록;
//            modify 수정;
//            delete 삭제;
//            get 상세조회;
//            list 전체조회;

    public Post findById(Long id){
        return postRepository.findById(id).orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,"이름없음"));
    }
    public PostResponse add(String userName,String body, String title){
        log.info("포스트 작성 서비스");
        // #1 토큰으로 로그인한 아이디 비교
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));;
        Post savedPost = Post.builder()
                .body(body)
                .title(title)
                .user(user)
                .build();
        postRepository.save(savedPost);
        PostResponse postResponse = PostResponse.builder()
                .postId(savedPost.getId())
                .message("게시글 작성완료")
                .build();
        return postResponse;
    }

    public PostResponse addRe(PostRequest postRequest, String userName){
        // #1 토큰으로 로그인한 아이디 비교
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        Post savedPost = postRequest.toEntity();
        postRepository.save(savedPost);
        return new PostResponse("게시글 작성완료",savedPost.getId());
    }

    @Transactional
    public PostResponse modify(String userName, Long postId, PostRequest postRequest){
        // #1 토큰으로 로그인한 아이디 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 수정할 포스트가 없을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지 않습니다!"));
        // #3 사용자와 수정할 포스트의 작성자가 다를 경우 + 계정이 ADMIN 이 아닐 경우
        if(!Objects.equals(post.getUser().getUserName(),user.getUserName()) && !user.getRole().equals(UserRole.ADMIN)) {
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
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 삭제할 포스트가 없을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지 않습니다!"));
        // #3 사용자와 삭제할 포스트의 작성자가 다를 경우
        if(!Objects.equals(post.getUser().getUserName(),user.getUserName()) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 삭제할 수 없는 아이디입니다.");
        }

        postRepository.delete(post);
        PostResponse postResponse = PostResponse.builder()
                .postId(postId)
                .message("게시글 삭제완료").build();
        return postResponse;
    }

    public PostDetailResponse get(String userName, Long postId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지 않습니다!"));

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


    public Page<PostDetailResponse> list(Pageable pageable){
        Page<Post> page = postRepository.findAll(pageable);
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }

    public Page<PostDetailResponse> findList(Pageable pageable, String body,String title){
        Page<Post> page = postRepository.findByTitleContainingOrBodyContaining(pageable,body,title);
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }


//    댓글 CRUD -----------------------------------------------
//            comment_add 등록;
//            comment_modify 수정;
//            comment_delete 삭제;
//            comment_list 전체조회;

    public CommentResponse commentAdd(String userName, Long postId, String comment) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));

        Comment savedcomment = Comment
                .builder()
                .comment(comment)
                .post(post)
                .user(user)
                .build();
        commentRepository.save(savedcomment);

        // #4 알람 추가
        Alarm alarm = Alarm.builder()
                .alarmType(AlarmType.NEW_COMMENT_ON_POST)
                .fromUserId(user.getId())
                .targetId(postId)
                .text("new comment!")
                .user(user)
                .build();
        alarmRepository.save(alarm);

        CommentResponse commentResponse = CommentResponse.builder()
                .id(savedcomment.getId())
                .comment(comment)
                .postId(postId)
                .userName(userName)
                .createdAt(savedcomment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .build();
        return commentResponse;
    }

    public CommentListResponse commentList(Long postId,Pageable pageable){
        Page<Comment> page = commentRepository.findByPostId(postId,pageable);
        List<CommentResponse> list = new ArrayList<>();
        for(Comment comment : page){
            CommentResponse commentResponse = CommentResponse.builder()
                    .id(comment.getId())
                    .comment(comment.getComment())
                    .postId(postId)
                    .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                    .userName(comment.getUser().getUserName())
                    .lastModifiedAt(comment.getLastModifiedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                    .build();
            list.add(commentResponse);
        }

        CommentListResponse commentListResponse = CommentListResponse.builder()
                .content(list)
                .pageable(pageable)
                .build();
        return commentListResponse;
    }

    @Transactional
    public CommentResponse commentModify(String userName, Long postId,CommentRequest commentRequest,Long commentId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));

        // #3 수정할 댓글의 작성자가 본인인지?
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.DATABASE_ERROR,"댓글이 존재하지 않습니다."));

        if(!Objects.equals(comment.getUser().getUserName(),userName) && !user.getRole().equals(UserRole.ADMIN)) {
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }

        //JPA 의 영속성 컨텍스트 덕분에 entity 객체의 값만 변경하면 자동으로 변경사항 반영함!
        //따라서 repository.update 를 쓰지 않아도 됨.
        comment.update(commentRequest.toEntity());
        CommentResponse commentResponse = CommentResponse.builder()
                .createdAt(comment.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .lastModifiedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-mm-dd hh:mm:ss")))
                .id(comment.getId())
                .userName(comment.getUser().getUserName())
                .comment(comment.getComment())
                .postId(comment.getPost().getId())
                .build();
        return commentResponse;
    }

    public CommentResponse commentDelete(String userName, Long postId, Long commentId){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));

        // #3 삭제할 댓글의 작성자가 본인인지?
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new AppException(ErrorCode.DATABASE_ERROR,"댓글이 존재하지 않습니다."));

        if(!Objects.equals(comment.getUser().getUserName(),userName) && !user.getRole().equals(UserRole.ADMIN)){
            throw new AppException(ErrorCode.INVALID_PERMISSION, "작성자 불일치로 수정할 수 없는 아이디입니다");
        }
        commentRepository.delete(comment);
        return CommentResponse.builder().comment(post.getUser().getUserName() + " 가 작성한 " + commentId + " 의 " + comment.getComment()  +" 내용이 삭제됨").build();
    }

//      좋아요 기능 -----------------------------------------------
//            postGood 좋아요 실행/취소;
//            countGood 좋아요 개수리턴;


    public String postGood(Long postId,String userName) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));

        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));

        Optional<Good> good = goodRepository.findByUserAndPost(user,post);
        // #3 이미 좋아요를 누른 상황
        if(good.isPresent()){
            goodRepository.delete(good.get());
            return "게시글 좋아요 취소했습니다";
        }
        Good savedGood = Good.builder().post(post).user(user).build();
        goodRepository.save(savedGood);

        // #4 알람 추가
        Alarm alarm = Alarm.builder()
                .alarmType(AlarmType.NEW_LIKE_ON_POST)
                .fromUserId(user.getId())
                .targetId(postId)
                .text("new like!")
                .user(user)
                .build();
        alarmRepository.save(alarm);

        return "게시글 좋아요 했습니다";
    }
    public Integer countGood(Long postId){
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = postRepository.findById(postId).orElseThrow(()->new AppException(ErrorCode.POST_NOT_FOUND,"게시글이 존재하지않습니다."));
        return post.getGoods().size();
    }

//    마이 피드 기능 --------------------------------------------------
    public Page<PostDetailResponse> myPages(String userName, Pageable pageable){
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = userRepository.findByUserName(userName)
                .orElseThrow(()->new AppException(ErrorCode.USERNAME_NOT_FOUND,userName +" 이 존재하지 않습니다."));

        Page<Post> page = postRepository.findByUserId(pageable,user.getId());
        Page<PostDetailResponse> postDetailResponsePage = PostDetailResponse.toDtoList(page);
        return postDetailResponsePage;
    }



}
