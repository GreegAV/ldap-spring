package ua.iib.ldapspring.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ua.iib.ldapspring.service.LdapUserService;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class UserController {

    private final LdapUserService adService;

    @GetMapping
    public String showUsers(Model model) {
        model.addAttribute("users", adService.getAllActiveUsersFromAD());
        return "users-list";
    }
}
