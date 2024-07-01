package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.dto.LoginRequestDto;
import com.kingmartinien.iutnotifyapi.dto.LoginResponseDto;
import com.kingmartinien.iutnotifyapi.dto.RefreshTokenDto;
import com.kingmartinien.iutnotifyapi.dto.ResetPasswordDto;
import com.kingmartinien.iutnotifyapi.entity.User;
import jakarta.mail.MessagingException;

import java.util.List;

public interface UserService {

    void createUser(User user) throws MessagingException;

    void activateUserAccount(String code);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void logout();

    LoginResponseDto refreshToken(RefreshTokenDto refreshTokenDto);

    void requestPasswordReset(String email) throws MessagingException;

    void resetPassword(String token, String email, ResetPasswordDto resetPasswordDto);

    List<User> getAllUsers();

}
