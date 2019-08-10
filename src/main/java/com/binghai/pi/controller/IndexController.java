package com.binghai.pi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author huaishuo
 * @date 2019/8/10 下午6:22
 **/
@Controller
@RequestMapping("/")
public class IndexController {

    @RequestMapping("index")
    public String index(){
        return "index";
    }

    @RequestMapping("addNature")
    public String addNature(){
        return "addNature";
    }
}
