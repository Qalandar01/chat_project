package org.example.chat_project.repo;

import org.example.chat_project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<Users,Integer> {
    Optional<Users> findByPhoneAndPassword(String phone,String password);

    @Query(value = "select * from users u where u.id <> :id" ,nativeQuery = true)
    List<Users> findAllUsersExcept(@Param("id") Integer id);


}
