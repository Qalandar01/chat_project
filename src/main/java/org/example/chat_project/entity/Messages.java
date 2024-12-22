package org.example.chat_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.chat_project.base.BaseEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Messages extends BaseEntity {

    @ManyToOne
    private Users fromm;
    @ManyToOne
    private Users too;

    private String text;

    @CreationTimestamp
    private LocalDateTime dateTime = LocalDateTime.now();

    @ManyToOne
    private Attachment attachment;
    public boolean hasAttachment() {
        return attachment != null;
    }

    public String getDateTime() {
       return dateTime.format(DateTimeFormatter.ofPattern("HH:mm"));
    }
}
