package com.jmwood.sample.discgolfreview.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@Document(collection = "courses")
public class Course {

    @Id
    private String id;
    private String name;
    private String location;
    private String description;
    private String defaultImageUrl;
    private String amenities;
    private User author;
    private List<Review> reviews;

    public void addReview(final Review review) {
        if(reviews == null) {
            setReviews(new ArrayList<>(List.of(review)));
        } else {
            reviews.add(review);
        }
    }
}
