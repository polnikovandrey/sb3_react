package com.mcfly.poll.controller.rest.user_role;

import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.UserExistsAlreadyException;
import com.mcfly.poll.payload.ApiResponse;
import com.mcfly.poll.payload.user_role.JwtAuthenticationResponse;
import com.mcfly.poll.payload.user_role.LoginRequest;
import com.mcfly.poll.payload.user_role.SignUpRequest;
import com.mcfly.poll.payload.user_role.SignUpResponse;
import com.mcfly.poll.security.JwtUtils;
import com.mcfly.poll.security.UserPrincipal;
import com.mcfly.poll.service.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // TODO https://www.bezkoder.com/spring-security-refresh-token/

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserService userService;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final AuthData authData = authenticateUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(new JwtAuthenticationResponse(authData.getId(), authData.getEmail(), authData.getName(), authData.getToken(), authData.isAdmin()));
    }

    @PostMapping("/signinWithCookie")
    public ResponseEntity<?> authenticateUserWithCookie(@Valid @RequestBody LoginRequest loginRequest) {
        final AuthData authData = authenticateUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        final ResponseCookie jwtCookie = jwtUtils.produceJwtCookie(authData.getToken());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new JwtAuthenticationResponse(authData.getId(), authData.getEmail(), authData.getName(), authData.getToken(), authData.isAdmin()));
    }

    @PostMapping("/signup")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        try {
            final User result = userService.registerUser(
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getMiddleName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    signUpRequest.isAdmin());
            final AuthData authData = authenticateUser(result.getUsername(), signUpRequest.getPassword());
            return ResponseEntity.ok().body(new SignUpResponse(authData.getId(), authData.getEmail(), authData.getName(), authData.getToken(), authData.isAdmin()));
        } catch (UserExistsAlreadyException exception) {
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private AuthData authenticateUser(String username, String password) {
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtils.generateToken(authentication);
        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final Long id = userPrincipal.getId();
        final String email = userPrincipal.getEmail();
        final String name = userPrincipal.getUsername();
        final boolean admin = userPrincipal.isAdmin();
        return new AuthData(id, email, name, token, admin);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
        final ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse(true, "You've been signed out!"));
    }

    @AllArgsConstructor
    @Getter
    @Setter
    public static class AuthData {

        private final Long id;
        private final String email;
        private final String name;
        private final String token;
        private final boolean admin;
    }
}
