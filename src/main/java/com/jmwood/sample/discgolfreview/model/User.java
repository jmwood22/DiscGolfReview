package com.jmwood.sample.discgolfreview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


@Data
@Document(collection = "users")
public class User {

    @Id
    @JsonProperty("sub")
    private String id;
    private String name;
    private String nickname;
    private String email;

}
