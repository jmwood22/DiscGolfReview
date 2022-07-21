package com.jmwood.sample.discgolfreview.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Data
@Jacksonized
@Builder
@Document(collection = "users")
public class User implements Serializable {

  @Id
  @JsonProperty("sub")
  private String id;

  private String name;
  private String nickname;
  private String email;
}
