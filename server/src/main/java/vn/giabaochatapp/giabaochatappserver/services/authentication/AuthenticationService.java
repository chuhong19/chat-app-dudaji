package vn.giabaochatapp.giabaochatappserver.services.authentication;

import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import vn.giabaochatapp.giabaochatappserver.config.exception.*;
import vn.giabaochatapp.giabaochatappserver.data.domains.RefreshToken;
import vn.giabaochatapp.giabaochatappserver.data.domains.Role;
import vn.giabaochatapp.giabaochatappserver.data.domains.Token;
import vn.giabaochatapp.giabaochatappserver.data.domains.User;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.AuthenticationRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.EmailMessage;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.TokenRefreshRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.AuthenticationResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.request.RegisterRequest;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.StandardResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.TokenRefreshResponse;
import vn.giabaochatapp.giabaochatappserver.data.dto.response.UserInfoResponse;
import vn.giabaochatapp.giabaochatappserver.data.enums.TokenType;
import vn.giabaochatapp.giabaochatappserver.data.repository.RefreshTokenRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.RoleRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.TokenRepository;
import vn.giabaochatapp.giabaochatappserver.data.repository.UserRepository;
import vn.giabaochatapp.giabaochatappserver.services.RefreshTokenService;
import vn.giabaochatapp.giabaochatappserver.services.EmailService;
import vn.giabaochatapp.giabaochatappserver.services.validation.PasswordValidatorService;

import java.util.Collections;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordValidatorService passwordValidatorService;
    @Autowired
    public RefreshTokenService refreshTokenService;

    @Autowired
    private EmailService emailService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        validatePassword(request.getPassword());
        Role userRole = getUserRole();
        User user = createUser(request, userRole);
        var savedUser = saveUser(user);
        return authenticateUser(savedUser);
    }

    private void validatePassword(String password) {
        passwordValidatorService.checkPassword(password);
    }

    private Role getUserRole() {
        Optional<Role> userRoleOptional = roleRepository.findByRole("USER");
        if (userRoleOptional.isEmpty()) {
            throw new InvalidRoleDataException("Role not found");
        }
        return userRoleOptional.get();
    }

    private User createUser(RegisterRequest request, Role userRole) {
        return User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .gender(request.getGender())
                .roles(Collections.singleton(userRole))
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .build();
    }

    private User saveUser(User user) {
        return userRepository.save(user);
    }

    private AuthenticationResponse authenticateUser(User user) {
        return _authentication(user, Long.toString(user.getId()));
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsernameOrEmail(request.getUsername()).orElseThrow(() -> new ForbiddenException("Invalid username or password"));
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getId(), request.getPassword()));
        return _authentication(user, Long.toString(user.getId()));
    }

    private AuthenticationResponse _authentication(final User user, final String uid) {
        Objects.requireNonNull(user, "Invalid user");
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());
        revokeAllUserTokens(user);
        saveUserToken(user, jwtToken);

        String confirmationToken = generateConfirmationToken();
        String confirmationLink = "https://google.com/confirm?token=" + confirmationToken;
        EmailMessage emailMessage = new EmailMessage();
        emailMessage.setTo(user.getEmail());
        emailMessage.setContent(confirmationLink);
        emailMessage.setType("registration");

        return AuthenticationResponse.builder().accessToken(jwtToken).refreshToken(refreshToken.getToken()).expiresIn(jwtService.getJwtExpiration()).build();
    }

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder().user(user).token(jwtToken).tokenType(TokenType.BEARER).expired(false).revoked(false).build();
        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());
        if (validUserTokens.isEmpty()) return;
        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public ResponseEntity<?> identifyUser(HttpServletRequest request) {
        String jwtToken = extractTokenFromRequest(request);
        validateToken(jwtToken);
        Claims claims = extractClaimsFromToken(jwtToken);
        UserInfoResponse userInfoResponse = buildUserInfoResponse(claims);

        return ResponseEntity.ok(new StandardResponse<>("200", "Check successfully", userInfoResponse));
    }

    private String extractTokenFromRequest(HttpServletRequest request) {
        String headerAuth = request.getHeader("Authorization");

        if (headerAuth == null || !headerAuth.startsWith("Bearer ")) {
            throw new NotFoundException("Authorization header is missing or invalid");
        }

        return headerAuth.substring(7);
    }

    private void validateToken(String jwtToken) {
        Token token = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new ValidateException("Invalid token"));

        if (token.isRevoked() || token.isExpired()) {
            throw new ValidateException("Token is either revoked or expired");
        }
    }

    private Claims extractClaimsFromToken(String jwtToken) {
        return jwtService.extractAllClaims(jwtToken);
    }

    private UserInfoResponse buildUserInfoResponse(Claims claims) {
        String userId = getClaimAsString(claims, "userId");
        String username = getClaimAsString(claims, "username");
        String email = getClaimAsString(claims, "email");
        String firstname = getClaimAsString(claims, "firstname");
        String lastname = getClaimAsString(claims, "lastname");
        String gender = getClaimAsString(claims, "gender");

        return new UserInfoResponse(
                userId != null ? userId : "",
                username != null ? username : "",
                email != null ? email : "",
                firstname != null ? firstname : "",
                lastname != null ? lastname : "",
                gender != null ? gender : ""
        );
    }

    private String getClaimAsString(Claims claims, String key) {
        Object claimValue = claims.get(key);
        if (claimValue != null) {
            if (claimValue instanceof String) {
                return (String) claimValue;
            } else {
                return claimValue.toString();
            }
        }
        return null;
    }

    public ResponseEntity<?> refreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return refreshTokenRepository.findByToken(requestRefreshToken)
                .map(refreshTokenService::verifyValid)
                .map(RefreshToken::getUser)
                .map(user -> {
                    var allValidToken = tokenRepository.findAllValidTokenByUser(user.getId());
                    allValidToken.forEach(token -> {
                        token.setRevoked(true);
                    });
                    var accessToken = jwtService.generateToken(user);
                    return ResponseEntity.ok(new TokenRefreshResponse(accessToken, requestRefreshToken));
                })
                .orElseThrow(() -> new TokenRefreshException("Refresh token is not in database!"));
    }
}
