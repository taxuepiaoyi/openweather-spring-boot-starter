package com.bruce.openweather.service;


import com.bruce.openweather.dto.WeatherResponseDTO;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class OpenWeatherService {
    private final String  apiKey;
    private final String  baseUrl;

    public OpenWeatherService(String apiKey, String baseUrl) {
        this.apiKey = apiKey;
        this.baseUrl = baseUrl;
    }

    public String getWeather(String city) {
        try {
            WeatherResponseDTO response = fetchWeatherFromApi(city).block();
            if (response == null) {
                return "无法获取" + city + "的天气数据";
            }
            return formatWeatherResponse(city, response);
        } catch (Exception e) {
            return "获取" + city + "天气时发生错误: " + e.getMessage();
        }
    }

    private String formatWeatherResponse(String city, WeatherResponseDTO response) {
        WeatherResponseDTO.Main main = response.getMain();
        WeatherResponseDTO.Weather[] weather = response.getWeather();
        String weatherDescription = weather.length > 0 ? weather[0].getDescription() : "未知";
        return String.format(
                "当前%s天气%s，温度%.1f摄氏度，湿度%d%%，气压%d hPa",
                city,
                weatherDescription,
                main.getTemp(),
                main.getHumidity(),
                main.getPressure()
        );
    }

    private Mono<WeatherResponseDTO> fetchWeatherFromApi(String city) {
        WebClient webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("q", city)
                        .queryParam("appid", apiKey)
                        .queryParam("units", "metric")
                        .build())
                .retrieve()
                .bodyToMono(WeatherResponseDTO.class);
    }
}
