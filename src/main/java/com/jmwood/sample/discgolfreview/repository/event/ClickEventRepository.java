package com.jmwood.sample.discgolfreview.repository.event;

import com.jmwood.sample.discgolfreview.model.event.ClickEvent;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ClickEventRepository extends MongoRepository<ClickEvent, String> {}
