package com.example.ipucp.Controller;

import com.example.ipucp.Dao.UsuarioDao;
import com.example.ipucp.Dto.UsuarioDto;
import com.example.ipucp.EmailSenderService;
import com.example.ipucp.Entity.Icono;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;

@Controller
public class LogController {
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UsuarioDao usuarioDao;
    @Autowired
    private EmailSenderService senderService;

    @Autowired
    private OAuth2AuthorizedClientService auth2AuthorizedClientService;

    @GetMapping(value = {"/login"})
    public String login( @ModelAttribute("usuario") Usuario usuario ) {
        return "login/log-in";
    }

    @GetMapping(value = "/redirecRol")
    public String redirecRol(Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes){

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
                if(usuario.getBan() <3){
                    return "redirect:/usuario/listar";
                }else{
                    String texto = "El usuario ha sido baneado";
                    redirectAttributes.addFlashAttribute("msgLogin1",texto);
                    return "redirect:/login";
                }
            }
            case "seguridad" -> {
                return "redirect:/seguridad/";
            }
            case "admin" -> {
                return "redirect:/admin";
            }
            default -> {
                String texto = "Credenciales invalidas";
                redirectAttributes.addFlashAttribute("msgLogin",texto);
                return "redirect:/login";
            }

        }
    }
    @GetMapping("/loginGoogle")
    public String listar(Model model, OAuth2AuthenticationToken authentication, HttpSession session, RedirectAttributes redirectAttributes, HttpServletRequest request) {
        OAuth2AuthorizedClient client = auth2AuthorizedClientService.loadAuthorizedClient(authentication.getAuthorizedClientRegistrationId(), authentication.getName());
        String name = (String) authentication.getPrincipal().getAttributes().get("given_name");
        String lastname = (String) authentication.getPrincipal().getAttributes().get("family_name");
        String email = (String) authentication.getPrincipal().getAttributes().get("email");
        Usuario usuario_g = new Usuario();
        usuario_g.setNombre(name);
        usuario_g.setApellido(lastname);
        usuario_g.setCorreo(email);
        Usuario usuario = usuarioRepository.findByCorreo(email);
        if(usuario_g.getCorreo().equals(usuario.getCorreo())){
            session.setAttribute("usuario",usuario);
            String rol= String.valueOf(usuario.getRol());
            switch (rol){
                case "usuario" -> {
                    if(usuario.getBan() <3){
                        return "redirect:/usuario/listar";
                    }else{
                        String texto = "El usuario ha sido baneado";
                        redirectAttributes.addFlashAttribute("msgLogin1",texto);
                        return "redirect:/login";
                    }
                }
                case "seguridad" -> {
                    return "redirect:/seguridad";
                }
                case "admin" -> {
                    return "redirect:/admin";
                }
                default -> {
                    String texto = "Credenciales invalidas";
                    redirectAttributes.addFlashAttribute("msgLogin",texto);
                    return "redirect:/login";
                }

            }

        }else {
            String texto = "No se enecuentra";
            redirectAttributes.addFlashAttribute("msgLogin",texto);
            return "redirect:/login";
        }

    }

    @GetMapping("/reset_password")
    public String reset() {
        return "login/reset-pass";
    }

    @PostMapping("/login/registrar")
    public  String registrar(Model model, @RequestParam("id") String codigo,
                             @RequestParam("correo") String correo, @ModelAttribute("usuario") @Valid Usuario usuario,
                             BindingResult bindingResult, RedirectAttributes redirectAttributes){
        //SELECT * FROM ipucp.usuario where codigo like codigo and estado like "0";

        String codigo1 = "";
        String nombre="";
        String apellido="";
        String correo1="";
        String dni="";
        List<UsuarioDto> lista= usuarioDao.listarUsuarios();
        int i=0;
        for(UsuarioDto usuario1: lista){
            if(Objects.equals(usuario1.getCodigo(), codigo) && Objects.equals(usuario1.getCorreo(),correo)){
                System.out.println("casi ");
                codigo1 = usuario1.getCodigo();
                nombre= usuario1.getNombre();
                apellido = usuario1.getApellido();
                correo1 = usuario1.getCorreo();
                dni = usuario1.getDni();
                i=1;
                break;
                //usuarioRepository.add_db(Integer.parseInt(usuario1.getCodigo()),usuario1.getNombre(),usuario1.getApellido(),usuario1.getCorreo(),usuario1.getDni());
            }
        }

        System.out.println(codigo1);
        System.out.println(nombre);
        System.out.println(apellido);
        System.out.println(correo1);
        System.out.println(dni);
        usuario.setId(codigo1);
        usuario.setCorreo(correo1);
        usuario.setNombre(nombre);
        usuario.setApellido(apellido);
        usuario.setDni(dni);

        List<Usuario> listaUsuarios = usuarioRepository.findAll();

        int j=0;
        for(Usuario listaUsuarios1: listaUsuarios){
            if (Objects.equals(listaUsuarios1.getId(), codigo) && Objects.equals(listaUsuarios1.getCorreo(), correo)) {
                j = 1;
                break;
            }
        }
        System.out.println(i);
        System.out.println(j);
        System.out.println(usuario.getNombre()+" "+usuario.getApellido());
        if(i==1 && j!=1){
            String random_code = cadenaAleatoria(6);
            senderService.sendSimpleEmail(correo," Codigo de verificación IPUCP","Su código de verificación es " + random_code);
            model.addAttribute("random_code",random_code);
            model.addAttribute("usuario",usuario);
            return "login/random-code";
        }else{
            String texto = "Credenciales invalidas o ya existentes.";
            redirectAttributes.addFlashAttribute("msg",texto);
            return "redirect:/login";
        }
    }

    @PostMapping("/login/establecer_pass1")
    public String random_code(Model model,
                              @RequestParam("random_code") String random_code, @RequestParam("codigo_aleatorio") String codigo_aleatorio,
                              @RequestParam("id") String codigo, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido, @RequestParam("correo") String correo, @RequestParam("dni") String dni,
                              @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                              RedirectAttributes redirectAttributes){
        String codigo1 = "";
        String nombre1="";
        String apellido1="";
        String correo1="";
        String dni1="";
        if(Objects.equals(codigo_aleatorio, random_code)){
            List<UsuarioDto> lista= usuarioDao.listarUsuarios();
            for(UsuarioDto usuario1: lista){
                if(Objects.equals(usuario1.getCodigo(), codigo) && Objects.equals(usuario1.getCorreo(),correo)){
                    System.out.println("casi ");
                    codigo1 = usuario1.getCodigo();
                    nombre1= usuario1.getNombre();
                    apellido1 = usuario1.getApellido();
                    correo1 = usuario1.getCorreo();
                    dni1 = usuario1.getDni();
                    break;
                }
            }
            usuario.setId(codigo1);
            usuario.setCorreo(correo1);
            usuario.setNombre(nombre1);
            usuario.setApellido(apellido1);
            usuario.setDni(dni1);
            model.addAttribute("usuario",usuario);
            return "login/new-pass";
        }else{
            model.addAttribute("random_code",random_code);
            String texto = "Código incorrecto. Vuelva ingresar el código que se envío a su correo.";
            redirectAttributes.addFlashAttribute("msg",texto);
            return "login/random-code";
        }

    }

    @PostMapping("/login/establecer_pass")
    public  String est_pass(Model model, @RequestParam("contrasenha1") String contrasenha1,
                            @RequestParam("contrasenha2") String contrasenha2,
                            @RequestParam("id") String codigo, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido, @RequestParam("correo") String correo, @RequestParam("dni") String dni,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        if(Objects.equals(contrasenha1, contrasenha2)){
            //usuarioRepository.registrar(BCrypt.hashpw(contrasenha1, BCrypt.gensalt()), codigo);
            System.out.println(codigo);
            System.out.println(nombre);
            System.out.println(apellido);
            System.out.println(correo);
            System.out.println(dni);

            String nombre1="";
            String apellido1="";
            List<UsuarioDto> lista= usuarioDao.listarUsuarios();
            for(UsuarioDto usuario1: lista){
                if(Objects.equals(usuario1.getCodigo(), codigo) && Objects.equals(usuario1.getCorreo(),correo)){
                    System.out.println("casi ");
                    nombre1= usuario1.getNombre();
                    apellido1 = usuario1.getApellido();
                    break;
                    //usuarioRepository.add_db(Integer.parseInt(usuario1.getCodigo()),usuario1.getNombre(),usuario1.getApellido(),usuario1.getCorreo(),usuario1.getDni());
                }
            }

            usuarioRepository.add_db(codigo,nombre1,apellido1,correo,BCrypt.hashpw(contrasenha1, BCrypt.gensalt()),dni);
            String texto = "Usuario registrado.";
            redirectAttributes.addFlashAttribute("msg1",texto);
            return "redirect:/login";
        }else{
            String texto = "Las contraseñas no son iguales.";
            redirectAttributes.addFlashAttribute("msg",texto);
            return "login/new-pass";
        }
    }


    public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un carácter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con límite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }
}
