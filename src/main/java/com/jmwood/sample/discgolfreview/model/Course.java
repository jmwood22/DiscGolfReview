package com.jmwood.sample.discgolfreview.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@ToString
@Getter
@Setter
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String name;
    private String location;
}
