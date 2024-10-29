package com.example.reminder.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Mail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private long title;
  private long sendTo;
  private long date;
}
