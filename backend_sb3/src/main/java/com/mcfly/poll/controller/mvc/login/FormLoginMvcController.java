package com.mcfly.poll.controller.mvc.login;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class FormLoginMvcController {

    @GetMapping({"/", "/login"})
    public String login() {
        return "login";
    }
}
