package com.groupware.controller.mvc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/")
public class MainMvcController {

    private String commonView(Model model, String view, boolean authorityFlag) {
        if (authorityFlag) {
            try {
                Authentication currentUser = (Authentication) SecurityContextHolder.getContext();
                model.addAttribute("empNo", currentUser.getName());
            } catch (Exception e) {
                return "redirect:page-401";
            }
        }
        return view;
    }

    @RequestMapping("/web/home")
    public String index(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("DXD");
        System.out.println(authentication.getAuthorities());
        model.addAttribute("path", "home");
        model.addAttribute("title", "Index");
        return "/application";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        return "/component/sample/sign-in";
    }
}
