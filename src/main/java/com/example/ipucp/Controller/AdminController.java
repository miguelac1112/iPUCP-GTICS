package com.example.ipucp.Controller;

import com.example.ipucp.Dao.CargoDao;
import com.example.ipucp.Dao.PerfilDao;
import com.example.ipucp.Dao.UsuarioDao;
import com.example.ipucp.Dto.UsuarioDto;
import com.example.ipucp.Entity.*;
import com.example.ipucp.Repository.CargoRepository;
import com.example.ipucp.Repository.InicidenciaRepository;
import com.example.ipucp.Repository.TipoRepository;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.*;


@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    TipoRepository tipoRepository;

    @Autowired
    CargoDao cargoDao;

    @Autowired
    UsuarioDao usuarioDao;
    @Autowired
    PerfilDao perfilDao;
    @GetMapping("")
    public String principal() {
        return "admin/principalAdmin";
    }

    @GetMapping("/listar")
    public String listar(Model model, @ModelAttribute("usuario") Usuario usuario) {
        List<Usuario> listaUsuarios = usuarioRepository.findAll();
        model.addAttribute("listaUsuarios",listaUsuarios);
        model.addAttribute("listaCargos",cargoRepository.findAll());
        HashMap<Usuario,String> user = new HashMap<Usuario,String>();
        for(Usuario u: listaUsuarios){
            user.put(u,perfilDao.obtenerImagen(u.getId()).getFileBase64());
        }
        model.addAttribute("iperfi",user);
        return "admin/listar";
    }

    @PostMapping("/suspender")
    public String suspenderUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                   @RequestParam("id") String codigo, @RequestParam("justificacion") String justificacion, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("----------------------------- Error detectado --------------------------");
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("id",codigo);
            List<Usuario> listaUsuarios = usuarioRepository.findAll();
            model.addAttribute("listaUsuarios",listaUsuarios);
            model.addAttribute("listaCargos",cargoRepository.findAll());
            HashMap<Usuario,String> user = new HashMap<Usuario,String>();
            for(Usuario u: listaUsuarios){
                user.put(u,perfilDao.obtenerImagen(u.getId()).getFileBase64());
            }
            model.addAttribute("iperfi",user);
            return "admin/listar";
        }else {
            usuarioRepository.suspenderUsuario(justificacion,codigo);
            return "redirect:/admin/listar";
        }
    }

    @GetMapping("/nuevoSeguridad")
    public String nuevoSeguridad(Model model, @ModelAttribute("usuario") Usuario usuario) {
        model.addAttribute("tipoUsuario","seguridad");
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/newForm";
    }

    //Usuarios que se encuentran en la base de datos externa y a la vez en la interna (ya registrados)
    /*
    public List<UsuarioDto> usuariosEnBaseExternaYRegistradosInterna(){
        List<UsuarioDto> listaNew = new ArrayList<>();
        for(UsuarioDto usuarioExterno : usuarioDao.listarUsuarios()){
            for (Usuario usuarioInterno : usuarioRepository.findAll()){
                if(usuarioExterno.getCodigo().equals(usuarioInterno.getId())){
                    listaNew.add(usuarioExterno);
                    break;
                }
            }
        }
        return listaNew;
    }
     */

    //Usuarios que se encuentran en la base de datos externa pero no en la interna (creados pero no registrados)
    /*
    public List<UsuarioDto> usuariosEnBaseExternaPeroNoRegistradosInterna(){
        List<UsuarioDto> listaNew = new ArrayList<>();
        int num = 0;
        for(UsuarioDto usuarioExterno : usuarioDao.listarUsuarios()){
            for (Usuario usuarioInterno : usuarioRepository.findAll()){
                if(usuarioExterno.getCodigo().equals(usuarioInterno.getId())){
                    num = 1;
                    break;
                }
            }
            if(num == 0){
                listaNew.add(usuarioExterno);
            }
            num = 0;
        }
        return listaNew;
    }

     */

    @PostMapping("/verificarSeguridad")
    public String verificarSeguridad(@RequestParam("codigo") String codigo, Model model, RedirectAttributes attr,
                                     @ModelAttribute("usuario") Usuario usuario){
        String msg2 = "";
        if(codigo.length() == 8){
            try {
                Double.parseDouble(codigo);
                //Continuamos:

                System.out.println(codigo+"################################################################################");
                msg2 = "El código ingresado para usuario seguridad no esta registrado ni creado";
                int cont = 0;
                for (Usuario usuarioRegistrado : usuarioRepository.findAll()){
                    if(codigo.equals(usuarioRegistrado.getId())){
                        System.out.println("Usuario ya registrado en base de datos de la aplicación web ################################################################################");
                        msg2 = "Usuario ya registrado en base de datos de la aplicación web";
                        cont = 1;
                        break;
                    }
                }
                // Si no se encuentra en la lista anterior entonces puede que este creado pero aun no registrado
                if(cont==0){
                    UsuarioDto optUsuario = usuarioDao.buscarOtro(codigo);
                    if (optUsuario != null){
                        System.out.println("INGRESASSSSSSTETTETET   ################################################################################");
                        model.addAttribute("tipoUsuario","seguridad");
                        model.addAttribute("listaCargos",cargoDao.listarCargos());

                        //Transferencia usuarioDTO a usuario
                        Usuario usuario1 = new Usuario();
                        usuario1.setId(optUsuario.getCodigo());
                        usuario1.setNombre(optUsuario.getNombre());
                        usuario1.setApellido(optUsuario.getApellido());
                        usuario1.setCorreo(optUsuario.getCorreo());
                        usuario1.setCelular("999999999");
                        usuario1.setDni(optUsuario.getDni());
                        usuario1.setRol(new Rol());
                        usuario1.setCargo(new Cargo());
                        usuario1.setEstado((byte) 0);
                        usuario1.setStrikes((byte) 0);
                        usuario1.setBan((byte) 0);


                        model.addAttribute("usuario",usuario1);
                        model.addAttribute("hayConfirma","0");
                        return "admin/newForm";
                    }
                    System.out.println("Usuario jajajaja ################################################################################");
                }

            }catch(NumberFormatException e) {
                System.out.println("Solo números");
            }
        }else {
            System.out.println("Solo 8 caracteres");
        }

        if(msg2.equals("")){
            msg2 = "Se debe ingresar 8 caracteres que sean numeros";
        }
        attr.addFlashAttribute("msg2",msg2);
        return "redirect:/admin/listar";
    }


    //Probando consumo:

    @GetMapping("/nuevoNormal")
    public String nuevoNormal(Model model, @ModelAttribute("usuario") UsuarioDto usuario) {
        model.addAttribute("tipoUsuario","normal");
        model.addAttribute("listaCargos",cargoDao.listarCargos());  //Consumiendo WebService
        return "admin/newForm2";
    }


    @PostMapping("/save2")
    public String guardarUsuarioExterno(@ModelAttribute("usuario") @Valid UsuarioDto usuario, BindingResult bindingResult,
                                        Model model, RedirectAttributes attr) {
        if(bindingResult.hasErrors()){
            //System.out.println(bindingResult.getFieldError());
            model.addAttribute("tipoUsuario","normal");
            model.addAttribute("listaCargos",cargoDao.listarCargos());
            return "admin/newForm2";
        }else {

            // -------- Verificacion de no duplicados --------------
            int esDuplicado = 0;
            String msg2 = "";
            for(UsuarioDto user : usuarioDao.listarUsuarios()){
                if(user.getCodigo().equals(usuario.getCodigo())){
                    esDuplicado = 1;
                    msg2 = "El código ingresado ya existe";
                    break;
                }
                if(user.getCorreo().equals(usuario.getCorreo())){
                    esDuplicado = 2;
                    msg2 = "El correo ingresado ya existe";
                    break;
                }
                if(user.getDni().equals(usuario.getDni())){
                    esDuplicado = 4;
                    msg2 = "El DNI ingresado ya existe";
                    break;
                }
            }
            // -----------------------------------------------------

            if(esDuplicado != 0){
                model.addAttribute("msg",msg2);
                model.addAttribute("tipoUsuario","normal");
                model.addAttribute("listaCargos",cargoDao.listarCargos());
                return "admin/newForm2";
            }else {
                attr.addFlashAttribute("msg","Usuario agregado exitosamente a base externa");
                usuarioDao.guardarUsuario(usuario);
                return "redirect:/admin/listar";
            }
        }
    }
    // -------------------------------------------------------------------------------------


    @PostMapping("/saveSeguridad")
    public String guardarUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                 @RequestParam("cargo") Integer id, @RequestParam("pass2") String pass2,
                                 @RequestParam("tipoUsuario") String tipoUsuario,RedirectAttributes attr, Model model) {

        if(bindingResult.hasErrors()){
            //Si se actualizará el usuario y este se vincula con esta parte del controlador, se deberia enviar la lista
            //con los datos del usuario, en este caso el admin no actualiza, solo crea al usuario.
            if(tipoUsuario.equals("normal")){
                model.addAttribute("tipoUsuario","normal");
            }else{
                model.addAttribute("tipoUsuario","seguridad");
                if(pass2.equals("")){
                    model.addAttribute("hayConfirma","1");
                }
            }
            //System.out.println(bindingResult.getFieldError());
            model.addAttribute("listaCargos",cargoRepository.findAll());
            return "admin/newForm";
        }else{
            // -------- Verificacion de no duplicados --------------
            int esDuplicado = 0;
            String msg2 = "";
            for(Usuario user : usuarioRepository.findAll()){
                if(user.getId().equals(usuario.getId())){
                    esDuplicado = 1;
                    msg2 = "El código ingresado ya existe";
                    break;
                }
                if(user.getCorreo().equals(usuario.getCorreo())){
                    esDuplicado = 2;
                    msg2 = "El correo ingresado ya existe";
                    break;
                }
                if(user.getDni().equals(usuario.getDni())){
                    esDuplicado = 4;
                    msg2 = "El DNI ingresado ya existe";
                    break;
                }
            }
            // -----------------------------------------------------

            usuario.setRol(new Rol());
            if(id == 6){


                usuario.getRol().setId(4); //Asignando rol de seguridad
                if(usuario.getContra().equals(pass2)){

                    if(esDuplicado != 0){
                        if(tipoUsuario.equals("normal")){
                            model.addAttribute("tipoUsuario","normal");
                        }else{
                            model.addAttribute("tipoUsuario","seguridad");
                        }
                        model.addAttribute("listaCargos",cargoRepository.findAll());
                        model.addAttribute("msg",msg2);
                        return "admin/newForm";
                    }else{
                        usuarioRepository.save(usuario); //guardando seguridad
                        usuarioRepository.cifradoHash(BCrypt.hashpw(usuario.getContra(), BCrypt.gensalt()),usuario.getId()); //save BCrypt password
                    }

                }else{
                    model.addAttribute("tipoUsuario","seguridad");
                    model.addAttribute("listaCargos",cargoRepository.findAll());
                    if(pass2.equals("")){
                        model.addAttribute("hayConfirma","1");
                    }
                    model.addAttribute("msg","Las contraseñas no coinciden, ingrese nuevamente");
                    return "admin/newForm";
                }


            }else {
                usuario.getRol().setId(1); //Asignando rol de usuario
                if(esDuplicado != 0){
                    if(tipoUsuario.equals("normal")){
                        model.addAttribute("tipoUsuario","normal");
                    }else{
                        model.addAttribute("tipoUsuario","seguridad");
                        if(pass2 != null){
                            model.addAttribute("hayConfirma","1");
                        }
                    }
                    model.addAttribute("listaCargos",cargoRepository.findAll());
                    model.addAttribute("msg",msg2);
                    return "admin/newForm";
                }else{
                    usuarioRepository.save(usuario); //guardando usuario
                }
            }
            attr.addFlashAttribute("msg","Usuario creado exitosamente");
            return "redirect:/admin/listar";
        }
    }

    @GetMapping("/incidencias")
    public String incidencias(Model model, @ModelAttribute("tipo") Tipo tipo) {

        List<Tipo> ListaTipo = tipoRepository.findAll();
        HashMap<Tipo,String> ti = new HashMap<Tipo,String>();
        for(Tipo tip: ListaTipo){
            ti.put(tip,perfilDao.obtenerImagen("t"+String.valueOf(tip.getId())).getFileBase64());
        }
        model.addAttribute("hashti",ti);
        model.addAttribute("incidenciaEnReporte",tipoRepository.listaIncidencias());
        model.addAttribute("listaIncidencias",tipoRepository.findAll());
        return "admin/incidencias";
    }

    @GetMapping("/habilitar")
    public String habilitarUsuario(@RequestParam("id") String codigo){
        usuarioRepository.habilitarUsuario(codigo);
        return "redirect:/admin/listar";
    }

    @PostMapping("/updateIncident")
    public String actualizarTipoIncidencia(@ModelAttribute("tipo") @Valid Tipo tipo, BindingResult bindingResult, RedirectAttributes attr, Model model,
                                           @RequestParam("id") int id,@RequestParam(name = "foti",required = false) MultipartFile img) {
        if(bindingResult.hasErrors()){
            System.out.println("----------------------------- Error detectado --------------------------");
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("id",Integer.toString(id));
            model.addAttribute("incidenciaEnReporte",tipoRepository.listaIncidencias());
            model.addAttribute("listaIncidencias",tipoRepository.findAll());
            return "admin/incidencias";
        }else {
            tipoRepository.save(tipo);
            try {
                byte[] bytes = img.getBytes();
                Perfil perfil = new Perfil();
                perfil.setName("t"+String.valueOf(tipo.getId())+".png");
                perfil.setFileBase64(Base64.getEncoder().encodeToString(bytes));
                perfilDao.subirImagen(perfil);

            }catch (Exception e){
                System.out.println("Hay excepcion");
            }
            attr.addFlashAttribute("msg","Tipo de incidencia actualizada correctamente");
            return "redirect:/admin/incidencias";
        }
    }

    @PostMapping("/saveIncident")
    public String guardarTipoIncidencia(@ModelAttribute("tipo") @Valid Tipo tipo, BindingResult bindingResult,
                                        @RequestParam("tipoIncidencia2") String tipoIncidencia2, RedirectAttributes attr,@RequestParam(name = "foto2",required = false) MultipartFile img, Model model) {
        if(tipoIncidencia2.equals("") || tipoIncidencia2.length() >= 45 || img.isEmpty()){
            List<Tipo> ListaTipo = tipoRepository.findAll();
            HashMap<Tipo,String> ti = new HashMap<Tipo,String>();
            for(Tipo tip: ListaTipo){
                ti.put(tip,perfilDao.obtenerImagen("t"+String.valueOf(tip.getId())).getFileBase64());
            }
            model.addAttribute("hashti",ti);
            if(tipoIncidencia2.equals("")){
                model.addAttribute("openModalCreate","No se acepta entrada vacía");
                model.addAttribute("incidenciaEnReporte",tipoRepository.listaIncidencias());
                model.addAttribute("listaIncidencias",tipoRepository.findAll());
            }if(tipoIncidencia2.length() >= 45) {
                model.addAttribute("openModalCreate", "Máximo 45 caracteres");
                model.addAttribute("corregir",tipoIncidencia2);
                model.addAttribute("incidenciaEnReporte", tipoRepository.listaIncidencias());
                model.addAttribute("listaIncidencias", tipoRepository.findAll());
            }if(img.isEmpty()){
                model.addAttribute("openModalCreate", "Imagen vacia");
                model.addAttribute("corregir",tipoIncidencia2);
                model.addAttribute("incidenciaEnReporte", tipoRepository.listaIncidencias());
                model.addAttribute("listaIncidencias", tipoRepository.findAll());
            }
            return "admin/incidencias";
        }else {
            tipoRepository.crearTipoIncidencia(tipoIncidencia2);
            try {
                byte[] bytes = img.getBytes();
                Perfil perfil = new Perfil();
                Tipo a = tipoRepository.findAll().get(tipoRepository.findAll().size()-1);
                perfil.setName("t"+String.valueOf(a.getId())+".png");
                perfil.setFileBase64(Base64.getEncoder().encodeToString(bytes));
                perfilDao.subirImagen(perfil);

            }catch (Exception e){
                System.out.println("Hay excepcion");
            }
            attr.addFlashAttribute("msg","Tipo de incidencia creada exitosamente");
            return "redirect:/admin/incidencias";
        }

    }

    @GetMapping("/deleteIncident")
    public String borrarIncidencias(@RequestParam("id") int id, RedirectAttributes attr) {
        Optional<Tipo> opTipo = tipoRepository.findById(id);

        if(opTipo.isPresent()){
            tipoRepository.deleteById(id);
            attr.addFlashAttribute("msg","Tipo de incidencia borrada exitosamente");
        }

        return "redirect:/admin/incidencias";
    }

    @PostMapping("/BuscarCategoria")
    public String buscarCategoria(@RequestParam("idcat") Integer id, Model model, @ModelAttribute("usuario") Usuario usuario){
        Optional<Cargo> optCargo = cargoRepository.findById(id);

        if(optCargo.isPresent()){
            List<Usuario> listaUsuarios = usuarioRepository.buscarUsuarioPorCat(id);
            model.addAttribute("listaUsuarios", listaUsuarios);
            model.addAttribute("listaCargos",cargoRepository.findAll());
            model.addAttribute("id",id);
            HashMap<Usuario,String> user = new HashMap<Usuario,String>();
            for(Usuario u: listaUsuarios){
                user.put(u,perfilDao.obtenerImagen(u.getId()).getFileBase64());
            }
            model.addAttribute("iperfi",user);
            return "admin/listar";
        }else{
            return "redirect:/admin/listar";
        }
    }

}
