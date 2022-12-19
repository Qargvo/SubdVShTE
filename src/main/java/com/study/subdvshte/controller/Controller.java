package com.study.subdvshte.controller;

import com.study.subdvshte.database.DBRepository;
import com.study.subdvshte.exceptions.ControllerException;
import com.study.subdvshte.exceptions.NotFoundException;
import com.study.subdvshte.model.mainRec;
import lombok.AllArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {
    private DBRepository dBrepository;

    @GetMapping("/products")
    public String products(@RequestParam(name = "id", required = true) Integer id, Model model) throws NotFoundException {
        if (dBrepository.checkProduct(id)) throw new NotFoundException();
        model.addAllAttributes(dBrepository.getProduct(id));
        model.addAttribute("points", dBrepository.getPoints(id));
        return "product_view";
    }
    @GetMapping("/home")
    public String home() {
        return "home";
    }
    @GetMapping("/buy")
    public String buy(@RequestParam(name = "id", required = true) Integer id, Model model) {
        model.addAttribute("id", id);
        model.addAttribute("points", dBrepository.getPoints(id));
        return "buy_form";
    }

    @PostMapping("/order")
    public String order(@RequestParam(name = "id", required = true) Integer id, @RequestParam(name = "point", required = true) Integer point, Model model) {
        dBrepository.order(id, point);
        model.addAllAttributes(dBrepository.getProduct(id));
        model.addAttribute("points", dBrepository.getPoints(id));
        return "product_view";
    }

    @GetMapping("/secret")
    public String index(Model model) {
        model.addAttribute("products", dBrepository.getAllIdProducts());
        model.addAttribute("points", dBrepository.getAllIdPoints());
        return "index";
    }

    @PostMapping("/secret")
    public String updateCountProduct(@RequestParam(name = "product_id", required = true) Integer id, @RequestParam(name = "point_id", required = true) Integer point, @RequestParam(name = "cnt", required = true) Integer cnt, Model model) {
        dBrepository.updateCountAv(id, point, cnt);
        return "result";
    }
}
