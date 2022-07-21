package com.jmwood.sample.discgolfreview.model.event;

import com.jmwood.sample.discgolfreview.model.event.enums.AuthEventType;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Jacksonized
@SuperBuilder
@ToString(callSuper = true)
@Document(collection = "AuthEvents")
public class AuthEvent extends Event {

  private AuthEventType type;
}
