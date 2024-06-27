package com.kingmartinien.iutnotifyapi.service;

import org.springframework.scheduling.annotation.Scheduled;

public interface TokenService {

    void removeExpiredTokens();

}
