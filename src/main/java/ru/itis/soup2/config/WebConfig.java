package ru.itis.soup2.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Основной обработчик — важно использовать file: с абсолютным путём для Windows
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/user/Documents/ORIS/soup2/uploads/");

        // Дополнительно — относительный путь (на всякий случай)
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/", "file:./uploads/");
    }
}