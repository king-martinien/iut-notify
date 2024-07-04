package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.dto.*;
import com.kingmartinien.iutnotifyapi.entity.User;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

    UploadResult uploadUsers(MultipartFile file, RoleEnum role) throws IOException;

}
