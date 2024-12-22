package org.example.chat_project.controls;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.chat_project.entity.Attachment;
import org.example.chat_project.entity.AttachmentContent;
import org.example.chat_project.entity.Messages;
import org.example.chat_project.entity.Users;
import org.example.chat_project.payload.RegisterReq;
import org.example.chat_project.payload.SaveReq;
import org.example.chat_project.repo.AttachmentContentRepo;
import org.example.chat_project.repo.AttachmentRepo;
import org.example.chat_project.repo.MessageRepo;
import org.example.chat_project.repo.UserRepo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final MessageRepo messageRepo;
    private final UserRepo userRepo;
    private final AttachmentContentRepo attachmentContentRepo;
    private final AttachmentRepo attachmentRepo;

    @GetMapping("/home")
    public String home(Model model, HttpSession httpSession, HttpServletRequest httpServletRequest) {
        String id1 = httpServletRequest.getParameter("id");
        Object user = httpSession.getAttribute("selectedUser");
        Users currentUser = (Users) Objects.requireNonNullElse(httpSession.getAttribute("currentUser"), new Users());

        List<Users> users = userRepo.findAllUsersExcept(currentUser.getId());
        model.addAttribute("users", users);
        model.addAttribute("currentUser", currentUser);

        if (id1 != null) {
            Integer id = Integer.parseInt(id1);
            getUser(model, httpSession, currentUser, id);
        } else if (user != null) {
            Users selectedUser = (Users) user;
            Integer id2 = selectedUser.getId();
            getUser(model, httpSession, currentUser, id2);
        }

        return "home";
    }

    private void getUser(Model model, HttpSession httpSession, Users currentUser, Integer id) {
        Optional<Users> optional = userRepo.findById(id);
        if (optional.isPresent()) {
            List<Messages> messages = messageRepo.findAllByFrommAndTooOrTooAndFromm(currentUser.getId(), optional.get().getId());
            model.addAttribute("messages", messages);
            model.addAttribute("selectedUser", optional.get());
            httpSession.setAttribute("selectedUser", optional.get());
        }
    }

    @PostMapping("/send")
    public String acceptMsg(HttpSession httpSession, @RequestParam String text, @RequestParam(required = false) MultipartFile file) throws IOException {
        Users currentUser = (Users) httpSession.getAttribute("currentUser");
        Users selectedUser = (Users) httpSession.getAttribute("selectedUser");
        Attachment attachment = null;
        if (file != null) {
            byte[] bytes = file.getBytes();
            attachment = new Attachment();
            attachment.setFileName(file.getOriginalFilename());
            attachment.setContentType(file.getContentType());
            attachmentRepo.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent(bytes, attachment);
            attachmentContentRepo.save(attachmentContent);
        }
        Messages messages = new Messages();
        messages.setFromm(currentUser);
        messages.setToo(selectedUser);
        messages.setText(text);
        if (attachment != null) {
            messages.setAttachment(attachment);
        }
        messageRepo.save(messages);

        return "redirect:/home";
    }

    @GetMapping("/attachments/download/{attachmentId}")
    public ResponseEntity<byte[]> downloadAttachment(@PathVariable(name = "attachmentId") Integer attachmentId) {
        Optional<AttachmentContent> byAttachmentId = attachmentContentRepo.findByAttachmentId(attachmentId);
        AttachmentContent attachmentContent = byAttachmentId.get();
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentContent.getAttachment().getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, attachmentContent.getAttachment().getContentType())
                .body(attachmentContent.getBytes());
    }

    @GetMapping("/user/get/{id}")
    public void getUserPic(@PathVariable Integer id, HttpServletResponse response ) throws IOException {
        Optional<Users> byId = userRepo.findById(id);
        if (byId.isPresent() && byId.get().getPhoto() !=null){
            Users user = byId.get();
            Attachment photo = user.getPhoto();
            Optional<AttachmentContent> byAttachmentId = attachmentContentRepo.findByAttachmentId(photo.getId());

            response.getOutputStream().write(byAttachmentId.get().getBytes());
        }
    }

    @PostMapping("/settings")
    public String settings(@SessionAttribute Users currentUser,Model model){
        model.addAttribute("firstname",currentUser.getFirstName());
        model.addAttribute("lastname",currentUser.getLastName());
        model.addAttribute("phone",currentUser.getPhone());
        model.addAttribute("password",currentUser.getPassword());
        return "settings";
    }

    @PostMapping("/save")
    public String saveChanges(@SessionAttribute Users currentUser , @ModelAttribute SaveReq saveReq , @RequestParam(required = false) MultipartFile file ) throws IOException {
        currentUser.setFirstName(saveReq.getFirstname());
        currentUser.setLastName(saveReq.getLastname());
        currentUser.setPhone(saveReq.getPhone());
        currentUser.setPassword(saveReq.getPassword());
        if (file != null && !file.isEmpty()){
            Attachment attachment = new Attachment(file.getOriginalFilename(), file.getContentType());
            attachmentRepo.save(attachment);
            AttachmentContent attachmentContent = new AttachmentContent(file.getBytes(),attachment);
            attachmentContentRepo.save(attachmentContent);
            currentUser.setPhoto(attachment);
        }
        userRepo.save(currentUser);
        return "redirect:/home";
    }

}
