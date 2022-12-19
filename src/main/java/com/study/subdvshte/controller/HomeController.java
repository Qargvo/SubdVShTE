package com.study.subdvshte.controller;

import com.study.subdvshte.database.DBRepository;
import com.study.subdvshte.model.mainRec;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
public class HomeController {
    private DBRepository dBrepository;
    @GetMapping(value = "/home", params = {"order", "av"})
    public List<mainRec> home(
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "av", required = false) Integer av
    ) {
        return dBrepository.getAvailability(order, av);
    }
    @GetMapping(value = "/home", params = {"order", "av", "min", "max", "search"})
    public List<mainRec> home(
            @RequestParam(name = "order", required = false) String order,
            @RequestParam(name = "av", required = false) Integer av,
            @RequestParam(name = "min", required = false) Integer min,
            @RequestParam(name = "max", required = false) Integer max,
            @RequestParam(name = "search", required = false) String search
    ){
        return dBrepository.getAvailability(order, av, min, max, search);
    }

}
