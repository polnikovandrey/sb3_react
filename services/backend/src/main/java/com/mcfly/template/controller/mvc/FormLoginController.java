package com.mcfly.template.controller.mvc;

import com.mcfly.template.payload.ws.TextMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping
public class FormLoginController {

    @Autowired
    private SimpMessagingTemplate wsTemplate;

    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/me")
    public String me(@RequestParam Integer id) {
        wsTemplate.convertAndSend(String.format("/topic/emailConfirmed/%s", id.toString()),
                                  new TextMessage("Email validation user message. Email confirmed: 123"));     // TODO del
        return "redirect:/login";
    }
}
