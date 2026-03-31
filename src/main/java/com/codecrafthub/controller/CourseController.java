package com.codecrafthub.controller;

import com.codecrafthub.model.Course;
import com.codecrafthub.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@RestController
@RequestMapping("/api")
public class CourseController {

    @Autowired
    private CourseService courseService;

    private static final List<String> VALID_STATUSES = Arrays.asList("Not Started", "In Progress", "Completed");

    // GET all courses
    @GetMapping("/courses")
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        try {
            List<Course> courses = courseService.loadCourses();
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("count", courses.size());
            response.put("courses", courses);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to retrieve courses: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // GET specific course
    @GetMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> getCourse(@PathVariable Integer id) {
        try {
            List<Course> courses = courseService.loadCourses();
            Optional<Course> course = courses.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();

            if (course.isPresent()) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("course", course.get());
                return ResponseEntity.ok(response);
            } else {
                return createErrorResponse("Course with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return createErrorResponse("Failed to retrieve course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // POST new course
    @PostMapping("/courses")
    public ResponseEntity<Map<String, Object>> addCourse(@RequestBody Course course) {
        try {
            // Validate required fields
            String validationError = validateCourse(course, true);
            if (validationError != null) {
                return createErrorResponse(validationError, HttpStatus.BAD_REQUEST);
            }

            List<Course> courses = courseService.loadCourses();

            // Set auto-generated fields
            course.setId(courseService.getNextId(courses));
            course.setCreatedAt(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

            courses.add(course);
            courseService.saveCourses(courses);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course added successfully");
            response.put("course", course);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to add course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // PUT update course
    @PutMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Integer id, @RequestBody Course updatedCourse) {
        try {
            List<Course> courses = courseService.loadCourses();
            Optional<Course> existingCourse = courses.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();

            if (!existingCourse.isPresent()) {
                return createErrorResponse("Course with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            // Validate status if provided
            if (updatedCourse.getStatus() != null && !VALID_STATUSES.contains(updatedCourse.getStatus())) {
                return createErrorResponse("Status must be one of: " + String.join(", ", VALID_STATUSES), HttpStatus.BAD_REQUEST);
            }

            Course course = existingCourse.get();

            // Update fields if provided
            if (updatedCourse.getName() != null) course.setName(updatedCourse.getName());
            if (updatedCourse.getDescription() != null) course.setDescription(updatedCourse.getDescription());
            if (updatedCourse.getTargetDate() != null) course.setTargetDate(updatedCourse.getTargetDate());
            if (updatedCourse.getStatus() != null) course.setStatus(updatedCourse.getStatus());

            courseService.saveCourses(courses);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course updated successfully");
            response.put("course", course);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to update course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // DELETE course
    @DeleteMapping("/courses/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Integer id) {
        try {
            List<Course> courses = courseService.loadCourses();
            Optional<Course> courseToDelete = courses.stream()
                    .filter(c -> c.getId().equals(id))
                    .findFirst();

            if (!courseToDelete.isPresent()) {
                return createErrorResponse("Course with ID " + id + " not found", HttpStatus.NOT_FOUND);
            }

            courses.remove(courseToDelete.get());
            courseService.saveCourses(courses);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Course deleted successfully");
            response.put("deleted_course", courseToDelete.get());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return createErrorResponse("Failed to delete course: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Helper method to validate course
    private String validateCourse(Course course, boolean checkRequired) {
        if (checkRequired) {
            if (course.getName() == null || course.getName().trim().isEmpty()) {
                return "Missing required field: name";
            }
            if (course.getDescription() == null || course.getDescription().trim().isEmpty()) {
                return "Missing required field: description";
            }
            if (course.getTargetDate() == null || course.getTargetDate().trim().isEmpty()) {
                return "Missing required field: target_date";
            }
            if (course.getStatus() == null || course.getStatus().trim().isEmpty()) {
                return "Missing required field: status";
            }
        }

        if (course.getStatus() != null && !VALID_STATUSES.contains(course.getStatus())) {
            return "Status must be one of: " + String.join(", ", VALID_STATUSES);
        }

        return null;
    }

    // Helper method to create error response
    private ResponseEntity<Map<String, Object>> createErrorResponse(String message, HttpStatus status) {
        Map<String, Object> response = new HashMap<>();
        response.put("success", false);
        response.put("error", message);
        return ResponseEntity.status(status).body(response);
    }
}
