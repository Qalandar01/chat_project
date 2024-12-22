package org.example.chat_project.repo;

import org.example.chat_project.entity.Messages;
import org.example.chat_project.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepo extends JpaRepository<Messages,Integer> {

    @Query(value = """
select *
from messages m
where (m.fromm_id = :fromm_id and m.too_id = :too_id)
   or (m.too_id = :fromm_id and m.fromm_id = :too_id);
""", nativeQuery = true)
    List<Messages> findAllByFrommAndTooOrTooAndFromm(@Param(value = "fromm_id") Integer fromm_id, @Param(value = "too_id") Integer too_id);
}
