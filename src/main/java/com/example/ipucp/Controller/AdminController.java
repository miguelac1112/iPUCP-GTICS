package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Cargo;
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
    public String listar(Model model) {
        model.addAttribute("listaUsuarios",usuarioRepository.findAll());
        model.addAttribute("listaCargos",cargoRepository.findAll());
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
        attr.addFlashAttribute("msg","Usuario creado exitosamente");
        return "redirect:/admin/listar";
    }

    @GetMapping("/incidencias")
    public String incidencias(Model model) {
        model.addAttribute("listaIncidencias",tipoRepository.findAll());
        return "admin/incidencias";
    }

    @GetMapping("/habilitar")
    public String habilitarUsuario(@RequestParam("id") String codigo){
        usuarioRepository.habilitarUsuario(codigo);
        return "redirect:/admin/listar";
    }

    @PostMapping("/suspender")
    public String suspenderUsuario(@RequestParam("id") String codigo, @RequestParam("justificacion") String justificacion){
        usuarioRepository.suspenderUsuario(justificacion,codigo);
        return "redirect:/admin/listar";
    }

    @PostMapping("/updateIncident")
    public String actualizarTipoIncidencia(Tipo tipo, RedirectAttributes attr) {
        tipoRepository.save(tipo);
        attr.addFlashAttribute("msg","Tipo de incidencia actualizada correctamente");
        return "redirect:/admin/incidencias";
    }

    @PostMapping("/saveIncident")
    public String guardarTipoIncidencia(@RequestParam("tipoIncidencia2") String tipoIncidencia2, RedirectAttributes attr ) {
        tipoRepository.crearTipoIncidencia(tipoIncidencia2);
        attr.addFlashAttribute("msg","Tipo de incidencia creada exitosamente");
        return "redirect:/admin/incidencias";
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
    public String buscarCategoria(@RequestParam("idcat") Integer id, Model model){
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
