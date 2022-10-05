package com.example.ipucp.Controller;

import com.example.ipucp.Entity.*;
import com.example.ipucp.Repository.CargoRepository;
import com.example.ipucp.Repository.InicidenciaRepository;
import com.example.ipucp.Repository.TipoRepository;
import com.example.ipucp.Repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
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

    @GetMapping("")
    public String principal() {
        return "admin/principalAdmin";
    }

    @GetMapping("/listar")
    public String listar(Model model, @ModelAttribute("usuario") Usuario usuario) {
        model.addAttribute("listaUsuarios",usuarioRepository.findAll());
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/listar";
    }

    @PostMapping("/suspender")
    public String suspenderUsuario(@ModelAttribute("usuario") @Valid Usuario usuario, BindingResult bindingResult,
                                   @RequestParam("id") String codigo, @RequestParam("justificacion") String justificacion, Model model){
        if(bindingResult.hasErrors()){
            System.out.println("----------------------------- Error detectado --------------------------");
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("id",codigo);
            model.addAttribute("listaUsuarios",usuarioRepository.findAll());
            model.addAttribute("listaCargos",cargoRepository.findAll());
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

    @GetMapping("/nuevoNormal")
    public String nuevoNormal(Model model, @ModelAttribute("usuario") Usuario usuario) {
        model.addAttribute("tipoUsuario","normal");
        model.addAttribute("listaCargos",cargoRepository.findAll());
        return "admin/newForm";
    }

    @PostMapping("/save")
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
                if(user.getCelular().equals(usuario.getCelular())){
                    esDuplicado = 3;
                    msg2 = "El N° celular ingresado ya existe";
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


                usuario.getRol().setId(2); //Asignando rol de seguridad
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
                        usuarioRepository.cifradoHash(usuario.getContra(),usuario.getId()); //save hashed password
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
                                           @RequestParam("id") int id) {
        if(bindingResult.hasErrors()){
            System.out.println("----------------------------- Error detectado --------------------------");
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("id",Integer.toString(id));
            model.addAttribute("incidenciaEnReporte",tipoRepository.listaIncidencias());
            model.addAttribute("listaIncidencias",tipoRepository.findAll());
            return "admin/incidencias";
        }else {
            tipoRepository.save(tipo);
            attr.addFlashAttribute("msg","Tipo de incidencia actualizada correctamente");
            return "redirect:/admin/incidencias";
        }
    }

    @PostMapping("/saveIncident")
    public String guardarTipoIncidencia(@ModelAttribute("tipo") @Valid Tipo tipo, BindingResult bindingResult,
                                        @RequestParam("tipoIncidencia2") String tipoIncidencia2, RedirectAttributes attr, Model model) {
        if(tipoIncidencia2.equals("")){
            model.addAttribute("openModalCreate","No se acepta entrada vacía");
            model.addAttribute("incidenciaEnReporte",tipoRepository.listaIncidencias());
            model.addAttribute("listaIncidencias",tipoRepository.findAll());
            return "admin/incidencias";
        }if(tipoIncidencia2.length() >= 45) {
            model.addAttribute("openModalCreate", "Máximo 45 caracteres");
            model.addAttribute("corregir",tipoIncidencia2);
            model.addAttribute("incidenciaEnReporte", tipoRepository.listaIncidencias());
            model.addAttribute("listaIncidencias", tipoRepository.findAll());
            return "admin/incidencias";
        } else {
            tipoRepository.crearTipoIncidencia(tipoIncidencia2);
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
            return "admin/listar";
        }else{
            return "redirect:/admin/listar";
        }
    }

}
