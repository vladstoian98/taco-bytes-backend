package com.example.tacocloudapplication.controller;

import com.example.tacocloudapplication.DTO.ChangePasswordDetailsDTO;
import com.example.tacocloudapplication.DTO.ChangeUsernameDetailsDTO;
import com.example.tacocloudapplication.service.ChangeLoginDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/change")
//@CrossOrigin(origins = {"https://localhost:4200", "http://3.79.153.161"})
public class ChangeLoginDetailsController {
    private final ChangeLoginDetailsService changeLoginDetailsService;

    public ChangeLoginDetailsController(ChangeLoginDetailsService changeLoginDetailsService) {
        this.changeLoginDetailsService = changeLoginDetailsService;
    }

    @PatchMapping("/username")
    public ResponseEntity<Map<String, String>> changeUsername(@RequestBody ChangeUsernameDetailsDTO changeUsernameDetails) {
        int numberOfChangedRows = changeLoginDetailsService.changeUsername(changeUsernameDetails);

        if(numberOfChangedRows != 0) {
            return ResponseEntity.ok(Collections.singletonMap("message", "The username has been updated."));
        }

        return ResponseEntity.notFound().build();
    }

    @PatchMapping("/password")
    public ResponseEntity<Map<String, String>> changePassword(@RequestBody ChangePasswordDetailsDTO changePasswordDetailsDTO) {
        int numberOfChangedRows = changeLoginDetailsService.changePassword(changePasswordDetailsDTO);

        if(numberOfChangedRows != 0) {
            return ResponseEntity.ok(Collections.singletonMap("message", "The password has been updated."));
        }

        return ResponseEntity.notFound().build();
    }
}
