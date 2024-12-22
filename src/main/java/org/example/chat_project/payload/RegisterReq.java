package org.example.chat_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.chat_project.entity.Attachment;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class RegisterReq {
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
    private String confirmPassword;
}
