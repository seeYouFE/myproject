package com.test.myproject.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class SettingController {

    @RequestMapping(path = {"/setting"})
    @ResponseBody
    public String setting() {

        return "Setting is OK";
    }
}
