package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.repo.impl.TacoRepository;
import com.example.tacocloudapplication.service.TacoService;
import com.example.tacocloudapplication.service.TokenBlacklistService;
import com.example.tacocloudapplication.table.Taco;
import com.example.tacocloudapplication.table.util.AuthenticationRequest;
import com.example.tacocloudapplication.table.util.AuthenticationResponse;
import com.example.tacocloudapplication.table.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class LoginController {

    private AuthenticationManager authenticationManager;

    private JwtUtil jwtUtil;

    private UserDetailsService userDetailsService;

    private TokenBlacklistService tokenBlacklistService;

    private TacoService tacoService;

    public LoginController(AuthenticationManager authenticationManager, JwtUtil jwtUtil, UserDetailsService userDetailsService, TokenBlacklistService tokenBlacklistService, TacoService tacoService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.tokenBlacklistService = tokenBlacklistService;
        this.tacoService = tacoService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
        }
        catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        System.out.println("Generated JWT:" + jwt);

        boolean isValidToken = jwtUtil.validateToken(jwt, userDetails);

        System.out.println(isValidToken);

        if (isValidToken) {
            return ResponseEntity.ok(new AuthenticationResponse(jwt));
        } else {
            throw new Exception("Token validation failed");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String token) {
        tokenBlacklistService.blacklistToken(token);
        return ResponseEntity.ok(new HashMap<String, String>() {{
            put("message", "Logged out successfully");
        }});
    }

    @DeleteMapping("/deleteTacosWithoutOrder")
    @Transactional
    public ResponseEntity<Void> deleteTacosWithoutOrder() {
        tacoService.deleteTacosWithoutOrder();

        return ResponseEntity.noContent().build();
    }

}




