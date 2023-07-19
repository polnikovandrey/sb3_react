package com.mcfly.poll.controller.user_role;

import com.mcfly.poll.domain.user_role.Role;
import com.mcfly.poll.domain.user_role.RoleName;
import com.mcfly.poll.domain.user_role.User;
import com.mcfly.poll.exception.AppException;
import com.mcfly.poll.payload.ApiResponse;
import com.mcfly.poll.payload.user_role.JwtAuthenticationResponse;
import com.mcfly.poll.payload.user_role.LoginRequest;
import com.mcfly.poll.payload.user_role.SignUpRequest;
import com.mcfly.poll.repository.user_role.RoleRepository;
import com.mcfly.poll.repository.user_role.UserRepository;
import com.mcfly.poll.security.JwtTokenProvider;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    JwtTokenProvider tokenProvider;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        final UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(loginRequest.getUsernameOrEmail(), loginRequest.getPassword());
        final Authentication authentication = authenticationManager.authenticate(authenticationToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new ApiResponse(false, "Username is already taken!"), HttpStatus.BAD_REQUEST);
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new ApiResponse(false, "Email Address is already in use"), HttpStatus.BAD_REQUEST);
        }
        final User user = new User(signUpRequest.getName(), signUpRequest.getUsername(), signUpRequest.getEmail(), signUpRequest.getPassword());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        final Role userRole = roleRepository.findByName(RoleName.ROLE_USER).orElseThrow(() -> new AppException("User Role not set."));
        user.setRoles(Collections.singleton(userRole));
        final User result = userRepository.save(user);
        final URI location
                = ServletUriComponentsBuilder.fromCurrentContextPath().path("/api/users/{username}").buildAndExpand(result.getUsername()).toUri();
        return ResponseEntity.created(location).body(new ApiResponse(true, "User registered successfully"));
    }
}
