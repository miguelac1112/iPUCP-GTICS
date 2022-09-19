package com.example.ipucp.Controller;

import com.example.ipucp.Repository.InicidenciaRepository;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    InicidenciaRepository inicidenciaRepository;
    @GetMapping("/mapa")
    public String mapa() {
        return "usuario/menu_mapa";
    }

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

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("userperfil",usuarioRepository.userPerfil("20197171"));
        return "usuario/perfil";
    }
    @GetMapping("/misIncidencias")
    public String misIncidencias(Model model) {
        model.addAttribute("listaIncidencias",inicidenciaRepository.userIncidencias("20197171"));
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
