package ru.itis.soup2.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.soup2.models.enums.TaskStatus;

@Component
public class StringToTaskStatusConverter implements Converter<String, TaskStatus> {

    @Override
    public TaskStatus convert(String source) {
        if (source.trim().isEmpty()) {
            return null;
        }
        try {
            return TaskStatus.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}