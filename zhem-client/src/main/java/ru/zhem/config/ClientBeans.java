package ru.zhem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriTemplateHandler;
import ru.zhem.client.*;

@Configuration
public class ClientBeans {

    @Bean
    public ZhemUserRestClientImpl zhemUserRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new ZhemUserRestClientImpl(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

    @Bean
    public IntervalRestClientImpl intervalRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new IntervalRestClientImpl(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

    @Bean
    public AppointmentRestClientImpl appointmentRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new AppointmentRestClientImpl(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

    @Bean
    public RoleRestClientImpl roleRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new RoleRestClientImpl(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

    @Bean
    public ZhemServiceRestClientImpl zhemServiceRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new ZhemServiceRestClientImpl(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

}
