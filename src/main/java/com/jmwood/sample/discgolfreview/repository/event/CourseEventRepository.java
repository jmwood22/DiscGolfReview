package com.jmwood.sample.discgolfreview.repository.event;

import com.jmwood.sample.discgolfreview.model.event.CourseEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CourseEventRepository extends MongoRepository<CourseEvent, String> {
}
