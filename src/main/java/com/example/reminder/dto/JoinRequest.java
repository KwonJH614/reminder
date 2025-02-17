package com.example.reminder.dto;


import com.example.reminder.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JoinRequest {

  @NotBlank(message = "이메일이 비어있습니다.")
  private String email;

  @NotBlank(message = "비밀번호가 비어있습니다.")
  private String password;
  private String passwordCheck;


  public User toEntity() {
    return User.builder()
            .email(this.email)
            .password(this.password)
            .build();
  }

}
