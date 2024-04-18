package ru.zhem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;
import ru.zhem.client.ZhemUserRestClient;
import ru.zhem.service.AdminUserDetailsService;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityBeans {

    @RequiredArgsConstructor
    @Configuration
    @Order(1)
    public static class AdminConfigurationAdapter {

        private final ZhemUserRestClient zhemUserRestClient;

        private final CheckAdminIsExistsFilter checkAdminIsExistsFilter;

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
                    .addFilterBefore(this.checkAdminIsExistsFilter, BasicAuthenticationFilter.class)
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(mvcMatcherBuilder.pattern("/admin/registration"))
                            .permitAll()
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
                            .logoutSuccessUrl("/")
                            .deleteCookies("JSESSIONID"));

            return http.build();
        }

    }

    @Configuration
    @Order(2)
    public static class ClientConfigurationAdapter {

        @Autowired
        public ClientConfigurationAdapter(@Qualifier("zhemUserDetailsService") UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        private final UserDetailsService userDetailsService;

        @Bean
        public AuthenticationEntryPoint clientAuthenticationEntryPoint() {
            return new LoginUrlAuthenticationEntryPoint("/user/login");
        }

        @Bean
        public SecurityFilterChain clientSecurityFilterChain(HttpSecurity http,
                                                             HandlerMappingIntrospector introspector) throws Exception {
            MvcRequestMatcher.Builder mvcMatcherBuilder = new MvcRequestMatcher.Builder(introspector);
            http.securityMatcher("/**")
                    .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
                            .requestMatchers(mvcMatcherBuilder.pattern("/user/registration"))
                            .anonymous()
                            .requestMatchers(mvcMatcherBuilder.pattern("/user/**"))
                            .hasRole("CLIENT")
                            .requestMatchers(mvcMatcherBuilder.pattern("/**"))
                            .permitAll())
                    .userDetailsService(this.userDetailsService)
                    .formLogin(form -> form
                            .loginPage("/user/login").permitAll()
                            .loginProcessingUrl("/user/login")
                            .failureUrl("/user/login?error=loginError")
                            .defaultSuccessUrl("/", true))
                    .logout(logout -> logout
                            .logoutUrl("/user/logout")
                            .logoutSuccessUrl("/")
                            .deleteCookies("JSESSIONID"));

            return http.build();
        }

    }

    @Bean
    public static PasswordEncoder bcryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

