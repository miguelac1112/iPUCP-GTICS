package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Inicidencia;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Objects;

@Controller
@RequestMapping("/login")
public class LogController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"", "/","/log-in"})
    public String login( @ModelAttribute("usuario") Usuario usuario ) {

        return "login/log-in";
    }

    @GetMapping("/reset_password")
    public String reset() {
        return "login/reset-pass";
    }

    @PostMapping("/registrar")
    public  String registrar(Model model, @RequestParam("id") String codigo,
                             @RequestParam("correo") String correo, @ModelAttribute("usuario") @Valid Usuario usuario,
                             BindingResult bindingResult){
        //SELECT * FROM ipucp.usuario where codigo like codigo and estado like "0";

        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        int i=0;
        for(Usuario listaUsuarios1: listaUsuarios){
            if (Objects.equals(listaUsuarios1.getId(), codigo) && Objects.equals(listaUsuarios1.getCorreo(), correo) &&
                    listaUsuarios1.getEstado() == 0) {
                i = 1;
                break;
            }
        }
        System.out.println(i);
        usuario.setId(codigo);
        usuario.setCorreo(correo);
        if(i==1){
            model.addAttribute("usuario",usuario);
            return "login/new-pass";
        }else{
            return "redirect:/login/log-in";
        }
    }
    @PostMapping("/establecer_pass")
    public  String est_pass(Model model, @RequestParam("contrasenha1") String contrasenha1,
                            @RequestParam("contrasenha2") String contrasenha2, @RequestParam("id") String codigo,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult){

        if(Objects.equals(contrasenha1, contrasenha2)){
            usuarioRepository.registrar(contrasenha1, codigo);
            return "redirect:/login/log-in";
        }else{
            return "login/new-pass";
        }
    }

}
