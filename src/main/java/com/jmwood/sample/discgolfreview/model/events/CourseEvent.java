package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.model.events.enums.CourseEventType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "CourseEvents")
public class CourseEvent extends Event {

    private Course course;
    private String rawJson;
    private CourseEventType type;
}
