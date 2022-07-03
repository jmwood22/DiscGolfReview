package com.jmwood.sample.discgolfreview.model.events;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ClickEvents")
public class ClickEvent extends Event {

    private String elementName;
    private String rawEventJson;
}
