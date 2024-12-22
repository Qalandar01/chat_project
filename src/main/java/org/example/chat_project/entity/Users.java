package org.example.chat_project.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.chat_project.base.BaseEntity;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
public class Users extends BaseEntity {
    private String firstName;
    private String lastName;
    private String phone;
    private String password;

    @ManyToOne
    private Attachment photo;
}
