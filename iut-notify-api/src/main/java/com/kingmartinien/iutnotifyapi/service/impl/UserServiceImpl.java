package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.dto.LoginRequestDto;
import com.kingmartinien.iutnotifyapi.dto.LoginResponseDto;
import com.kingmartinien.iutnotifyapi.entity.Activation;
import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.entity.Token;
import com.kingmartinien.iutnotifyapi.entity.User;
import com.kingmartinien.iutnotifyapi.enums.EmailTemplateEnum;
import com.kingmartinien.iutnotifyapi.repository.ActivationRepository;
import com.kingmartinien.iutnotifyapi.repository.RoleRepository;
import com.kingmartinien.iutnotifyapi.repository.TokenRepository;
import com.kingmartinien.iutnotifyapi.repository.UserRepository;
import com.kingmartinien.iutnotifyapi.security.JwtService;
import com.kingmartinien.iutnotifyapi.service.EmailService;
import com.kingmartinien.iutnotifyapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ActivationRepository activationRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @Value("${application.activation-code.expiration}")
    private Long ACTIVATION_CODE_EXPIRATION;
    @Value("${application.activation-code.characters}")
    private String ACTIVATION_CODE_CHARACTERS;
    @Value("${application.activation-code.activationUrl}")
    private String ACTIVATION_CODE_URL;

    @Override
    @Transactional
    public void createUser(User user) throws MessagingException {
        checkIfUserAlreadyExist(user);
        Set<Role> roles = new HashSet<>();
        user.getRoles().forEach(role -> {
            Optional<Role> optionalRole = this.roleRepository.findByLabel(role.getLabel());
            if (optionalRole.isEmpty()) {
                throw new RuntimeException("Role with label " + role.getLabel() + " does not exist");
            }
            roles.add(optionalRole.get());
        });
        user.setRoles(roles);
        user.setPassword(this.passwordEncoder.encode(user.getPassword()));
        this.userRepository.save(user);
        this.sendActivationEmail(user);
    }

    @Override
    public void activateUserAccount(String code) {
        Activation activation = this.activationRepository.findByCode(code)
                .orElseThrow(() -> new RuntimeException("Invalid code"));
        if (activation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Activation code expired");
        }
        User user = this.userRepository.findByEmail(activation.getUser().getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));
        user.setEnabled(true);
        this.userRepository.save(user);
        activation.setActivated(true);
        this.activationRepository.save(activation);
    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
        if (authentication.isAuthenticated()) {
            User user = (User) authentication.getPrincipal();
            HashMap<String, Object> claims = new HashMap<>();
            claims.put("fullname", user.getFullName());
            String accessToken = this.jwtService.generateAccessToken(claims, user);
            String refreshToken = this.jwtService.generateRefreshToken(claims, user);
            this.revokeUserTokens(user);
            this.saveToken(accessToken, refreshToken, user);
            return LoginResponseDto.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build();
        }
        return null;
    }

    private void revokeUserTokens(User user) {
        List<Token> userTokens = this.tokenRepository.findAllByUser(user);
        userTokens.forEach(token -> token.setExpired(true));
        this.tokenRepository.saveAll(userTokens);
    }

    private void saveToken(String accessToken, String refreshToken, User user) {
        Token token = Token.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .user(user)
                .build();
        this.tokenRepository.save(token);
    }

    private void checkIfUserAlreadyExist(User user) {
        Optional<User> optionalUserWithEmail = userRepository.findByEmail(user.getEmail());
        Optional<User> optionalUserWithPhone = userRepository.findByEmail(user.getPhone());
        if (optionalUserWithEmail.isPresent()) {
            throw new RuntimeException("User with email " + user.getEmail() + " already exists");
        }
        if (optionalUserWithPhone.isPresent()) {
            throw new RuntimeException("User with phone " + user.getPhone() + " already exists");
        }
    }

    private void sendActivationEmail(User user) throws MessagingException {
        String code = this.generateAndSaveActivationCode(user);
        this.emailService.sendActivationEmail(
                user.getEmail(),
                user.getFullName(),
                "Your Notify Verification Code",
                code,
                ACTIVATION_CODE_URL,
                EmailTemplateEnum.ACCOUNT_ACTIVATION);
    }

    private String generateAndSaveActivationCode(User user) {
        Activation activation = Activation.builder()
                .code(this.generateCode())
                .expiresAt(LocalDateTime.now().plusMinutes(ACTIVATION_CODE_EXPIRATION))
                .user(user)
                .build();
        return this.activationRepository.save(activation).getCode();
    }

    private String generateCode() {
        String characters = ACTIVATION_CODE_CHARACTERS;
        SecureRandom random = new SecureRandom();
        StringBuilder codeBuilder = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            int index = random.nextInt(characters.length());
            codeBuilder.append(characters.charAt(index));
        }
        return codeBuilder.toString();
    }

}
