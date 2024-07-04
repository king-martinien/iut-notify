package com.kingmartinien.iutnotifyapi.service.impl;

import com.kingmartinien.iutnotifyapi.dto.*;
import com.kingmartinien.iutnotifyapi.entity.Activation;
import com.kingmartinien.iutnotifyapi.entity.Role;
import com.kingmartinien.iutnotifyapi.entity.Token;
import com.kingmartinien.iutnotifyapi.entity.User;
import com.kingmartinien.iutnotifyapi.enums.EmailTemplateEnum;
import com.kingmartinien.iutnotifyapi.enums.RoleEnum;
import com.kingmartinien.iutnotifyapi.mapper.UserMapper;
import com.kingmartinien.iutnotifyapi.repository.ActivationRepository;
import com.kingmartinien.iutnotifyapi.repository.RoleRepository;
import com.kingmartinien.iutnotifyapi.repository.TokenRepository;
import com.kingmartinien.iutnotifyapi.repository.UserRepository;
import com.kingmartinien.iutnotifyapi.security.JwtService;
import com.kingmartinien.iutnotifyapi.service.EmailService;
import com.kingmartinien.iutnotifyapi.service.TokenService;
import com.kingmartinien.iutnotifyapi.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

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
    private final TokenService tokenService;
    private final Validator validator;
    private final UserMapper userMapper;

    @Value("${application.activation-code.expiration}")
    private Long ACTIVATION_CODE_EXPIRATION;
    @Value("${application.activation-code.characters}")
    private String ACTIVATION_CODE_CHARACTERS;
    @Value("${application.activation-code.activationUrl}")
    private String ACTIVATION_CODE_URL;
    @Value("${application.reset-password.url}")
    private String RESET_PASSWORD_URL;

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
            return generateLoginResponseDto(claims, user);
        }
        return null;
    }

    @Override
    public void logout() {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        this.tokenService.revokeUserTokens(user);
    }

    @Override
    public LoginResponseDto refreshToken(RefreshTokenDto refreshTokenDto) {
        Token token = this.tokenRepository.findByRefreshToken(refreshTokenDto.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));
        if (!this.jwtService.isTokenValid(token.getRefreshToken(), token.getUser())) {
            throw new RuntimeException("Expired or invalid refresh token");
        }
        HashMap<String, Object> claims = new HashMap<>();
        User user = token.getUser();
        return generateLoginResponseDto(claims, user);
    }

    @Override
    public void requestPasswordReset(String email) throws MessagingException {
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        this.sendResetPasswordEmail(user);
    }

    @Override
    public void resetPassword(String token, String email, ResetPasswordDto resetPasswordDto) {
        Activation activation = this.activationRepository.findByCode(token)
                .orElseThrow(() -> new RuntimeException("Token code not found"));
        if (activation.getExpiresAt().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Token has expired");
        }
        User user = this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (activation.getUser().getEmail().equals(user.getEmail())) {
            user.setPassword(this.passwordEncoder.encode(resetPasswordDto.getPassword()));
            this.userRepository.save(user);
            activation.setActivated(true);
            this.activationRepository.save(activation);
        }

    }

    @Override
    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public UploadResult uploadUsers(MultipartFile file, RoleEnum role) throws IOException {
        if (!Objects.equals(file.getContentType(), "text/csv")) {
            throw new RuntimeException("Invalid file type : upload a csv file.");
        }
        Reader reader = new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8);
        enum Header {firstname, lastname, email, phone, password}
        CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                .setHeader(Header.class)
                .setIgnoreHeaderCase(true)
                .setSkipHeaderRecord(true)
                .build());
        List<CSVRecord> records = csvParser.getRecords().stream().toList();
        int total = records.size();
        AtomicInteger success = new AtomicInteger();
        AtomicInteger line = new AtomicInteger();
        List<UploadError> errors = new ArrayList<>();
        records.forEach(record -> {
            line.addAndGet(1);
            Set<Role> roles = new HashSet<>();
            Optional<Role> optionalRole = this.roleRepository.findByLabel(role);
            if (optionalRole.isEmpty()) {
                throw new RuntimeException("Role with label " + role + " does not exist");
            }
            roles.add(optionalRole.get());
            User user = new User();
            user.setFirstName(record.get("firstname"));
            user.setLastName(record.get("lastname"));
            user.setEmail(record.get("email"));
            user.setPhone(record.get("phone"));
            user.setEnabled(true);
            user.setPassword(this.passwordEncoder.encode(record.get("password")));
            user.setRoles(roles);
            Set<ConstraintViolation<CreateUserDto>> violations = validator.validate(this.userMapper.toCreateUserDto(user));
            if (violations.isEmpty() &&
                    !this.userRepository.existsByPhone(user.getPhone()) &&
                    !this.userRepository.existsByEmail(user.getEmail())) {
                this.userRepository.save(user);
                success.getAndIncrement();
            } else {
                StringBuilder stringBuilder = new StringBuilder();
                violations.forEach(violation -> {
                    if (!stringBuilder.toString().contains(violation.getMessage())) {
                        stringBuilder.append(violation.getMessage()).append(" ");
                    }
                });
                if (this.userRepository.existsByEmail(user.getEmail())) {
                    stringBuilder.append("Duplicate email address found : ").append(user.getEmail());
                }
                if (this.userRepository.existsByPhone(user.getPhone())) {
                    stringBuilder.append("Duplicate phone number found : ").append(user.getPhone());
                }
                errors.add(UploadError.builder().line(line.get()).error(stringBuilder.toString()).build());
            }
        });
        return UploadResult.builder()
                .total(total)
                .success(success.get())
                .errors(errors)
                .build();
    }

    private LoginResponseDto generateLoginResponseDto(HashMap<String, Object> claims, User user) {
        claims.put("fullname", user.getFullName());
        String accessToken = this.jwtService.generateAccessToken(claims, user);
        String refreshToken = this.jwtService.generateRefreshToken(claims, user);
        this.tokenService.revokeUserTokens(user);
        this.saveToken(accessToken, refreshToken, user);
        return LoginResponseDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
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
        Optional<User> optionalUserWithPhone = userRepository.findByPhone(user.getPhone());
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

    private void sendResetPasswordEmail(User user) throws MessagingException {
        String code = this.generateAndSaveActivationCode(user);
        this.emailService.sendResetPasswordEmail(user, code, RESET_PASSWORD_URL);
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
