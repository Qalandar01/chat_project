package org.example.chat_project.entity;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.chat_project.base.BaseEntity;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Attachment extends BaseEntity {
    private String fileName;
    private String contentType;
}
