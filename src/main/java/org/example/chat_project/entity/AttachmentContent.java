package org.example.chat_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.chat_project.base.BaseEntity;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
public class AttachmentContent extends BaseEntity {
    private byte[] bytes;

    @ManyToOne
    private Attachment attachment;
}
