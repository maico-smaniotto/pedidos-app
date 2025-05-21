package com.maicosmaniotto.pedidos_api.controller;

import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/api/hello")
public class HelloController {
   
    @GetMapping
    // @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public String hello() {
        return "Hello, World!";
    }
}
