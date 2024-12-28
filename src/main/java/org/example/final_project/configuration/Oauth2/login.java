package org.example.final_project.configuration.Oauth2;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class login {
    @GetMapping("/longin")
    public String longin() {
        return "login";
    }
}
