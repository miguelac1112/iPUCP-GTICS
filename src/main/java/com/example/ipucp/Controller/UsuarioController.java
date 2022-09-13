package com.example.ipucp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @GetMapping("/mapa")
    public String mapa() {
        return "usuario/menu_mapa";
    }

    @GetMapping("/menu")
    public String listar() {
        return "usuario/menu";
    }

    @GetMapping("/detalle")
    public String detalle() {
        return "usuario/detalleIncid";
    }

    @GetMapping("/newInciden")
    public String newInciden() {
        return "usuario/newIncidencia";
    }

    @GetMapping("/perfil")
    public String perfil() {
        return "usuario/perfil";
    }
    @GetMapping("/misIncidencias")
    public String misIncidencias() {
        return "usuario/incidencias";
    }

    @GetMapping("/login")
    public String login() {
        return "usuario/log-in";
    }

    @GetMapping("/reset_password")
    public String reset() {
        return "usuario/reset-pass";
    }

}
