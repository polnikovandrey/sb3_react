package com.mcfly.poll.controller.mvc.admin;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class LoginController {

    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("message", "Hello!");        // TODO
        return "login";
    }
}
