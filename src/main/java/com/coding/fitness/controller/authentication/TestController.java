package com.coding.fitness.controller.authentication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TestController {


    @GetMapping("/adminonly")
    public String onlyAdmin(){
        return "Authenticated users only can access this";
    }
}
