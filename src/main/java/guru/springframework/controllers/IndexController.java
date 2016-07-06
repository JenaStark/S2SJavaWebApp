package guru.springframework.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {
    @RequestMapping("/")
    String index(){
        return "index";
    }

    @RequestMapping("/index.html")
    String indexhtml() {
        return "index";
    }

    @RequestMapping("/about")
    String about() {
        return "about";
    }

    @RequestMapping("/data")
    String data() {
        return "data";
    }
}
