package com.example.ipucp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/seguridad")
public class SeguridadController {

    @GetMapping("/comentar_incidencia")
    public String probando() {
        return "seguridad/seguridad";
    }


    @GetMapping("/incidencias")
    public String lista() {
        return "seguridad/incidencias";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "seguridad/dashboard";
    }

    @GetMapping("/usuarior")
    public String probando2() {
        return "seguridad/seguridad_reportar";
    }


    @GetMapping("/detalleid")
    public String probando4() {
        return "seguridad/detalleid_seguridad";
    }

    @GetMapping("/lista")
    public String probando3() {
        return "seguridad/lista_usuarios";
    }
}
