package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Rol;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.CargoRepository;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

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
    public String incidencias() {
        return "admin/incidencias";
    }
}
