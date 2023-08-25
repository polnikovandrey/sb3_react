package com.mcfly.template.controller.rest.user_role;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mcfly.template.domain.user_role.User;
import com.mcfly.template.exception.UserExistsAlreadyException;
import com.mcfly.template.payload.ApiResponse;
import com.mcfly.template.payload.user_role.AuthDataResponse;
import com.mcfly.template.payload.user_role.LoginRequest;
import com.mcfly.template.payload.user_role.SignUpRequest;
import com.mcfly.template.security.JwtUtils;
import com.mcfly.template.security.UserPrincipal;
import com.mcfly.template.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // TODO Swagger
    // TODO https://www.bezkoder.com/spring-security-refresh-token/

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final AuthDataResponse authDataResponse = authenticateUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(authDataResponse);
    }

    @PostMapping("/signinWithCookie")
    public ResponseEntity<?> authenticateUserWithCookie(@Valid @RequestBody LoginRequest loginRequest) {
        final AuthDataResponse authDataResponse = authenticateUser(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        final ResponseCookie jwtCookie = jwtUtils.produceJwtCookie(authDataResponse.getToken());
        return ResponseEntity
                .ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(authDataResponse);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) throws JsonProcessingException {
        try {
            final User result = userService.registerUser(
                    signUpRequest.getFirstName(),
                    signUpRequest.getLastName(),
                    signUpRequest.getMiddleName(),
                    signUpRequest.getUsername(),
                    signUpRequest.getEmail(),
                    signUpRequest.getPassword(),
                    false);
            final String url = ServletUriComponentsBuilder.fromCurrentContextPath().replacePath(null).toUriString();
            userService.sendEmailConfirmation(url, result.getEmail());
            // TODO ws
            final AuthDataResponse authDataResponse = authenticateUser(result.getUsername(), signUpRequest.getPassword());
            return ResponseEntity.ok().body(authDataResponse);
        } catch (UserExistsAlreadyException exception) {
            return new ResponseEntity<>(new ApiResponse(false, exception.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

    private AuthDataResponse authenticateUser(String username, String password) {
        final UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtUtils.generateToken(authentication);
        final UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        final Long id = userPrincipal.getId();
        final String email = userPrincipal.getEmail();
        final String name = userPrincipal.getUsername();
        final boolean admin = userPrincipal.isAdmin();
        return new AuthDataResponse(id, email, name, token, admin);
    }

    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        SecurityContextHolder.getContext().setAuthentication(null);
        final ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new ApiResponse(true, "You've been signed out!"));
    }
}
