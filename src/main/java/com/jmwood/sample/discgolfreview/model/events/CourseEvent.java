package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.model.events.enums.CourseEventType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "CourseEvents")
public class CourseEvent extends Event {

    private Course course;
    private String rawJson;
    private CourseEventType type;
}
