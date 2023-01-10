package com.example.mutsasnsproject.controller;

import com.example.mutsasnsproject.domain.dto.alarm.AlarmResponse;
import com.example.mutsasnsproject.domain.entity.Alarm;
import com.example.mutsasnsproject.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/view/v1/")
public class AlarmController {
    private final AlarmService alarmService;
    @GetMapping("/alarms")
    public String getAlarm(Model model, Authentication authentication, @PageableDefault(size = 5, sort = "createdAt",direction = Sort.Direction.DESC) Pageable pageable){
        try {
            String userName = authentication.getName();
            Page<AlarmResponse> page = alarmService.getAlarmList(userName, pageable);
            int nowPage = page.getPageable().getPageNumber() + 1;
            int startPage = Math.max(nowPage - 4, 1);
            int endPage = Math.min(page.getTotalPages(), nowPage + 4);
            model.addAttribute("alarms", page);
            model.addAttribute("startPage", startPage);
            model.addAttribute("endPage", endPage);

            return "users/alarm";
        }catch (Exception e){
            return "error";
        }
    }

    @PostMapping("/alarms/{id}")
    public String readAlarm(@PathVariable Long id, Authentication authentication){
        String userName = authentication.getName();
        alarmService.readAlarm(userName,id);

        return "redirect:/view/v1/alarms";
    }

    @PostMapping("/alarmsto/{id}/{target}")
    public String readAlarms(@PathVariable Long id,@PathVariable Long target, Authentication authentication){
        String userName = authentication.getName();
        alarmService.readAlarm(userName,id);

        return "redirect:/view/v1/posts/detail/" + target;
    }

}
