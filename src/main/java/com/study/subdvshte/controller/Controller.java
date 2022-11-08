package com.study.subdvshte.controller;

import com.study.subdvshte.database.DBRepository;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    private DBRepository dBrepository;
    @GetMapping("/products")
    public String products(@RequestParam(name = "id",required = true) Integer id, Model model) {
        model.addAllAttributes(dBrepository.getProduct(id));
        model.addAttribute("points", dBrepository.getPoints(id));
        return "product_view";
    }
    @GetMapping("/home")
    public String home(Model model) {
        model.addAttribute("avlist", dBrepository.getAvailability());
        return "home";
    }
}
