package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.CreateUserDto;
import com.kingmartinien.iutnotifyapi.dto.LoginRequestDto;
import com.kingmartinien.iutnotifyapi.dto.LoginResponseDto;
import com.kingmartinien.iutnotifyapi.mapper.UserMapper;
import com.kingmartinien.iutnotifyapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;
    private final UserMapper userMapper;

    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(@RequestBody @Valid CreateUserDto createUserDto) throws MessagingException {
        this.userService.createUser(this.userMapper.toEntity(createUserDto));
    }

    @PostMapping("activate-account")
    @ResponseStatus(HttpStatus.OK)
    public void activateUserAccount(@RequestParam String code) {
        this.userService.activateUserAccount(code);
    }

    @PostMapping("login")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto login(@RequestBody @Valid LoginRequestDto loginRequestDto) {
        return this.userService.login(loginRequestDto);
    }


}
