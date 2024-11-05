package com.example.reminder.controller;


import com.example.reminder.entity.Mail;
import com.example.reminder.service.MailService;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MailController {
  private MailService mailService;

  @GetMapping
  public String homePage() {
    return "home";
  }

  @GetMapping("/mail/create")
  public String createMailPage(Model model) {
    model.addAttribute(model);
    return "mail/create";
  }

  @PostMapping("/mail/create")
  public String createMail(@ModelAttribute Mail mail) {
    mailService.createMail(mail);
    return "redirect:/mail/list";
  }

  @GetMapping("/mail/list")
  public String getAllMails(Model model) {
    List<Mail> mails = mailService.getAllMails();
    model.addAttribute("mails", mails);
    return "mail/list";
  }

  @GetMapping("/mail/update/{id}")
  public String updateMailPage(@PathVariable Long id, Model model) {
    Mail mail = mailService.getMailById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 메일이 존재하지 않습니다 : " + id));
    model.addAttribute("mail", mail);
    return "/mail/update";
  }

  @PostMapping("/mail/update/{id}")
  public String updateMail(@PathVariable Long id, Mail mailDetails) {
    mailService.updateMail(id, mailDetails);
    return "redirect:/mail/list";
  }

  @DeleteMapping("/mail/delete/{id}")
  public ResponseEntity<Void> deleteMail(@PathVariable Long id) {
    mailService.deleteMail(id);
    return ResponseEntity.noContent().build();
  }
}
