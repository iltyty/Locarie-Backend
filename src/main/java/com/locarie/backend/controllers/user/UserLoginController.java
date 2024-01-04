package com.locarie.backend.controllers.user;

import com.locarie.backend.domain.dto.ResponseDto;
import com.locarie.backend.domain.dto.user.UserLoginRequestDto;
import com.locarie.backend.domain.dto.user.UserLoginResponseDto;
import com.locarie.backend.global.ResultCode;
import com.locarie.backend.services.user.UserLoginService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users/login")
public class UserLoginController {
  private final UserLoginService service;

  public UserLoginController(UserLoginService service) {
    this.service = service;
  }

  @PostMapping
  public ResponseDto<UserLoginResponseDto> login(UserLoginRequestDto dto) {
    UserLoginResponseDto result = service.login(dto);
    return result == null ? ResponseDto.fail(ResultCode.RC202) : ResponseDto.success(result);
  }
}
