package ru.zhem.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    // TODO раздать статику здесь
    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/error/403").setViewName("service/common/errors/403");
    }

}
