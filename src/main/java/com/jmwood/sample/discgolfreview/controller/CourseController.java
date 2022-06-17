package com.jmwood.sample.discgolfreview.controller;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    @GetMapping
    public List<Course> getCourses() {
        log.info("Received request to get all courses");
        List<Course> results = courseRepository.findAll();
        log.info("Retrieved {} courses", results.size());
        return results;
    }

    @GetMapping("/{id}")
    public Course getCourse(@PathVariable String id) {
        log.info("Received request to get Course with id {}", id);
        return courseRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    @PostMapping
    public ResponseEntity createCourse(@RequestBody Course course) throws URISyntaxException {
        log.info("Received request to create Course: {}", course);
        Course savedCourse = courseRepository.save(course);
        return ResponseEntity.created(new URI("/courses/" + savedCourse.getId())).body(savedCourse);
    }

    @PutMapping("/{id}")
    public ResponseEntity updateCourse(@PathVariable String id, @RequestBody Course course) {
        log.info("Received request to update Course with id {} to Course: {}", id, course);
        Course currentCourse = courseRepository.findById(id).orElseThrow(RuntimeException::new);
        currentCourse.setName(course.getName());
        currentCourse.setLocation(course.getLocation());
        currentCourse = courseRepository.save(currentCourse);

        return ResponseEntity.ok(currentCourse);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteCourse(@PathVariable String id) {
        log.info("Received request to delete Course with id {}", id);
        courseRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("saveSample")
    public ResponseEntity<HttpStatus> saveSampleCourse() {
        log.info("Received request to save sample Course");
        courseRepository.save(createSampleCourse());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Course createSampleCourse() {
        return Course.builder()
                .name("Sample Course")
                .location("Middle of Nowhere")
                .build();
    }
}
