package com.hyd.job.server.repositories;

import com.hyd.job.server.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

  List<User> findByProduct(String product);

  // 添加其他需要的查询方法
}
