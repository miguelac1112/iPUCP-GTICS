package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
@Controller
public class LogController {
    @Autowired
    UsuarioRepository usuarioRepository;

    @GetMapping(value = {"/login"})
    public String login( @ModelAttribute("usuario") Usuario usuario ) {
        return "login/log-in";
    }

    @GetMapping(value = "/redirecRol")
    public String redirecRol(Authentication authentication, HttpSession session){

        String rol="";
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) authentication.getAuthorities();
        for (GrantedAuthority grantedAuthority: authorities){
            System.out.println(grantedAuthority.getAuthority());
            rol= grantedAuthority.getAuthority();
        }

        String username= authentication.getName();
        Usuario usuario=usuarioRepository.findByCorreo(username);
        session.setAttribute("usuario",usuario);

        switch (rol){
            case "usuario" -> {
                return "redirect:/usuario/listar";
            }
            case "seguridad" -> {
                return "redirect:/seguridad";
            }
            case "admin" -> {
                return "redirect:/admin";
            }
            default -> {
                return "redirect:/login";
            }

        }
    }

    @GetMapping("/reset_password")
    public String reset() {
        return "login/reset-pass";
    }

    @PostMapping("/registrar")
    public  String registrar(Model model, @RequestParam("id") String codigo,
                             @RequestParam("correo") String correo, @ModelAttribute("usuario") @Valid Usuario usuario,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes){
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
            String texto = "Credenciales invalidas o existentes.";
            redirectAttributes.addFlashAttribute("msg",texto);
            return "redirect:/login/log-in";
        }
    }

    @PostMapping("/establecer_pass")
    public  String est_pass(Model model, @RequestParam("contrasenha1") String contrasenha1,
                            @RequestParam("contrasenha2") String contrasenha2, @RequestParam("id") String codigo,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        if(Objects.equals(contrasenha1, contrasenha2)){
            usuarioRepository.registrar(contrasenha1, codigo);
            String texto = "Usuario registrado.";
            redirectAttributes.addFlashAttribute("msg1",texto);
            return "redirect:/login/log-in";
        }else{
            String texto = "Las contraseñas no son iguales.";
            redirectAttributes.addFlashAttribute("msg",texto);
            return "login/new-pass";
        }
    }
}
