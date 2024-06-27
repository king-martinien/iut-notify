package com.kingmartinien.iutnotifyapi.service;

import com.kingmartinien.iutnotifyapi.entity.User;

public interface TokenService {

    void removeExpiredTokens();

    void revokeUserTokens(User user);

}
