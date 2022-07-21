package com.jmwood.sample.discgolfreview.repository;

import com.jmwood.sample.discgolfreview.model.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseRepository extends MongoRepository<Course, String> {}
