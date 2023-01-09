package com.example.mutsasnsproject.service;

import com.example.mutsasnsproject.customutils.InValidChecker;
import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.domain.entity.User;
import com.example.mutsasnsproject.exception.AppException;
import com.example.mutsasnsproject.exception.ErrorCode;
import com.example.mutsasnsproject.repository.AlarmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AlarmService {
    private final AlarmRepository alarmRepository;
    private final InValidChecker inValidChecker;
    public Page<AlarmResponse> getAlarmList(String userName, Pageable pageable){
        User user = inValidChecker.userCheck(userName);
        Page<Alarm> page =alarmRepository.findByUserAndReadCheck(user,false,pageable);
        return AlarmResponse.toDtoList(page);
    }

    public Page<AlarmResponse> getReadAlarm(String userName, Pageable pageable){
        User user = inValidChecker.userCheck(userName);
        Page<Alarm> page = alarmRepository.findByUserAndReadCheck(user,true,pageable);
        return AlarmResponse.toDtoList(page);
    }

    public void modifyAlarm(Page<Alarm> page){
        for(Alarm p : page){
            p.updateRead();
        }
    }

    @Transactional
    public AlarmResponse readAlarm(String userName, Long alarmId){
        User user = inValidChecker.userCheck(userName);
        //어차피 읽는건 자신의 이름과 연관된 게시글 밖에 안됨

        Alarm alarm = alarmRepository.findById(alarmId).orElseThrow(() -> new AppException(ErrorCode.POST_NOT_FOUND,"알람존재하지않음"));
        alarm.updateRead();
        return alarm.toResponse();
    }
}
