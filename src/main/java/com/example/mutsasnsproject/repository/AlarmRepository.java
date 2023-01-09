package com.example.mutsasnsproject.repository;

import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.domain.role.AlarmType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    Page<Alarm> findByUser(Pageable pageable, User user);
    long countAlarmByUserAndReadCheck(User user,boolean read);
    Page<Alarm> findByUserAndReadCheck(User user, boolean read , Pageable pageable);

    Alarm findByAlarmTypeAndUserAndTargetId(AlarmType alarmType,User user, Long targetId);
}
