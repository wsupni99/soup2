package ru.itis.soup2.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import ru.itis.soup2.converters.StringToProjectStatusConverter;
import ru.itis.soup2.converters.StringToTaskPriorityConverter;
import ru.itis.soup2.converters.StringToTaskStatusConverter;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final StringToTaskStatusConverter stringToTaskStatusConverter;
    private final StringToTaskPriorityConverter stringToTaskPriorityConverter;
    private final StringToProjectStatusConverter stringToProjectStatusConverter;

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(stringToTaskStatusConverter);
        registry.addConverter(stringToTaskPriorityConverter);
        registry.addConverter(stringToProjectStatusConverter);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/user/Documents/ORIS/soup2/uploads/");
    }
}