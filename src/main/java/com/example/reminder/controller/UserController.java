package com.example.reminder.controller;

import com.example.reminder.dto.JoinRequest;
import com.example.reminder.dto.LoginRequest;
import com.example.reminder.entity.User;
import com.example.reminder.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
  private final UserService userService;

  @GetMapping("/")
  public String home(Model model, @SessionAttribute(name = "userId", required = false) Long userId) {
    User loginUser = userService.getLoginUserById(userId);

    if (loginUser != null) {
      model.addAttribute("user", loginUser); // user 객체 전체를 모델에 추가
    }

    return "user/home";
  }

  @GetMapping("/signup")
  public String joinPage(Model model) {
    model.addAttribute("joinRequest", new JoinRequest());

    return "user/join";
  }

  @PostMapping("/signup")
  public String join(@Valid @ModelAttribute JoinRequest joinRequest, BindingResult bindingResult, Model model) {
    // 이메일 중복 체크
    if (userService.checkEmailDuplicate(joinRequest.getEmail())) {
      bindingResult.addError(new FieldError("joinRequest", "email", "중복된 이메일이 있습니다"));
    }

    // 비밀번호 일치 확인
    if (!joinRequest.getPassword().equals(joinRequest.getPasswordCheck())) {
      bindingResult.addError(new FieldError("joinRequest", "passwordCheck", "비밀번호가 일치하지 않습니다"));
    }

    if (bindingResult.hasErrors()) {
      return "user/join";
    }

    userService.join(joinRequest);
    return "redirect:/user/signin";
  }

  @GetMapping("/signin")
  public String loginPage(Model model) {
    model.addAttribute("loginRequest", new LoginRequest());

    return "user/login";
  }

  @PostMapping("/signin")
  public String login(@ModelAttribute LoginRequest loginRequest, BindingResult bindingResult, HttpServletRequest httpServletRequest, Model model) {
    User user = userService.login(loginRequest);

    // 로그인 아이디나 비밀번호가 틀린경우 global error return
    if (user == null) {
      bindingResult.reject("loginFail", "로그인 아이디 또는 비밀번호가 틀렸습니다");
    }

    if (bindingResult.hasErrors()) {
      return "user/login";
    }

    // 로그인 성공 => 세션 생성

    // 세션 생성 전, 기존에 있던 세션 파기
    httpServletRequest.getSession().invalidate();
    HttpSession session = httpServletRequest.getSession(true); // 세션이 없으면 생성
    // 세션에 userId를 넣어줌
    session.setAttribute("userId", user.getId());
    session.setMaxInactiveInterval(1800); // Session 이 30분 동안 유지

    return "redirect:/user/";
  }

  @GetMapping("/signout")
  public String logout(HttpServletRequest request, Model model) {
    HttpSession session = request.getSession(false);

    if (session != null) {
      session.invalidate();
    }

    return "redirect:/user/";
  }

}
