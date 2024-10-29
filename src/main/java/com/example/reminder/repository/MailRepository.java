package com.example.reminder.repository;

import com.example.reminder.entity.Mail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MailRepository extends JpaRepository<Mail, Long> {

}
