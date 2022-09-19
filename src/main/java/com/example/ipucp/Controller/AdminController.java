package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Rol;
import com.example.ipucp.Entity.Tipo;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.CargoRepository;
import com.example.ipucp.Repository.TipoRepository;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TipoRepository tipoRepository;

    @GetMapping(value = {"/listar",""})
    public String listar(Model model) {
        model.addAttribute("listaUsuarios",usuarioRepository.findAll());
        return "admin/listar";
    }

    @GetMapping("/nuevoSeguridad")
    public String nuevoSeguridad(Model model) {
        model.addAttribute("tipoUsuario","seguridad");
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/newForm";
    }

    @GetMapping("/nuevoNormal")
    public String nuevoNormal(Model model) {
        model.addAttribute("tipoUsuario","normal");
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/newForm";
    }

    @PostMapping("/save")
    public String guardarUsuario(Usuario usuario,@RequestParam("cargo") Integer id, RedirectAttributes attr) {
        //usuario.getCargo().setId(id);
        usuario.setRol(new Rol());
        if(id == 6){
            usuario.getRol().setId(2); //seguridad
        }else {
            usuario.getRol().setId(1); //usuario
        }
        usuarioRepository.save(usuario);
        return "redirect:/admin";
    }

    @GetMapping("/incidencias")
    public String incidencias(Model model) {
        model.addAttribute("listaIncidencias",tipoRepository.findAll());
        return "admin/incidencias";
    }

    @GetMapping("/habilitar")
    public String habilitarUsuario(@RequestParam("id") String codigo){
        usuarioRepository.habilitarUsuario(codigo);
        return "redirect:/admin";
    }

    @PostMapping("/suspender")
    public String suspenderUsuario(@RequestParam("id") String codigo, @RequestParam("justificacion") String justificacion){
        usuarioRepository.suspenderUsuario(justificacion,codigo);
        return "redirect:/admin";
    }

    @PostMapping("/saveIncident")
    public String guardarTipoIncidencia(Tipo tipo, RedirectAttributes attr) {
        tipoRepository.save(tipo);
        return "redirect:/admin/incidencias";
    }

    @GetMapping("/deleteIncident")
    public String borrarIncidencias(@RequestParam("id") int id) {
        Optional<Tipo> opTipo = tipoRepository.findById(id);

        if(opTipo.isPresent()){
            tipoRepository.deleteById(id);
        }

        return "redirect:/admin/incidencias";
    }

}
