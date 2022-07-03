package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.User;
import org.springframework.data.annotation.Id;

import java.util.Date;

public class Event {

    @Id
    private String id;
    private User user;
    private Date date;
}
