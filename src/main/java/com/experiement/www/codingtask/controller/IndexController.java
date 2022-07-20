package com.experiement.www.codingtask.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
    
@RestController
public class IndexController {
    @GetMapping("/")
    public String Index() {
        return "<H1>welcome to this coding task</H1>";
    }
}