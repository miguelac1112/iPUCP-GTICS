package com.example.ipucp.Controller;

import com.example.ipucp.Entity.Cargo;
import com.example.ipucp.Entity.Inicidencia;
import com.example.ipucp.Entity.Usuario;
import com.example.ipucp.Dto.UsuarioIncidencias;
import com.example.ipucp.Repository.UsuarioRepository;
import com.example.ipucp.Repository.CargoRepository;
import com.example.ipucp.Repository.InicidenciaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seguridad")
public class SeguridadController {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    InicidenciaRepository inicidenciaRepository;


    @GetMapping("")
    public String principal() {
        return "seguridad/principal";
    }

    @GetMapping("/incidencias")
    public String lista(Model model) {
        List<Inicidencia> inicidenciaList = inicidenciaRepository.findAll();
        model.addAttribute("ListaIncidencias", inicidenciaList);
        return "seguridad/incidencias";
    }

    @GetMapping("/comentar_incidencia")
    public String comentarIncidencia(@RequestParam("id") Integer id, Model model) {

        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);

        if(optInicidencia.isPresent()){
            Inicidencia inicidencia = optInicidencia.get();
            if(inicidencia.getEstado()==0){
                model.addAttribute("incidencia", inicidencia);
                return "seguridad/seguridad";
            }else{
                return "redirect:/seguridad/incidencias";
            }
        }else{
            return "redirect:/seguridad/incidencias";
        }
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model) {
            model.addAttribute("incidenciaEstado",inicidenciaRepository.bucarEstadoIncidencia());
            model.addAttribute("incidenciaUrgencia",inicidenciaRepository.buscarUrgenciaIncidencia());
        return "seguridad/dashboard";
    }

    @GetMapping("/mapa_incidencias")
    public String mapa() {
        return "seguridad/seguridad_mapa";
    }

    @GetMapping("/lista_usuarios")
    public String listaUsuarios(Model model) {
        List<Usuario> ListaUsuarios = usuarioRepository.listarUsuarios();
        model.addAttribute("listaUsuarios", ListaUsuarios);
        return "seguridad/lista_usuarios";
    }

    @PostMapping("/BuscarCategoria")
    public String buscarCategoria(@RequestParam("idcat") Integer id, Model model){
        Optional<Cargo> optCargo = cargoRepository.findById(id);

        if(optCargo.isPresent()){
            List<Usuario> listaUsuarios = usuarioRepository.buscarUsuarioPorCat(id);
            model.addAttribute("listaUsuarios", listaUsuarios);
            return "seguridad/lista_usuarios";
        }else{
            return "redirect:/seguridad/lista_usuarios";
        }
    }

    @GetMapping("/reporte")
    public String reporteUsuario(Model model,
                                      @RequestParam("id") String id) {

        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()) {
            Usuario usuario = optUsuario.get();
            List<UsuarioIncidencias> listaIncidencias = usuarioRepository.obtenerUsuarioIncidencias(id);
            for(UsuarioIncidencias incidencias: listaIncidencias){
                System.out.println(incidencias.getIdinicidencia()+" "+incidencias.getTipo_incidencia()+" "+incidencias.getEstado());
            }
            model.addAttribute("usuario", usuario);
            model.addAttribute("listaIncidencias", listaIncidencias);
            return "seguridad/seguridad_reportar";
        } else {
            return "redirect:/seguridad/lista_usuarios";
        }
    }

    @GetMapping("/detalle_incidencia")
    public String detalleIncidencia(Model model,
                                 @RequestParam("id") Integer id,@RequestParam("codigo") String codigo ){
        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);
        Optional<Usuario> optUsuario = usuarioRepository.findById(codigo);

        if (optInicidencia.isPresent() && optUsuario.isPresent() ){
            Inicidencia inicidencia = optInicidencia.get();

            model.addAttribute("incidencia", inicidencia);

            return "seguridad/detalleid_seguridad";
        }else{
            return "redirect:/seguridad/reporte?id="+codigo;
        }

    }

}
