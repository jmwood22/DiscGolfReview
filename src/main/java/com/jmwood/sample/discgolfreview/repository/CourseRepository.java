package com.jmwood.sample.discgolfreview.repository;

import com.jmwood.sample.discgolfreview.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.UUID;

public interface CourseRepository extends MongoRepository<Course, String> {
}
