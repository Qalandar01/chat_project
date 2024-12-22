package org.example.chat_project.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.repository.Query;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class SaveReq {
    private String firstname;
    private String lastname;
    private String phone;
    private String password;
}
