package br.com.gudaites.caronaescola.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Class Controller Admin do Spring
 * @author Jair Gudaites Junior
 */

@Controller
public class AdminController {

    @GetMapping( "/")
    public String index(){
        return "index";
    }

}
