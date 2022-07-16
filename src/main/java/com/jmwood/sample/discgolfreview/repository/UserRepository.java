package com.jmwood.sample.discgolfreview.repository;

import com.jmwood.sample.discgolfreview.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

public interface UserRepository extends MongoRepository<User, String> {

    @Query("{username:'?0'}")
    User findUserByUserName(String username);
}
