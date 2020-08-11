package com.funtree.defocus.app.controller;

import com.funtree.defocus.app.entity.SystemConfig;
import org.springframework.web.bind.annotation.GetMapping;
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
