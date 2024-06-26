package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.dto.LoginRequestDto;
import com.kingmartinien.iutnotifyapi.dto.LoginResponseDto;
import com.kingmartinien.iutnotifyapi.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    void createUser(User user) throws MessagingException;

    void activateUserAccount(String code);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

}
