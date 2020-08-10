package com.funtree.defocus.controller;

import com.funtree.defocus.entity.SystemConfig;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/system")
public class SystemController {

    @GetMapping("/config")
    public SystemConfig getSysCfg() {
        return new SystemConfig();
    }
}
