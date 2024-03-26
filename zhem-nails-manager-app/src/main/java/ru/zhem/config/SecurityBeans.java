package ru.zhem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityBeans {

    @Configuration
    @Order(1)
    public static class AdminConfigurationAdapter {

        @Bean
        public UserDetailsService adminUserDetailsService() {
            UserDetails user = User.builder()
                    .username("admin")
                    .password(bcryptPasswordEncoder().encode("admin"))
                    .roles("ADMIN")
                    .build();
            return new InMemoryUserDetailsManager(user);
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
                    .formLogin(form -> form
                            .loginPage("/admin/login").permitAll()
                            .loginProcessingUrl("/admin/login")
                            .failureUrl("/admin/login?error=loginError")
                            .defaultSuccessUrl("/admin/dashboard", true))
                    .userDetailsService(adminUserDetailsService())
                    .logout(logout -> logout
                            .logoutUrl("/admin/logout")
                            .logoutSuccessUrl("/admin/login")
                            .deleteCookies("JSESSIONID"))
                    .exceptionHandling(handling -> handling
                            .defaultAuthenticationEntryPointFor(adminAuthenticationEntryPoint(),
                                    new AntPathRequestMatcher("/admin/**"))
                            .accessDeniedPage("/error/403"));

            return http.build();
        }

    }

    @RequiredArgsConstructor
    @Configuration
    @Order(2)
    public static class ClientConfigurationAdapter {

        private final UserDetailsService clientUserDetailsService;

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
                            .requestMatchers(mvcMatcherBuilder.pattern("/user/**"))
                            .hasRole("USER"))
                    .formLogin(form -> form
                            .loginPage("/user/login").permitAll()
                            .loginProcessingUrl("/user/login")
                            .failureUrl("/user/login?error=loginError")
                            .defaultSuccessUrl("/user/profile", true))
                    .userDetailsService(this.clientUserDetailsService)
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .logoutSuccessUrl("/user/login")
                            .deleteCookies("JSESSIONID"))
                    .exceptionHandling(handling -> handling
                            .defaultAuthenticationEntryPointFor(clientAuthenticationEntryPoint(),
                                    new AntPathRequestMatcher("/user/profile"))
                            .accessDeniedPage("/error/403"));

            return http.build();
        }

    }

    @Bean
    public static PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

