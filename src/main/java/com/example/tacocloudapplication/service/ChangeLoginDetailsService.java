package com.example.tacocloudapplication.service;

import com.example.tacocloudapplication.DTO.ChangePasswordDetailsDTO;
import com.example.tacocloudapplication.DTO.ChangeUsernameDetailsDTO;
import com.example.tacocloudapplication.repo.impl.UserRepository;
import com.example.tacocloudapplication.table.User;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class ChangeLoginDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public ChangeLoginDetailsService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public int changeUsername(ChangeUsernameDetailsDTO changeUsernameDetailsDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String loggedInUserUsername = "";
        if(principal instanceof UserDetails) {
            loggedInUserUsername = ((UserDetails) principal).getUsername();
        }

        if(loggedInUserUsername.equals(changeUsernameDetailsDTO.getOldUsername())) {
            return userRepository.updateUsername(changeUsernameDetailsDTO.getOldUsername(), changeUsernameDetailsDTO.getNewUsername());
        }

        return 0;
    }

    public int changePassword(ChangePasswordDetailsDTO changePasswordDetailsDTO) {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        String loggedInUserUsername = null;
        if(principal instanceof UserDetails) {
            loggedInUserUsername = ((UserDetails) principal).getUsername();
        }

        String currentPassword= userRepository.getCurrentPassword(loggedInUserUsername);

        if(passwordEncoder.matches(changePasswordDetailsDTO.getOldPassword(), currentPassword)) {
            return userRepository.updatePassword(loggedInUserUsername, passwordEncoder.encode(changePasswordDetailsDTO.getNewPassword()));
        }

        return 0;
    }
}
