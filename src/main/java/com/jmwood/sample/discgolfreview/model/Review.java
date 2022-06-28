package com.jmwood.sample.discgolfreview.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Review {

    @Id
    private String id;
    private User author;
    private String text;
    private Integer rating;
}
