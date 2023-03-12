package com.example.dripchip.controllers;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountController {

    @PostMapping(value = "/b")
    public String getAccount(){
        return "b";
    }


    @PostMapping(value = "/a")
    public String getAccount2(){
        return "a";
    }

}
