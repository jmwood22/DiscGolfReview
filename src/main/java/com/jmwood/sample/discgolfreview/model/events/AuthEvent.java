package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.events.enums.AuthEventType;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "AuthEvents")
public class AuthEvent extends Event {

    private AuthEventType type;
}
