package org.example.chat_project.repo;

import org.example.chat_project.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepo extends JpaRepository<Attachment,Integer> {
}
