package ru.zhem.common.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.zhem.service.interfaces.ZhemUserService;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class CheckAdminIsExistsFilter extends OncePerRequestFilter {

    private final ZhemUserService zhemUserService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (new AntPathRequestMatcher("/admin/**").matches(request)) {
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth == null || !auth.isAuthenticated()
                    || !auth.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN"))) {
                boolean isExists = this.zhemUserService.adminIsExists();
                if (!isExists && !request.getRequestURI().equals("/admin/registration")) {
                    response.sendRedirect("/admin/registration");
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
