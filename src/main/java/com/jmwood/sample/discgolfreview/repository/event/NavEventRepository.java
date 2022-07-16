package com.jmwood.sample.discgolfreview.repository.event;

import com.jmwood.sample.discgolfreview.model.event.NavEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavEventRepository extends MongoRepository<NavEvent, String> {
}
