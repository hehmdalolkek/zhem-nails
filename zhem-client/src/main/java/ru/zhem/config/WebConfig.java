package ru.zhem.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.zhem.common.interceptor.HeaderInterceptor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class WebConfig implements WebMvcConfigurer {

    private final HeaderInterceptor headerInterceptor;

    @Value("${file.storage.path}")
    private String storageDirectory;

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/admin/dashboard").setViewName("admin/common/dashboard");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this.headerInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:" + storageDirectory + "/");
    }
}
