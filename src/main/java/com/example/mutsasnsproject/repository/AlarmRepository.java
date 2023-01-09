package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.Post;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.AlarmType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    Page<Alarm> findByUser(Pageable pageable, User user);
    long countByTargetUserIdAndReadCheck(Long id, boolean read);
    Page<Alarm> findByTargetUserIdAndReadCheck(Long id, boolean read,Pageable pageable);

    Alarm findByAlarmTypeAndUserAndPost(AlarmType alarmType,User user,Post post);
}
