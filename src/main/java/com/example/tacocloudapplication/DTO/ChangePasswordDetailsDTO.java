package com.example.tacocloudapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangePasswordDetailsDTO {
    private String oldPassword;

    private String newPassword;
}
