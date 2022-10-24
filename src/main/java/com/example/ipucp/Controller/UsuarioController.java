package com.example.ipucp.Controller;

import com.example.ipucp.Entity.*;
import com.example.ipucp.Repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    InicidenciaRepository inicidenciaRepository;
    @Autowired
    TipoRepository tipoRepository;
    @Autowired
    UrgenciaRepository urgenciaRepository;
    @Autowired
    UsuarioRepository usuarioRepository;
    @Autowired
    UbicacionRepository ubicacionRepository;

    @Autowired
    ComentarioRepository comentarioRepository;

    @GetMapping("/mapa")
    public String mapa() {
        return "usuario/menu_mapa";
    }

    @GetMapping("/listar")
    public String listar(Model model) {

        List<Inicidencia> lista  =inicidenciaRepository.orderReciente();
        model.addAttribute("incidenciaList", lista);

        for(Inicidencia incidencias: lista){
            System.out.println(incidencias.getUbicacion().getNombre());
        }

        return "usuario/menu";
    }

    @PostMapping("/listarFiltrado")
    public String listarFiltrado(Model model, @RequestParam("orden") int form) {
        List<Inicidencia> listIncidencias = new ArrayList<>();

        if (form == 2){
            List<Inicidencia> lista  =inicidenciaRepository.orderMaspopular();
            model.addAttribute("incidenciaList", lista);
        }
        else{
            List<Inicidencia> lista  =inicidenciaRepository.orderReciente();
            model.addAttribute("incidenciaList", lista);
        }

        return "usuario/menu";
    }

    @GetMapping("/destacar")
    public String destacarIncidencia(@RequestParam("id") Integer id) {
        inicidenciaRepository.destacarIncidencia(id);
        return "redirect:/usuario/listar";
    }


    @PostMapping("/save")
    public String guardarNuevaIncidencia(@ModelAttribute("incidencia") @Valid Inicidencia incidencia, BindingResult bindingResult, Model model, RedirectAttributes attr,
                                         HttpSession session) {

        if(bindingResult.hasErrors()){

            List<Tipo> listaTipo  =tipoRepository.findAll();
            List<Urgencia> listaUrgencia  =urgenciaRepository.findAll();
            List<Ubicacion> listaUbicacion = ubicacionRepository.findAll();
            model.addAttribute("listaTipo", listaTipo);
            model.addAttribute("listaUrgencia", listaUrgencia);
            model.addAttribute("listaUbicacion", listaUbicacion);
            model.addAttribute("errorCompany", "Ningún campo puede dejarse vacío, intente crear nuevamente por favor");
            System.out.println(bindingResult.getFieldError());
            return "usuario/newIncidencia";
        }else{

            Usuario user = (Usuario) session.getAttribute("usuario");
            Instant fecha = inicidenciaRepository.fecha();
            incidencia.setFecha(fecha);
            incidencia.setCodigo(user);
            inicidenciaRepository.save(incidencia);
            attr.addFlashAttribute("msg","Incidencia creada exitosamente.");
            return "redirect:/usuario/misIncidencias";
        }

    }

    @GetMapping("/detalle")
    public String detalleIncidencia(Model model,
                                    @RequestParam("id") Integer id){
        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);


        if (optInicidencia.isPresent()){
            Inicidencia inicidencia = optInicidencia.get();

            model.addAttribute("incidencia", inicidencia);

            return "usuario/detalleIncid";
        }else{
            return "redirect:/usuario/misIncidencias";
        }
    }

    @GetMapping("/newInciden")
    public String newInciden(@ModelAttribute("incidencia") Inicidencia incidencia, Model model) {
        List<Tipo> listaTipo  =tipoRepository.findAll();
        List<Urgencia> listaUrgencia  =urgenciaRepository.findAll();
        List<Ubicacion> listaUbicacion = ubicacionRepository.findAll();
        model.addAttribute("listaTipo", listaTipo);
        model.addAttribute("listaUrgencia", listaUrgencia);
        model.addAttribute("listaUbicacion", listaUbicacion);
        return "usuario/newIncidencia";
    }

    @GetMapping("/perfil")
    public String perfil(Model model) {
        model.addAttribute("userperfil",usuarioRepository.userPerfil("20197171"));
        return "usuario/perfil";
    }
    @GetMapping("/misIncidencias")
    public String misIncidencias(Model model,HttpSession session, @ModelAttribute("incidencia") Inicidencia incidencia) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        model.addAttribute("listaIncidencias",inicidenciaRepository.userIncidencias(user.getId()));
        return "usuario/incidencias";
    }

    @GetMapping("/ListaComentarios")
    public String comentariosIncidencia() {
        return "usuario/lista_comentarios";
    }

    @GetMapping("/lista_comentarios")
    public String listacomentarios(HttpSession session, Model model, @RequestParam("id") Integer id, @ModelAttribute("comentario") Comentario comentario ) {
        List<Comentario> listaComentariosSeguridad = comentarioRepository.IncidenciasComentariosSeguridad(id);
        List<Comentario> listaComentariosUsuario = comentarioRepository.IncidenciasComentariosUsuario(id);

        Usuario user = (Usuario) session.getAttribute("usuario");

        Optional<Inicidencia> incidencia_flotante = inicidenciaRepository.findById(id);

        if (incidencia_flotante.isPresent()) {
            Inicidencia incidencia = incidencia_flotante.get();
            int i = 0;
            if (Objects.equals(incidencia.getCodigo().getId(), user.getId())) {
                i = 1;
            }

            if (listaComentariosSeguridad.size() == 0 || i == 0) {
                return "redirect:/usuario/misIncidencias";
            } else {
                model.addAttribute("id", id);
                model.addAttribute("listaComentariosSeguridad", listaComentariosSeguridad);
                model.addAttribute("listaComentariosUsuario", listaComentariosUsuario);
                return "usuario/lista_comentarios";
            }
        }else{
            return "redirect:/usuario/misIncidencias";
        }
    }

    @PostMapping("/comentar")
    public String comentar(@RequestParam("id") Integer id,
                           @RequestParam("comentarioUsuario") String comentarioUsuario,
                           RedirectAttributes redirectAttributes, @ModelAttribute("incidencia") @Valid Inicidencia incidencia,
                           BindingResult bindingResult, Model model, HttpSession session){
        Usuario user = (Usuario) session.getAttribute("usuario");

        if(bindingResult.hasErrors()){
            System.out.println("----------------------------- Error detectado --------------------------");
            System.out.println(bindingResult.getFieldError());
            model.addAttribute("id",id);
            model.addAttribute("listaIncidencias",inicidenciaRepository.userIncidencias(user.getId()));
            return "usuario/misIncidencias";
        }else {
            int max = incidencia.getMax_usuario();
            max+=1;
            inicidenciaRepository.comentarIncidenciaUsuario(comentarioUsuario,max,id);
            comentarioRepository.comentarIncidenciaUsuario(comentarioUsuario,id);
            if(max==5){
                inicidenciaRepository.cambiarEstadoIncidencia(id);
                redirectAttributes.addFlashAttribute("msg2","Incidencia comentada. Se ha llegado al máximo de comentarios por incidencia.");
                return "redirect:/usuario/misIncidencias";
            }else{
                redirectAttributes.addFlashAttribute("msg3","Incidencia comentada. Recuerda que hay un máximo de 5 comentarios por incidencia.");
                return "redirect:/usuario/misIncidencias";
            }
        }
    }

    @PostMapping("/incidenciaResuelta")
    public String incidenciaResuelta(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes) {
        inicidenciaRepository.cambiarEstadoIncidencia(id);
        redirectAttributes.addFlashAttribute("msg4","La incidencia con ID #"+id+" ha sido resuelta.");
        return "redirect:/usuario/misIncidencias";
    }





}
