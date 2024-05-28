package io.h2o.ufc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class MatchController {

    @GetMapping("/match/update/{id}")
    public String viewHomePage(@PathVariable("id") int id, Model model) {
        //model.addAttribute("allemplist", null);
        System.err.println("id for match update:: "+id);
        return "tournament";
    }
}
