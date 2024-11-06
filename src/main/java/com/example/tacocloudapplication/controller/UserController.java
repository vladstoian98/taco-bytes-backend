package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.service.UserDetailsService;
import com.example.tacocloudapplication.table.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/admin/users")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class UserController {

    private final UserDetailsService userDetailsService;

    public UserController(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/{username}")
    public ResponseEntity<Map<String, User>> getUserByUsername(@PathVariable String username) {
        User selectedUser = userDetailsService.getUserByUsername(username);
    
        if(selectedUser != null) {
            return ResponseEntity.ok(Collections.singletonMap("user", selectedUser));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<Map<String, Integer>> deleteUserByUsername(@PathVariable String username) {
        Integer numberOfDeletedRows = userDetailsService.deleteUserByUsername(username);

        if(numberOfDeletedRows != 0) {
            return ResponseEntity.ok(Collections.singletonMap("numberOfDeletedRows", numberOfDeletedRows));
        }

        return ResponseEntity.notFound().build();
    }
}
