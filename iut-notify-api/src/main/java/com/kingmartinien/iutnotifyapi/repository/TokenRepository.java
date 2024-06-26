package com.kingmartinien.iutnotifyapi.repository;

import com.kingmartinien.iutnotifyapi.entity.Token;
import com.kingmartinien.iutnotifyapi.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TokenRepository extends JpaRepository<Token, Long> {

    @Query("FROM Token AS T WHERE T.accessToken = :token OR T.refreshToken = :token")
    Optional<Token> findByAccessTokenOrRefreshToken(String token);

    List<Token> findAllByUser(User user);

    void deleteAllByExpired(boolean expired);

}
