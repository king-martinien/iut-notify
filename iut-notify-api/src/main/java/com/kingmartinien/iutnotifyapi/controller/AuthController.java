package com.kingmartinien.iutnotifyapi.controller;

import com.kingmartinien.iutnotifyapi.dto.*;
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

    @PostMapping("logout")
    @ResponseStatus(HttpStatus.OK)
    public void logout() {
        this.userService.logout();
    }

    @PostMapping("refresh-token")
    @ResponseStatus(HttpStatus.OK)
    public LoginResponseDto refreshToken(@RequestBody @Valid RefreshTokenDto refreshTokenDto) {
        return this.userService.refreshToken(refreshTokenDto);
    }

    @PostMapping("password-reset/request")
    @ResponseStatus(HttpStatus.OK)
    public void requestPasswordReset(@RequestParam(name = "email") String email) throws MessagingException {
        this.userService.requestPasswordReset(email);
    }

    @PostMapping("password-reset/reset")
    @ResponseStatus(HttpStatus.OK)
    public void resetPassword(
            @RequestParam(name = "token") String token,
            @RequestParam(name = "email") String email,
            @RequestBody @Valid ResetPasswordDto resetPasswordDto
    ) {
        this.userService.resetPassword(token, email, resetPasswordDto);
    }

}
