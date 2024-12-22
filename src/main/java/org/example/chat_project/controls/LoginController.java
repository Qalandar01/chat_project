package org.example.chat_project.controls;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.example.chat_project.entity.Attachment;
import org.example.chat_project.entity.AttachmentContent;
import org.example.chat_project.entity.Users;
import org.example.chat_project.payload.RegisterReq;
import org.example.chat_project.repo.AttachmentContentRepo;
import org.example.chat_project.repo.AttachmentRepo;
import org.example.chat_project.repo.UserRepo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class LoginController {
    private final UserRepo userRepo;
    private final AttachmentRepo attachmentRepo;
    private final AttachmentContentRepo attachmentContentRepo;

    @PostMapping("/logout")
    public String logout(HttpSession httpSession){
        httpSession.invalidate();
        return "login";
    }
    @PostMapping("/login")
    public String getHome(HttpSession httpSession, @RequestParam String phone, @RequestParam String password) {
        Optional<Users> optionalUser = userRepo.findByPhoneAndPassword(phone, password);
        if (optionalUser.isEmpty()) {
            return "login";
        } else {
            Users user = optionalUser.get();
            httpSession.setAttribute("currentUser", user);
            return "redirect:/home";
        }
    }
    @GetMapping("/register")
    public String sendRegisterPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute RegisterReq registerReq, @RequestParam(required = false) MultipartFile file) throws IOException {
        if (!registerReq.getConfirmPassword().equals(registerReq.getPassword())) {
            return "login";
        }
        Users users = new Users();
        users.setFirstName(registerReq.getFirstname());
        users.setLastName(registerReq.getLastname());
        users.setPassword(registerReq.getPassword());
        users.setPhone(registerReq.getPhone());
        users.setPhoto(getAttachment(file));
        userRepo.save(users);

        return "login";
    }

    private Attachment getAttachment(MultipartFile profilePic) throws IOException {
        if (!profilePic.isEmpty()) {
            Attachment attachment = new Attachment(profilePic.getOriginalFilename(), profilePic.getContentType());
            attachmentRepo.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent(profilePic.getBytes(), attachment);
            attachmentContentRepo.save(attachmentContent);
            return attachment;
        }
        return null;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
