package com.blockFileExtension.controller;

import net.sf.json.JSONSerializer;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ViewController {

    @RequestMapping(value = "/main")
    public String main(Model model){
        // main page

        return "main";
    }
}
