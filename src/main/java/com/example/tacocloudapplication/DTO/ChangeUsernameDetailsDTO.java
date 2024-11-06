package com.example.tacocloudapplication.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class ChangeUsernameDetailsDTO {
    private String oldUsername;

    private String newUsername;
}
