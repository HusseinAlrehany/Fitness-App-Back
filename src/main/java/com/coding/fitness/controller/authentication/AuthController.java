package com.coding.fitness.controller.authentication;

import com.coding.fitness.dtos.AuthenticationResponse;
import com.coding.fitness.dtos.SignUpRequest;
import com.coding.fitness.dtos.SigninRequest;
import com.coding.fitness.dtos.UserDTO;
import com.coding.fitness.entity.User;
import com.coding.fitness.repository.UserRepository;
import com.coding.fitness.tokens.jwtservice.AppUserDetailsService;
import com.coding.fitness.tokens.jwtservice.AuthService;
import com.coding.fitness.tokens.utils.JwtUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import io.jsonwebtoken.io.IOException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
//@CrossOrigin("*")
public class AuthController {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AppUserDetailsService appUserDetailsService;
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private AuthService authService;



   @PostMapping("/signup")
   public ResponseEntity<?> signUp(@RequestBody SignUpRequest signUpRequest){

       if(authService.hasUserWithEmail(signUpRequest.getEmail())){
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                    .body("User Already Exists With This Email");
       }
            UserDTO createdUser = authService.signUp(signUpRequest);
        if(createdUser == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("User Not Created");
        }

       return ResponseEntity.status(HttpStatus.CREATED)
               .body(createdUser);
   }


    @PostMapping("/signin")
    public AuthenticationResponse signIn(@RequestBody SigninRequest signinRequest)  {
         try{
              authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                      signinRequest.getEmail(),
                      signinRequest.getPassword()
              ));
         }
         catch(BadCredentialsException bce){
             throw new BadCredentialsException("Invalid username or password");
         }

         final UserDetails userDetails = appUserDetailsService.loadUserByUsername(
                 signinRequest.getEmail()
         );

         Optional<User> optionalUser = userRepository.findFirstByEmail(signinRequest.getEmail());
         final String jwtToken = jwtUtils.generateToken(userDetails);
          AuthenticationResponse response = new AuthenticationResponse();
          if(optionalUser.isPresent()){
             response.setJwtToken(jwtToken);
              response.setUserId(optionalUser.get().getId());
              response.setUserRole(optionalUser.get().getRole());


          }
          return  response;
    }


}
