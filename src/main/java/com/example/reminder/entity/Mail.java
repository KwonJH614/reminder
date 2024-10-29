package com.example.reminder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mail {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String title;
  private String sendTo;
  private LocalDate date;
}
