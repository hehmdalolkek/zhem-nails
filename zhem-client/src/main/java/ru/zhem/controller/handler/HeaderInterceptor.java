package ru.zhem.controller.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import ru.zhem.dto.request.ZhemUserDto;
import ru.zhem.service.ZhemUserService;

import java.security.Principal;

@RequiredArgsConstructor
@Component
public class HeaderInterceptor implements HandlerInterceptor {

    private final ZhemUserService zhemUserService;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {
        if (modelAndView != null && !modelAndView.getViewName().startsWith("redirect:")) {
            Principal principal = request.getUserPrincipal();
            if (principal != null) {
                ZhemUserDto user = zhemUserService.findUserByPhone(principal.getName());
                modelAndView.addObject("authUser", user);
            }
        }
    }
}
