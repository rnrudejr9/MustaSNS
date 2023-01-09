package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.Good;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.AlarmType;
import com.example.mutsasnsproject.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class GoodService {
    //      좋아요 기능 -----------------------------------------------
//            postGood 좋아요 실행/취소;
//            countGood 좋아요 개수리턴;
    private final AlarmRepository alarmRepository;
    private final GoodRepository goodRepository;

    private final InValidChecker inValidChecker;

    public String postGood(Long postId,String userName) {
        // #1 토큰으로 로그인한 아이디가 없을 경우
        User user = inValidChecker.userCheck(userName);

        // #2 해당 게시글이 존재하지 않을 경우
        Post post = inValidChecker.postCheckById(postId);

        Optional<Good> good = goodRepository.findByUserAndPost(user,post);
        // #3 이미 좋아요를 누른 상황
        if(good.isPresent()){
            Alarm alarm = alarmRepository.findByAlarmTypeAndUserAndPost(AlarmType.NEW_LIKE_ON_POST,user,post);
            alarmRepository.delete(alarm);
            goodRepository.delete(good.get());
            return "게시글 좋아요 취소했습니다";
        }
        Good savedGood = Good.builder().post(post).user(user).build();
        goodRepository.save(savedGood);

        // #4 알람 추가
        Alarm alarm = Alarm.makeByType(AlarmType.NEW_LIKE_ON_POST,user,post);
        alarmRepository.save(alarm);

        return "게시글 좋아요 했습니다";
    }
    public Integer countGood(Long postId){
        // #2 해당 게시글이 존재하지 않을 경우
        Post post = inValidChecker.postCheckById(postId);
        return post.getGoods().size();
    }

    public boolean isContainGood(User user, Post post){
        Optional<Good> good = goodRepository.findByUserAndPost(user,post);
        // #3 이미 좋아요를 누른 상황
        if(good.isPresent()){
            return true;
        }
        return false;
    }
}
