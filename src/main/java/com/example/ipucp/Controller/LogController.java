package com.example.ipucp.Controller;

import com.example.ipucp.Dao.UsuarioDao;
import com.example.ipucp.Dto.UsuarioDto;
import com.example.ipucp.EmailSenderService;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.crypto.bcrypt.BCrypt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        System.out.println("estoy en login");
        return "login/log-in";
    }

    @GetMapping(value = "/redirecRol")
    public String redirecRol(Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println("entre a redirec");
        System.out.println("auth nombre/correo " +authentication.getName());
        String correo = authentication.getName();
        Optional<Usuario> optionalUsuario = Optional.ofNullable(usuarioRepository.findByCorreo(correo));

        if(optionalUsuario.isPresent()){
            Usuario usuario = optionalUsuario.get();
            System.out.println(usuario.getCorreo()+" "+usuario.getRol().getNombreRol());
            String rol = usuario.getRol().getNombreRol();
            session.setAttribute("usuario",usuario);
            switch (rol){
                case "usuario" -> {
                    if(usuario.getBan() != 1){
                        return "redirect:/usuario/listar?index=0";
                    }else{
                        String texto = "El usuario ha sido baneado";
                        redirectAttributes.addFlashAttribute("msgLogin_ban",texto);
                        return "redirect:/login";
                    }
                }
                case "seguridad" -> {
                    return "redirect:/seguridad/";
                }
                case"seguridadSinInicio"->{
                    return "redirect:/seguridad/establecer";
                }
                case "admin" -> {
                    return "redirect:/admin";
                }
                default -> {
                    String texto = "Usuario no existente o credenciales incorrectas";
                    redirectAttributes.addFlashAttribute("msgLoginGoogle",texto);
                    return "redirect:/login";
                }

            }
        }else{
            return "redirect:/login";
        }
    }

    @GetMapping(value = "/redirecRolGoogle")
    public String redirecRolGoogle(Authentication authentication, HttpSession session, RedirectAttributes redirectAttributes){
        System.out.println("entre a redirec");
        System.out.println("auth nombre " +authentication.getName());
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(authentication.getName());
        if(optionalUsuario.isPresent()){
            Usuario usuario = optionalUsuario.get();
            System.out.println(usuario.getCorreo()+" "+usuario.getRol().getNombreRol());
            String rol = usuario.getRol().getNombreRol();
            session.setAttribute("usuario",usuario);
            switch (rol){
                case "usuario" -> {
                    if(usuario.getBan() <3){
                        return "redirect:/usuario/listar?index=0";
                    }else{
                        String texto = "El usuario ha sido baneado";
                        redirectAttributes.addFlashAttribute("msgLogin_ban",texto);
                        return "redirect:/login";
                    }
                }
                default -> {
                    String texto = "Usuario no existente o rol no designado";
                    redirectAttributes.addFlashAttribute("msgLoginGoogle",texto);
                    return "redirect:/login";
                }

            }
        }else{
            return "redirect:/login";
        }
    }
    /*@GetMapping("/loginGoogle")
    public String loginGoogle(OAuth2AuthenticationToken authentication,HttpSession session,RedirectAttributes redirectAttributes,@RequestParam String email) {

        System.out.println("llegue siuuuu");
        System.out.println(email);
        authentication.setAuthenticated(true);


        //authentication.getPrincipal().getAttributes().get("email");
        Usuario usuario = usuarioRepository.findByCorreo(email);
        System.out.println(usuario.getNombre());

        System.out.println("el rol es:" +usuario.getRol().getNombreRol());
        //session.setAttribute("usuario", usuario);

        if(usuario==null){
            return "login/log-in";

        }else {
            session.setAttribute("usuario",usuario);
            System.out.println(usuario.getCorreo());
            return "redirect:/redirecRol";
        }


    }/*

    /*@GetMapping("/loginGoogle")
    public String loginGoogle(OAuth2AuthenticationToken authentication,HttpSession session,RedirectAttributes redirectAttributes) {

        System.out.println("llegue siuuuu");
        String email= (String) authentication.getPrincipal().getAttributes().get("email");

        //authentication.getPrincipal().getAttributes().get("email");
        Usuario usuario = usuarioRepository.findByCorreo(email);
        System.out.println(usuario.getNombre());

        System.out.println("el rol es:" +usuario.getRol().getNombreRol());
        //session.setAttribute("usuario", usuario);

        if(usuario==null){
            return "login/log-in";

        }else {
            session.setAttribute("usuario",usuario);
            System.out.println(usuario.getCorreo());
            return "redirect:/redirecRol";
        }


    }*/

    /*@GetMapping("/loginGoogle")
    public String loginGoogle(HttpSession session, RedirectAttributes redirectAttributes, @RequestParam List<String> cred) {

        System.out.println("llegue siuuuu");
        System.out.println(cred);
        System.out.println(cred.get(0));
        System.out.println(cred.get(1));


        //client=auth2AuthorizedClientService.loadAuthorizedClient(cred.get(0),cred.get(1));


        Usuario usuario= usuarioRepository.findByCorreo(cred.get(2));



        System.out.println(usuario.getNombre());
        System.out.println(usuario.getContra());

        System.out.println("el rol es:" +usuario.getRol().getNombreRol());


        String rol = usuario.getRol().getNombreRol();


        if(usuario==null){
            return "login/log-in";

        }else {
            System.out.println("existe");
            session.setAttribute("usuario",usuario);

            switch (rol){
                case "usuario" -> {
                    if(usuario.getBan() <3){
                        System.out.println("casi");
                        return "redirect:/usuario/listar";
                    }else{
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
                    return "redirect:/login";
                }

            }
        }

    }*/
    @GetMapping("/loginGoogle")
    public String loginGoogle(@RequestParam List<String> cred, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        System.out.println("entre");
        System.out.println(cred);
        System.out.println(cred.get(0));
        System.out.println(cred.get(1));
        System.out.println(cred.get(2));
        Usuario usuario= usuarioRepository.findByCorreo(cred.get(2));
        if(usuario==null){
            String texto = "Usuario no existente o rol no designado";
            redirectAttributes.addFlashAttribute("msgLoginGoogle",texto);
            return "redirect:/login";

        }else {
            Authentication authentication = new UsernamePasswordAuthenticationToken(usuario.getId(),null, AuthorityUtils.createAuthorityList(usuario.getRol().getNombreRol()));
            SecurityContext securityContext = SecurityContextHolder.getContext();
            securityContext.setAuthentication(authentication);

            HttpSession session = request.getSession(true);
            session.setAttribute("SPRING_SECURITY_CONTEXT",securityContext);
            return "redirect:/redirecRolGoogle";
        }
    }
    @PostMapping("/login/ingresar_correo")
    public String ingresar_correo(Model model, @RequestParam("correo") String correo, RedirectAttributes redirectAttributes){
        List<Usuario> list_usuario = usuarioRepository.findAll();
        int i=0;
        for(Usuario listaUsuarios1: list_usuario){
            if (Objects.equals(listaUsuarios1.getCorreo(), correo)) {
                i = 1;
                break;
            }
        }
        if(i==1){
            String random_code = cadenaAleatoria(6);
            senderService.sendSimpleEmail(correo," Codigo de verificaci??n IPUCP","Su c??digo de verificaci??n es " + random_code);
            model.addAttribute("correo",correo);
            model.addAttribute("random_code",random_code);
            return "login/random-code-resetp";
        }else{
            String texto = "Correo inv??lido o inexistente.";
            redirectAttributes.addFlashAttribute("msg10",texto);
            return "redirect:/login";
        }
    }

    @PostMapping("/login/ingresar_aleatorio")
    public String ingresar_codrandom(Model model, @RequestParam("random_code") String random_code, @RequestParam("codigo_aleatorio") String codigo_aleatorio,
                                     @RequestParam("correo") String correo,
                                     RedirectAttributes redirectAttributes){
        if(Objects.equals(random_code, codigo_aleatorio)){
            model.addAttribute("correo",correo);
            return "login/reset-pass";
        }else{
            model.addAttribute("correo",correo);
            model.addAttribute("random_code",random_code);
            String texto = "C??digo incorrecto. Vuelva ingresar el c??digo que se env??o a su correo.";
            model.addAttribute("msg",texto);
            return "login/random-code-resetp";
        }
    }

    @PostMapping("/login/reset_password")
    public String reset(Model model,@RequestParam("contrasenha1") String contrasenha1, @RequestParam("contrasenha2") String contrasenha2,
                        @RequestParam("correo") String correo,
                        RedirectAttributes redirectAttributes) {
        if(isValid(contrasenha1) && isValid(contrasenha2)){
            if(Objects.equals(contrasenha1, contrasenha2)){
                usuarioRepository.cambiarpassword(BCrypt.hashpw(contrasenha1, BCrypt.gensalt()),correo);
                String texto = "Se ha cambiado exit??samente de contrase??a.";
                redirectAttributes.addFlashAttribute("msg20",texto);
                return "redirect:/login";
            }else{
                model.addAttribute("correo",correo);
                String texto = "Las contrase??as puestas no son iguales.";
                model.addAttribute("msg",texto);
                return "login/reset-pass";
            }
        }else{
            model.addAttribute("correo",correo);
            String texto = "La contrase??a debe tener al menos un d??gito, al menos una letra min??scula y may??scula, y caracteres especiales.";
            model.addAttribute("msg",texto);
            return "login/reset-pass";
        }

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
            if (Objects.equals(listaUsuarios1.getId(), codigo) || Objects.equals(listaUsuarios1.getCorreo(), correo)) {
                j = 1;
                break;
            }
        }
        System.out.println(i);
        System.out.println(j);
        System.out.println(usuario.getNombre()+" "+usuario.getApellido());
        if(i==1 && j!=1){
            String random_code = cadenaAleatoria(6);
            senderService.sendSimpleEmail(correo," Codigo de verificaci??n IPUCP","Su c??digo de verificaci??n es " + random_code);
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
            String texto = "C??digo incorrecto. Vuelva ingresar el c??digo que se env??o a su correo.";
            model.addAttribute("msg",texto);
            return "login/random-code";
        }

    }

    @PostMapping("/login/establecer_pass")
    public  String est_pass(Model model, @RequestParam("contrasenha1") String contrasenha1,
                            @RequestParam("contrasenha2") String contrasenha2,
                            @RequestParam("id") String codigo, @RequestParam("nombre") String nombre, @RequestParam("apellido") String apellido, @RequestParam("correo") String correo, @RequestParam("dni") String dni,
                            @ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                            RedirectAttributes redirectAttributes){

        if(isValid(contrasenha1) && isValid(contrasenha2)){
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
                String texto = "El usuario fue registrado.";
                redirectAttributes.addFlashAttribute("msg1",texto);
                return "redirect:/login";
            }else{
                String texto = "Las contrase??as puestas no son iguales.";
                model.addAttribute("msg",texto);
                return "login/new-pass";
            }
        }else{
            String texto = "La contrase??a debe tener al menos un d??gito, al menos una letra min??scula y may??scula, y caracteres especiales.";
            model.addAttribute("msg",texto);
            return "login/new-pass";
        }


    }


    public static String cadenaAleatoria(int longitud) {
        // El banco de caracteres
        String banco = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        // La cadena en donde iremos agregando un car??cter aleatorio
        String cadena = "";
        for (int x = 0; x < longitud; x++) {
            int indiceAleatorio = numeroAleatorioEnRango(0, banco.length() - 1);
            char caracterAleatorio = banco.charAt(indiceAleatorio);
            cadena += caracterAleatorio;
        }
        return cadena;
    }

    public static int numeroAleatorioEnRango(int minimo, int maximo) {
        // nextInt regresa en rango pero con l??mite superior exclusivo, por eso sumamos 1
        return ThreadLocalRandom.current().nextInt(minimo, maximo + 1);
    }

    public static boolean isValid(String s) {

        /*
        * (? =. * [0-9]) representa que un d??gito debe aparecer al menos una vez.
        (? =. * [az]) representa que el alfabeto en min??sculas debe aparecer al menos una vez.
        (? =. * [AZ]) representa un alfabeto en may??sculas que debe aparecer al menos una vez.
        (? =. * [@ # $% ^ & - + =()] representa un car??cter especial que debe aparecer al menos una vez.
        (? = \\ S + $) no se permiten espacios en blanco en toda la string.
        {5, 20} representa al menos 5 caracteres y como m??ximo 20 caracteres.*/

        Pattern p = Pattern.compile("^(?=.*[0-9])"
                + "(?=.*[a-z])(?=.*[A-Z])"
                + "(?=.*[@#$%^&+=])"
                + "(?=\\S+$).{5,20}$");

        // Pattern class contains matcher() method
        // to find matching between given number
        // and regular expression for which
        // object of Matcher class is created
        Matcher m = p.matcher(s);

        // Returning bollean value
        return (m.matches());
    }
}
