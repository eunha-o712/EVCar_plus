package com.evcar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/main")
public class MainController {

    // /main , /main/ 둘 다 처리
    @GetMapping({"", "/"})
    public String main() {
        return "main/index";
    }

    // /main/index
    @GetMapping("/index")
    public String index() {
        return "main/index";
    }
}