package com.jmwood.sample.discgolfreview.repository.event;

import com.jmwood.sample.discgolfreview.model.event.AuthEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuthEventRepository extends MongoRepository<AuthEvent, String> {
}
