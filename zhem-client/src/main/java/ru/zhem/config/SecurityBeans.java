package ru.zhem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.zhem.client.RoleRestClient;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.service.AdminUserDetailsService;
import ru.zhem.service.ZhemUserDetailsService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityBeans {

    @RequiredArgsConstructor
    @Configuration
    @Order(1)
    public static class AdminConfigurationAdapter {

        private final ZhemUserRestClient zhemUserRestClient;

        @Bean
        public UserDetailsService adminUserDetailsService() {
            return new AdminUserDetailsService(zhemUserRestClient);
        }

        @Bean
        public AuthenticationEntryPoint adminAuthenticationEntryPoint() {
            return new LoginUrlAuthenticationEntryPoint("/admin/login");
        }

        @Bean
        public SecurityFilterChain adminSecurityFilterChain(HttpSecurity http,
                                                            HandlerMappingIntrospector introspector) throws Exception {
            MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
            http.securityMatcher("/admin/**")
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(mvcMatcherBuilder.pattern("/admin/**"))
                            .hasRole("ADMIN"))
                    .userDetailsService(this.adminUserDetailsService())
                    .formLogin(form -> form
                            .loginPage("/admin/login").permitAll()
                            .loginProcessingUrl("/admin/login")
                            .failureUrl("/admin/login?error=loginError")
                            .defaultSuccessUrl("/admin/dashboard", true))
                    .logout(logout -> logout
                            .logoutUrl("/admin/logout")
                            .logoutSuccessUrl("/admin/login")
                            .deleteCookies("JSESSIONID"));

            return http.build();
        }

    }

    @RequiredArgsConstructor
    @Configuration
    @Order(2)
    public static class ClientConfigurationAdapter {

        private final ZhemUserRestClient zhemUserRestClient;
        private final RoleRestClient roleRestClient;

        @Bean
        public UserDetailsService clientUserDetailsService() {
            return new ZhemUserDetailsService(zhemUserRestClient, roleRestClient);
        }

        @Bean
        public AuthenticationEntryPoint clientAuthenticationEntryPoint() {
            return new LoginUrlAuthenticationEntryPoint("/user/login");
        }

        @Bean
        public SecurityFilterChain clientSecurityFilterChain(HttpSecurity http,
                                                             HandlerMappingIntrospector introspector) throws Exception {
            MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
            http.securityMatcher("/user/**")
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(mvcMatcherBuilder.pattern("/user/registration"))
                            .anonymous()
                            .requestMatchers(mvcMatcherBuilder.pattern("/user/**"))
                            .hasRole("CLIENT"))
                    .userDetailsService(this.clientUserDetailsService())
                    .formLogin(form -> form
                            .loginPage("/user/login").permitAll()
                            .loginProcessingUrl("/user/login")
                            .failureUrl("/user/login?error=loginError")
                            .defaultSuccessUrl("/", true))
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .logoutSuccessUrl("/user/login")
                            .deleteCookies("JSESSIONID"));

            return http.build();
        }

    }

    @Bean
    public static PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

