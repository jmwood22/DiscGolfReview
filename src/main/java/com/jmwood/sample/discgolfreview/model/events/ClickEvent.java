package com.jmwood.sample.discgolfreview.model.events;

import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "ClickEvents")
public class ClickEvent extends Event {

    private String elementName;
    private String rawEventJson;
}
