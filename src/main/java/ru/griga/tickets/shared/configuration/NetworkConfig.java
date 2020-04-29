package ru.griga.tickets.shared.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;

@Configuration
public class NetworkConfig {

    @Bean
    public RequestResponseLoggingInterceptor requestResponseLoggingInterceptor() {
        return new RequestResponseLoggingInterceptor();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(RequestResponseLoggingInterceptor rrli,
                                     MappingJackson2HttpMessageConverter messageConverter) {

        return new RestTemplateBuilder()
                .additionalInterceptors(rrli)
                .additionalMessageConverters(messageConverter)
                .build();

    }

    @Bean
    public MappingJackson2HttpMessageConverter messageConverter(ObjectMapper objectMapper) {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setObjectMapper(objectMapper);
        return converter;
    }

}
