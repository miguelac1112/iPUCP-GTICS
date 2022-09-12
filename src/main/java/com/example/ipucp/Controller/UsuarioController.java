package com.example.ipucp.Controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @GetMapping("/listar")
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


}
