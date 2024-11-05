package com.example.reminder.service;

import com.example.reminder.entity.Mail;
import com.example.reminder.repository.MailRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MailService {

  private final MailRepository mailRepository;
  private final JavaMailSender mailSender;

  public Mail createMail(Mail mail) {
    LocalDate mailDate = mail.getDate();
    if (mail.getDate().isBefore(LocalDate.now())) {
      throw new IllegalArgumentException("이미 지난 날짜입니다");
    }
    return mailRepository.save(mail);
  }

  public List<Mail> getAllMails() {
    return mailRepository.findAll();
  }

  public Optional<Mail> getMailById(Long id) {
    return mailRepository.findById(id);
  }


  public Mail updateMail(Long id, Mail mailDetail) {
    Mail mail = mailRepository.findById(id)
            .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 아이디입니다 : " + id));

    LocalDate mailDate = mailDetail.getDate();
    if (mailDate.isBefore(LocalDate.now()))
      throw new IllegalArgumentException("이미 지난 날짜입니다");

    mail.setTitle(mailDetail.getTitle());
    mail.setSendTo(mailDetail.getSendTo());
    mail.setDate(mailDetail.getDate());

    return mailRepository.save(mail);
  }

  public void deleteMail(Long id) {
    mailRepository.deleteById(id);
  }


  public void sendMail(Mail mail) throws MessagingException {
    MimeMessage message = mailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

    helper.setTo(mail.getSendTo());
    helper.setSubject("Reminder");

    String htmlContent ="<html>"
            + "<body style='font-family: Arial, sans-serif;'>"
            + "<div style='background-color: #f3f4f6; padding: 20px; border-radius: 5px;'>"
            + "  <h2 style='color: #4a90e2; text-align: center;'>" + mail.getTitle() + "</h2>"
            + "  <p style='font-size: 16px; color: #333;'>안녕하세요!</p>"
            + "  <p style='font-size: 14px; color: #555;'>"
            + "    <strong>기념일 날짜:</strong> " + mail.getDate() + "<br>"
            +  mail.getTitle() + " 을(를) 축하합니다! 특별한 날을 기념하세요."
            + "  </p>"
            + "<p style='font-size: 13px; color: #333; text-align: center;'>기념일 알림 서비스</p>"
            + "</div>"
            + "</body>"
            + "</html>";

    helper.setText(htmlContent, true);

    mailSender.send(message);
  }

  @Scheduled(cron = "0 * * * * ?")
  public void sendScheduleMail() {
    List<Mail> mails = getAllMails();
    LocalDate today = LocalDate.now();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    for (Mail mail : mails) {
      LocalDate mailDate = mail.getDate();
      if (mailDate.isEqual(today)) {
        try {
          sendMail(mail);
        } catch (MessagingException e) {
          e.printStackTrace();
        }
      }
    }
  }
}