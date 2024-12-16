package com.coding.fitness.tokens.jwtservice;
import com.coding.fitness.dtos.SignUpRequest;
import com.coding.fitness.dtos.UserDTO;

public interface AuthService {


    UserDTO signUp(SignUpRequest signUpRequest);
    boolean hasUserWithEmail(String email);
}
