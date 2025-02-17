package com.example.reminder.service;

import com.example.reminder.dto.JoinRequest;
import com.example.reminder.dto.LoginRequest;
import com.example.reminder.entity.User;
import com.example.reminder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  /**
   * 이메일 중복 체크
   * 중복되면 true 반환
   */
  public boolean checkEmailDuplicate(String email) {
    return userRepository.existsByEmail(email);
  }

  public void join(JoinRequest req) {
    userRepository.save(req.toEntity());
  }

  public User login(LoginRequest req) {
    Optional<User> optionalUser = userRepository.findByEmail(req.getEmail());

    if (optionalUser.isEmpty()) {
      return null;
    }

    User user = optionalUser.get();
    if (!user.getPassword().equals(req.getPassword())) {
      return null;
    }

    return user;
  }

  public User getLoginUserById(Long userId) {
    if (userId == null) {
      return null;
    }
    return userRepository.findById(userId).orElse(null);
  }
}
