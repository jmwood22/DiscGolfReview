package com.jmwood.sample.discgolfreview.controller;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/courses")
public class CourseController {

    private final CourseRepository courseRepository;

    @GetMapping
    public List<Course> getCourses() {
        return courseRepository.findAll();
    }

    @GetMapping("saveSample")
    public ResponseEntity<HttpStatus> saveSampleCourse() {
        courseRepository.save(createSampleCourse());
        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Course createSampleCourse() {
        return Course.builder()
                .id(UUID.randomUUID().toString())
                .name("Sample Course")
                .location("Middle of Nowhere")
                .build();
    }
}
