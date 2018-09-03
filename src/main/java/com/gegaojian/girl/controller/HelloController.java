package com.gegaojian.girl.controller;

import com.gegaojian.girl.properties.GirlProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class HelloController {
    @Autowired
    private GirlProperties girlProperties;

    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    public String say(@RequestParam(name = "id", required = false, defaultValue = "0") int id){
        return "Hello, " + girlProperties.getCupSize() + " cupSize and " + girlProperties.getAge() + " years old girl.";
    }
}
