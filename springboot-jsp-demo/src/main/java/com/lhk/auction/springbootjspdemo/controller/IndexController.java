package com.lhk.auction.springbootjspdemo.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author dalaoyang
 * @project springboot_learn
 * @package com.dalaoyang.controller
 * @email yangyang@dalaoyang.cn
 * @date 2018/8/13
 */
@Controller
public class IndexController {

    @GetMapping("/")
    public String index(Model model){
        System.out.println("ddddddddddddd");
        model.addAttribute("name", "dalaoyang");
        return "index";
    }

}

