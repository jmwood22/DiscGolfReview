package com.jmwood.sample.discgolfreview.model;

import lombok.Builder;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String name;
    private String location;
}
