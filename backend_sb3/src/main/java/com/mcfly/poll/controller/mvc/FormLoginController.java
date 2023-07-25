package com.mcfly.poll.controller.mvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FormLoginController {

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }
}
