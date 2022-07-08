package com.jmwood.sample.discgolfreview.model.events;

import com.jmwood.sample.discgolfreview.model.User;
import lombok.Data;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.util.Date;

@Data
public class Event implements Serializable {

    @Id
    private String id;
    private User user;
    private Date date;
    private String token;
}
