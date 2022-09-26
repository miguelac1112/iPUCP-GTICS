package com.example.ipucp.Controller;

import com.example.ipucp.Dto.UsuarioIncidencias;
import com.example.ipucp.Entity.Inicidencia;
import com.example.ipucp.Entity.Tipo;
import com.example.ipucp.Entity.Urgencia;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    InicidenciaRepository inicidenciaRepository;
    @Autowired
    TipoRepository tipoRepository;
    @Autowired
    UrgenciaRepository urgenciaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping("/mapa")
    public String mapa() {
        return "usuario/menu_mapa";
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        List<Inicidencia> lista  =inicidenciaRepository.orderReciente();
        model.addAttribute("incidenciaList", lista);

        return "usuario/menu";
    }

    @PostMapping("/listarFiltrado")
    public String listarFiltrado(Model model, @RequestParam("orden") int form) {
        List<Inicidencia> listIncidencias = new ArrayList<>();

        if (form == 2){
            List<Inicidencia> lista  =inicidenciaRepository.orderMaspopular();
            model.addAttribute("incidenciaList", lista);
        }
        else{
            List<Inicidencia> lista  =inicidenciaRepository.orderReciente();
            model.addAttribute("incidenciaList", lista);
        }

        return "usuario/menu";
    }

    @GetMapping("/destacar")
    public String destacarIncidencia(@RequestParam("id") Integer id) {
        inicidenciaRepository.destacarIncidencia(id);
        return "redirect:/usuario/listar";
    }


    @PostMapping("/save")
    public String guardarNuevaIncidencia(Inicidencia inicidencia, RedirectAttributes attr) {
        inicidenciaRepository.save(inicidencia);
        return "redirect:/usuario/listar";
    }

    @GetMapping("/detalle")
    public String detalleIncidencia(Model model,
                                    @RequestParam("id") Integer id){
        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);


        if (optInicidencia.isPresent()){
            Inicidencia inicidencia = optInicidencia.get();

            model.addAttribute("incidencia", inicidencia);

            return "usuario/detalleIncid";
        }else{
            return "redirect:/usuario/misIncidencias";
        }
    }

    @GetMapping("/newInciden")
    public String newInciden(Model model) {
        List<Tipo> listaTipo  =tipoRepository.findAll();
        List<Urgencia> listaUrgencia  =urgenciaRepository.findAll();
        model.addAttribute("listaTipo", listaTipo);
        model.addAttribute("listaUrgencia", listaUrgencia);
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
