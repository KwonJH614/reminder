package com.example.reminder.controller;


import com.example.reminder.entity.Mail;
import com.example.reminder.entity.User;
import com.example.reminder.service.MailService;
import com.example.reminder.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/")
@AllArgsConstructor
public class MailController {
  private MailService mailService;
  private UserService userService;

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
  public String createMail(@ModelAttribute Mail mail, @SessionAttribute(name = "userId") Long userId) {
    User user = userService.getLoginUserById(userId);
    if (user != null) {
      mail.setUser(user);
      mail.setSendTo(user.getEmail());
      mailService.createMail(mail);
    }
    return "redirect:/mail/list";
  }

  @GetMapping("/mail/list")
  public String mailList(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
    if (userId == null) {
      return "redirect:/user/signin"; // 로그인하지 않았다면 로그인 페이지로 리디렉트
    }

    List<Mail> mails = mailService.getMailsByUserId(userId); // 로그인한 사용자의 메일만 가져오기
    model.addAttribute("mails", mails);
    return "/mail/list";
  }

  @GetMapping("/mail/update/{id}")
  public String updateMailPage(@PathVariable Long id, Model model) {
    Mail mail = mailService.getMailById(id)
            .orElseThrow(() -> new IllegalArgumentException("해당 메일이 존재하지 않습니다 : " + id));
    model.addAttribute("mail", mail);
    return "/mail/update";
  }
  @PutMapping("/mail/update/{id}")
  public String updateMail(@PathVariable Long id, Mail mailDetails) {
    mailService.updateMail(id, mailDetails);
    return "redirect:/mail/list";
  }

  @DeleteMapping("/mail/delete/{id}")
  public String deleteMail(@PathVariable Long id) {
    mailService.deleteMail(id);
    return "redirect:/mail/list";
  }
}

