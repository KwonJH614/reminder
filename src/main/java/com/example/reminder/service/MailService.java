package com.example.reminder.service;

import com.example.reminder.entity.Mail;
import com.example.reminder.repository.MailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class MailService {

  private final MailRepository mailRepository;
  private final JavaMailSender javaMailSender;

  public Mail createMail(Mail mail) {
    if (mail.getDate() == null) {
      throw new IllegalArgumentException("날짜를 입력하세요");
    }
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


}