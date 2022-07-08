package com.jmwood.sample.discgolfreview.repository;

import com.jmwood.sample.discgolfreview.model.events.NavEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface NavEventRepository extends MongoRepository<NavEvent, String> {
}
