package com.bruce.openweather;

import com.bruce.openweather.service.OpenWeatherService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;
// 添加静态导入
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class OpenweatherSpringBootStarterApplicationTests {

    private final ApplicationContextRunner contextRunner = new ApplicationContextRunner()
            .withConfiguration(AutoConfigurations.of(OpenWeatherAutoConfiguration.class));

    @Test
    void contextLoads() {
    }

    @Test
    public void testGetWeather() {
        OpenWeatherProperties properties = new OpenWeatherProperties();
        properties.setApiKey(System.getenv("OPENWEATHER_KEY"));
        properties.setBaseUrl("https://api.openweathermap.org/data/2.5/weather");
        OpenWeatherService service = new OpenWeatherService(properties.getApiKey(), properties.getBaseUrl());
        System.out.println(service.getWeather("GuangDong"));
    }


    /**
     * 测试自动配置类是否正确加载
     */
    @Test
    public void testAutoConfigurationLoadsWithCustomProperties() {
        contextRunner.withPropertyValues("openweather.apiKey=" + System.getenv("OPENWEATHER_KEY"))
                .withPropertyValues("openweather.baseUrl=https://api.openweathermap.org/data/2.5/weather")
                .run(context -> {
                    assertThat(context).hasSingleBean(OpenWeatherService.class);
                    assertThat(context.getBean(OpenWeatherService.class).getWeather("Beijing"));
                });
    }

    @Test
    public void testAutoConfigurationDoesNotLoadWhenServiceClassMissing() {
        // 使用一个不包含HelloService的空配置来模拟类路径中没有HelloService的情况
        ApplicationContextRunner emptyContextRunner = new ApplicationContextRunner()
                .withConfiguration(AutoConfigurations.of());

        emptyContextRunner.run(context -> {
            assertThat(context).doesNotHaveBean(OpenWeatherService.class);
        });
    }

}
