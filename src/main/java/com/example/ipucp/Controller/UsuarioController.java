package com.example.ipucp.Controller;

import com.example.ipucp.Dto.UsuarioIncidencias;
import com.example.ipucp.Entity.Inicidencia;
import com.example.ipucp.Entity.Tipo;
import com.example.ipucp.Entity.Urgencia;
import com.example.ipucp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    InicidenciaRepository inicidenciaRepository;
    TipoRepository tipoRepository;
    UrgenciaRepository urgenciaRepository;
    UsuarioRepository usuarioRepository;

    @GetMapping("/mapa")
    public String mapa() {
        return "usuario/menu_mapa";
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        List<Inicidencia> lista  =inicidenciaRepository.findAll();
        model.addAttribute("incidenciaList", lista);

        return "usuario/menu";
    }


    @PostMapping("/save")
    public String guardarNuevaIncidencia(Inicidencia inicidencia, RedirectAttributes attr) {
        inicidenciaRepository.save(inicidencia);
        return "redirect:/usuario/listar";
    }

    @GetMapping("/detalle")
    public String detalle() {
        return "usuario/detalleIncid";
    }

    @GetMapping("/newInciden")
    public String newInciden(Model model) {

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
