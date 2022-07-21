package com.jmwood.sample.discgolfreview.repository;

import com.jmwood.sample.discgolfreview.model.SessionActivity;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SessionActivityRepository extends MongoRepository<SessionActivity, String> {}
