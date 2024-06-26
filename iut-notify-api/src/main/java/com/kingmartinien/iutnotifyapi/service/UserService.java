package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.User;
import jakarta.mail.MessagingException;

public interface UserService {

    void createUser(User user) throws MessagingException;

    void activateUserAccount(String code);

}
