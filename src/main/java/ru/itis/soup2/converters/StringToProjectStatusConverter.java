package ru.itis.soup2.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.soup2.models.enums.ProjectStatus;

@Component
public class StringToProjectStatusConverter implements Converter<String, ProjectStatus> {

    @Override
    public ProjectStatus convert(String source) {
        if (source.trim().isEmpty()) {
            return ProjectStatus.PLANNED;
        }
        try {
            return ProjectStatus.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return ProjectStatus.PLANNED;
        }
    }
}
