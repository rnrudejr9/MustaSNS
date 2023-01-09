package com.example.mutsasnsproject.infra;

import com.example.mutsasnsproject.repository.AlarmRepository;
import com.example.mutsasnsproject.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@RequiredArgsConstructor
@Component
@Slf4j
public class NotificationInterceptor implements HandlerInterceptor {
    private final AlarmRepository alarmRepository;
    private final UserService userService;
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (modelAndView != null && !isRedirectView(modelAndView) && authentication != null) {
            String userName = authentication.getName();
            long count = alarmRepository.countByTargetUserIdAndReadCheck(userService.loadUserByUsername(userName).getId(),false);
            log.info(userName);
            log.info(userService.loadUserByUsername(userName).getId() + " ");
            log.info(String.valueOf(count));
            modelAndView.addObject("hasNotification", count > 0);
        }
    }

    private boolean isRedirectView(ModelAndView modelAndView) {
        Optional<ModelAndView> optionalModelAndView = Optional.ofNullable(modelAndView);
        return startsWithRedirect(optionalModelAndView) || isTypeOfRedirectView(optionalModelAndView);
    }

    private Boolean startsWithRedirect(Optional<ModelAndView> optionalModelAndView) {
        return optionalModelAndView.map(ModelAndView::getViewName)
                .map(viewName -> viewName.startsWith("redirect:"))
                .orElse(false);
    }

    private Boolean isTypeOfRedirectView(Optional<ModelAndView> optionalModelAndView) {
        return optionalModelAndView.map(ModelAndView::getView)
                .map(v -> v instanceof RedirectView)
                .orElse(false);
    }
}
