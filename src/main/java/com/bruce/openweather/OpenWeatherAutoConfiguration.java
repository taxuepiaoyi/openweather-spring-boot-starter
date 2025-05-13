package com.bruce.openweather;

import com.bruce.openweather.service.OpenWeatherService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(OpenWeatherProperties.class)
public class OpenWeatherAutoConfiguration {

    public OpenWeatherAutoConfiguration() {
    }

    @Bean
    @ConditionalOnMissingBean
    public OpenWeatherService openWeatherService(OpenWeatherProperties properties) {
        return new OpenWeatherService(properties.getApiKey(), properties.getBaseUrl()) ;
    }
}
