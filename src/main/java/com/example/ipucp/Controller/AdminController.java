package com.example.ipucp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/listar")
    public String listar() {
        return "admin/listar";
    }

    @GetMapping("/nuevo")
    public String nuevo() {
        return "admin/newFrm";
    }

    @GetMapping("/incidencias")
    public String incidencias() {
        return "admin/newForm2";
    }
}
