package ru.itis.soup2.converters;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.itis.soup2.models.enums.TaskPriority;

@Component
public class StringToTaskPriorityConverter implements Converter<String, TaskPriority> {

    @Override
    public TaskPriority convert(String source) {
        if (source == null || source.trim().isEmpty()) {
            return null;
        }
        try {
            return TaskPriority.valueOf(source.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
