package demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class AuthenticationController {

    @RequestMapping(value = {"/login"})
    public String login() {
        return "login";
    }
}
