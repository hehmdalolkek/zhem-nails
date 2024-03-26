package ru.zhem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.client.RestClient;
import ru.zhem.client.RestClientClientRestClient;
import ru.zhem.client.RestClientWorkIntervalRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientWorkIntervalRestClient WorkIntervalRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new RestClientWorkIntervalRestClient(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

    @Bean
    public RestClientClientRestClient ClientRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl,
            @Value("${zhem-nails.services.service.name}") String serviceUserName,
            @Value("${zhem-nails.services.service.password}") String serviceUserPassword) {
        return new RestClientClientRestClient(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .requestInterceptor(
                                new BasicAuthenticationInterceptor(serviceUserName, serviceUserPassword))
                        .build()
        );
    }

}
