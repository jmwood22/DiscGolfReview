package com.jmwood.sample.discgolfreview.model.event;

import com.jmwood.sample.discgolfreview.model.Course;
import com.jmwood.sample.discgolfreview.model.event.enums.CourseEventType;
import lombok.Data;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@ToString(callSuper = true)
@Document(collection = "CourseEvents")
public class CourseEvent extends Event {

    private Course course;
    private String rawJson;
    private CourseEventType type;
}
