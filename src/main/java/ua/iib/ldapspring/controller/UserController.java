package ua.iib.ldapspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final ActiveDirectoryService adService;

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", adService.getAllUsers());
        return "users";
    }
}
