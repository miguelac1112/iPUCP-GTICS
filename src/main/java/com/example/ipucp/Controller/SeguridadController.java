package com.example.ipucp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seguridad")
public class SeguridadController {

    @GetMapping("/listar")
    public String probando() {
        return "seguridad/seguridad";
    }


    @GetMapping("/incidencias")
    public String lista() {
        return "seguridad/incidencias";
    }
}
