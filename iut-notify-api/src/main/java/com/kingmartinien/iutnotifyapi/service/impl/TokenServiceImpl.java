package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.repository.TokenRepository;
import com.kingmartinien.iutnotifyapi.service.TokenService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Transactional
public class TokenServiceImpl implements TokenService {

    private static final Logger log = LoggerFactory.getLogger(TokenServiceImpl.class);
    private final TokenRepository tokenRepository;

    @Override
    @Scheduled(cron = "0 */5 * * * *") // AFTER EVERY 5 MINUTES
    public void removeExpiredTokens() {
        log.info("REMOVING EXPIRED TOKENS AT {}", Instant.now());
        this.tokenRepository.deleteAllByExpired(true);
    }

}
