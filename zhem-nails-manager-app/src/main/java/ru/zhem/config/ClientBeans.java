package ru.zhem.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import ru.zhem.client.RestClientWorkIntervalRestClient;

@Configuration
public class ClientBeans {

    @Bean
    public RestClientWorkIntervalRestClient WorkIntervalRestClient(
            @Value("${zhem-nails.services.service.url:http://localhost:8081}") String serviceBaseUrl) {
        return new RestClientWorkIntervalRestClient(
                RestClient.builder()
                        .baseUrl(serviceBaseUrl)
                        .build()
        );
    }

}
