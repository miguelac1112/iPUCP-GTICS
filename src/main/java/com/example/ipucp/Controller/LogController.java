package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/login")
public class LogController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"", "/","/log-in"})
    public String login( @ModelAttribute("usuario") Usuario usuario ) {
        usuarioRepository.listarUsuarios();
        return "login/log-in";
    }

    @GetMapping("/reset_password")
    public String reset() {
        return "login/reset-pass";
    }
    @PostMapping("/registrar")
    public  String registrar(Model model, @RequestParam("id") String codigo){
        //SELECT * FROM ipucp.usuario where codigo like codigo and estado like "0";

        usuarioRepository.registrar(codigo);
        //usuarioRepository.registrar(correo);

        return "redirect:/login";
    }
    @PostMapping("/establecer_pass")
    public  String est_pass(Model model, @RequestParam("correo") String correo){
        //SELECT * FROM ipucp.usuario where codigo like codigo and estado like "0";


        return "login/reset-pass";
    }

}
