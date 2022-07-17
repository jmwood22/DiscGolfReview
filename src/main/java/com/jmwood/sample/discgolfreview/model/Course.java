package com.jmwood.sample.discgolfreview.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "courses")
public class Course implements Serializable {

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
