package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.events.enums.AuthEventType;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "AuthEvents")
public class AuthEvent extends Event {

    private AuthEventType type;
    private String token;
}
