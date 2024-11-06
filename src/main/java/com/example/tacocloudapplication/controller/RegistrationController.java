package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.repo.impl.UserRepository;
import com.example.tacocloudapplication.table.User;
import com.example.tacocloudapplication.table.util.RegistrationForm;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/register")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class RegistrationController {

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    public RegistrationController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> processRegustration(@RequestBody RegistrationForm form) {
        try {
            System.out.println(form);
            User savedUser = userRepository.save(form.toUser(passwordEncoder));

            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>("Registration failed", HttpStatus.BAD_REQUEST);
        }
    }
}
