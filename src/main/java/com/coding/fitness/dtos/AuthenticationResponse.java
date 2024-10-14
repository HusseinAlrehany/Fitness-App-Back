package com.coding.fitness.dtos;

import com.coding.fitness.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwtToken;
    private Long userId;
    private UserRole userRole;
}
