package com.jmwood.sample.discgolfreview.model.events;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "NavEvents")
public class NavEvent extends Event {

    private String path;
    private String rawLocationJson;
}
