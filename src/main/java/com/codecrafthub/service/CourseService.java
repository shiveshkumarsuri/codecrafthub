package com.codecrafthub.service;

import com.codecrafthub.model.Course;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class CourseService {
    private static final String DATA_FILE = "courses.json";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Load courses from JSON file
    public List<Course> loadCourses() throws IOException {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            saveCourses(new ArrayList<>());
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(file, new TypeReference<List<Course>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    // Save courses to JSON file
    public void saveCourses(List<Course> courses) throws IOException {
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(new File(DATA_FILE), courses);
    }

    // Generate next ID
    public Integer getNextId(List<Course> courses) {
        return courses.stream()
                .mapToInt(Course::getId)
                .max()
                .orElse(0) + 1;
    }
}
