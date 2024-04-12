package com.hyd.job.server.repositories;

import com.hyd.job.server.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  User findByUserName(String username);
}
