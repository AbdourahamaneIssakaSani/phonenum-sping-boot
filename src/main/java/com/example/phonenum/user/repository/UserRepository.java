package com.example.phonenum.user.repository;


import com.example.phonenum.user.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Repository class for {@link User}
 */
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber);
}
