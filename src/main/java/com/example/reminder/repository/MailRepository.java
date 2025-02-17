package com.example.reminder.repository;

import com.example.reminder.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MailRepository extends JpaRepository<Mail, Long> {
  List<Mail> findByUserId(Long userId);
}
