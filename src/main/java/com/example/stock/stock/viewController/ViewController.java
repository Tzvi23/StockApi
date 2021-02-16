package com.example.stock.stock.viewController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ViewController {

    @RequestMapping("/")
    public String index(){
        return "index";
    }
}
