package com.example.ipucp.Controller;

import com.example.ipucp.Dao.PerfilDao;
import com.example.ipucp.Dto.DtoIncidencia;
import com.example.ipucp.Dto.IncidenciaPorMes;
import com.example.ipucp.EmailSenderService;
import com.example.ipucp.Entity.*;
import com.example.ipucp.Dto.UsuarioIncidencias;
import com.example.ipucp.Repository.*;
import com.example.ipucp.util.ListaIncidenciasExcel;
import com.example.ipucp.util.ListaIncidenciasPDF;
import com.lowagie.text.DocumentException;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.http.HttpHeaders;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
@RequestMapping("/seguridad")
public class SeguridadController {
    @Autowired
    PerfilDao perfilDao;
    @Autowired
    private EmailSenderService senderService;

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    CargoRepository cargoRepository;

    @Autowired
    InicidenciaRepository inicidenciaRepository;

    @Autowired
    TipoRepository tipoRepository;

    @Autowired
    UrgenciaRepository urgenciaRepository;

    @Autowired
    ComentarioRepository comentarioRepository;



    @GetMapping("")
    public String principal(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String codigo = usuario.getId();
        usuarioRepository.reiniciarSeguridad(codigo);
        String codigo2factor = cadenaAleatoria(6);
        usuarioRepository.generarCodigoDe2Factor(codigo2factor,codigo);
        String correo = usuario.getCorreo();
        senderService.sendSimpleEmail(correo," Codigo de verificacion de 2 factores","Su código de verificación es " + codigo2factor);
        return "seguridad/principal";
    }

    @GetMapping("/lista_comentarios")
    public String listacomentarios(Model model, @RequestParam("id") Integer id ) {
        List<Comentario> listaComentariosSeguridad = comentarioRepository.IncidenciasComentariosSeguridad(id);
        List<Comentario> listaComentariosUsuario = comentarioRepository.IncidenciasComentariosUsuario(id);
        Optional<Inicidencia> incidenciaopt = inicidenciaRepository.findById(id);
        if(incidenciaopt.isPresent()){
            if(listaComentariosSeguridad.size()==0){
                return "redirect:/seguridad/incidencias";
            }else{
                model.addAttribute("id", id);
                model.addAttribute("listaComentariosSeguridad", listaComentariosSeguridad);
                model.addAttribute("listaComentariosUsuario",listaComentariosUsuario);
                return "seguridad/lista_comentarios";
            }
        }else{
            return "redirect:/seguridad/incidencias";
        }

    }


    // Define el separador
    private static final CsvPreference PIPE_DELIMITED = new CsvPreference.Builder('"', '|', "\n").build();
    @GetMapping("/exportar_txt")
    public void exportToTxt(HttpServletResponse response, @RequestParam("tipo") int idTipo ,@RequestParam("urgencia") int idUrgencia, @RequestParam("orden") int idOrden, @RequestParam("estado") int idEstad) throws IOException  {
        response.setContentType("text/csv");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());
        String headerKey = "Content-Disposition";
        // Aquí generas el nombre con el que vas a guardarlo y la extensión (.txt)
        String headerValue = "attachment; filename=incidencias_TXT_" + currentDateTime+".txt";
        response.setHeader(headerKey, headerValue);
        // Auqí mandas creas la lista que vas a exportar
        List<Inicidencia> inicidenciaList = new ArrayList<>();
        if(idEstad==2) {
            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntig(idTipo, idUrgencia));
                        }
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgencia(idTipo, idUrgencia));
                        }
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguo(idTipo));
                        }
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipo(idTipo));
                        }
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguo(idUrgencia));
                            }
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoUrgencia(idUrgencia));
                        }
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.findAll());}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevo());}
                    }
                }
            }
        }else{

            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntigEstado(idTipo, idUrgencia, idEstad));}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaEstado(idTipo, idUrgencia, idEstad));}
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguoEstado(idTipo, idEstad));}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoEstado(idTipo, idEstad));}
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguoEstado(idUrgencia,idEstad));}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaEstado(idUrgencia, idEstad));}
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.ordenAntigEstaodo(idEstad));}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevoEstaodo(idEstad));}
                    }
                }
            }
        }
        ICsvBeanWriter csvWriter = new CsvBeanWriter(response.getWriter(), PIPE_DELIMITED);
        String[] csvHeader ={"#", " Título ", " Tipo ", "Ubicación ", "Usuario ", " Fecha ", " Destacados "};
        String[] nameMapping = {"id", "nombre", "idtipo", "ubicacion", "codigo", "fecha", "destacado"};
        csvWriter.writeHeader(csvHeader);
        for(int i=0;i<inicidenciaList.size();i++){
            // Como debe coincidir exactamente, mejor creé un Dto para incidencias y lo asigné
            DtoIncidencia dtoIncidencia = new DtoIncidencia(inicidenciaList.get(i), i);
            // se asigna los valores
            csvWriter.write(dtoIncidencia, nameMapping);
        }
        csvWriter.close();
    }

    @GetMapping("/exportar_xlsx")
    public void exportToExcel(HttpServletResponse response, @RequestParam("tipo") int idTipo ,@RequestParam("urgencia") int idUrgencia, @RequestParam("orden") int idOrden, @RequestParam("estado") int idEstad)
            throws IOException {
        List<Inicidencia> inicidenciaList = new ArrayList<>();
        response.setContentType("application/octet-stream");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=incidencias_lista" + currentDateTime + ".xlsx";
        response.setHeader(headerKey,headerValue);

        if(idEstad==2) {
            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntig(idTipo, idUrgencia));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);
                        }
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgencia(idTipo, idUrgencia));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);
                        }
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguo(idTipo));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipo(idTipo));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);
                        }
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguo(idUrgencia));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoUrgencia(idUrgencia));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);
                        }
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.findAll());
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevo());
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                    }
                }
            }
        }else{

            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntigEstado(idTipo, idUrgencia, idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaEstado(idTipo, idUrgencia, idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguoEstado(idTipo, idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoEstado(idTipo, idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguoEstado(idUrgencia,idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaEstado(idUrgencia, idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.ordenAntigEstaodo(idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevoEstaodo(idEstad));
                            ListaIncidenciasExcel incidenciasExcel = new ListaIncidenciasExcel(inicidenciaList);
                            incidenciasExcel.export(response);}
                    }
                }
            }
        }

    }

    @GetMapping("/exportar_pdf")
    public void exportToPDF(HttpServletResponse response, @RequestParam("tipo") int idTipo ,@RequestParam("urgencia") int idUrgencia, @RequestParam("orden") int idOrden, @RequestParam("estado") int idEstad)
            throws DocumentException, IOException {
        List<Inicidencia> inicidenciaList = new ArrayList<>();
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=incidencias_lista" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        if(idEstad==2) {
            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntig(idTipo, idUrgencia));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);
                        }
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgencia(idTipo, idUrgencia));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);
                        }
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguo(idTipo));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoTipo(idTipo));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);
                        }
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguo(idUrgencia));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {
                            inicidenciaList.addAll(inicidenciaRepository.filtradoUrgencia(idUrgencia));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);
                        }
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.findAll());
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevo());
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                    }
                }
            }
        }else{

            if (idTipo != 0) {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntigEstado(idTipo, idUrgencia, idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoUrgenciaEstado(idTipo, idUrgencia, idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                    }
                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoAntiguoEstado(idTipo, idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoTipoEstado(idTipo, idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                    }
                }
            } else {
                if (idUrgencia != 0) {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaAntiguoEstado(idUrgencia,idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.filtradoUrgenciaEstado(idUrgencia, idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                    }

                } else {
                    switch (idOrden) {
                        case 1 -> {inicidenciaList.addAll(inicidenciaRepository.ordenAntigEstaodo(idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                        case 0 -> {inicidenciaList.addAll(inicidenciaRepository.ordenNuevoEstaodo(idEstad));
                            ListaIncidenciasPDF incidenciasPDF = new ListaIncidenciasPDF(inicidenciaList);
                            incidenciasPDF.export(response);}
                    }
                }
            }
        }


    }


    @PostMapping("/verificacion")
    public String verificar(Model model, HttpSession session,@RequestParam("codigo") String codigo, RedirectAttributes redirectAttributes){
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String id = usuario.getId();
        Optional<Usuario> optionalUsuario = usuarioRepository.findById(id);
        if(optionalUsuario.isPresent()){
            if(codigo.equals(optionalUsuario.get().getFactordoble())){
                usuarioRepository.validarSeguridad(id);
                return "redirect:/seguridad/incidencias";
            }else{
                String texto = "Código incorrecto, se le ha enviado otro a su correo";
                redirectAttributes.addFlashAttribute("msg",texto);
                return "redirect:/seguridad";
            }
        }else{
            return "redirect:/seguridad";
        }

    }

    @GetMapping("/incidencias")
    public String lista(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String codigo = usuario.getId();
        Optional<Usuario> optUser = usuarioRepository.findById(codigo);
        if(optUser.isPresent()){
            if(optUser.get().getValidado() == 1){
                /*Tipo*/
                List<Tipo> listaTipos  = this.obtenerTipos();
                /*urgencias*/
                List<Urgencia> listaUrg = this.obtenerUrgencias();
                /*Orden*/
                List<Orden> listaOrden = this.obtenerOrden();
                /*Estado*/
                List<Orden> listaEstados = this.obtenerEstado();
                List<Inicidencia> inicidenciaList = inicidenciaRepository.orderReciente();
                model.addAttribute("idtipoI",0);
                model.addAttribute("idUrgI",0);
                model.addAttribute("idOrdenI",0);
                model.addAttribute("idEstad",2);
                model.addAttribute("ListaIncidencias", inicidenciaList);

                HashMap<Inicidencia, String> datos = new HashMap<Inicidencia, String>();
                for(Inicidencia incidencia: inicidenciaList){
                    datos.put(incidencia,perfilDao.obtenerImagen("Incidencia_"+ String.valueOf(incidencia.getId())).getFileBase64());
                }
                model.addAttribute("hashmap",datos);
                model.addAttribute("ListaTipos", listaTipos);
                model.addAttribute("ListaUrgencia", listaUrg);
                model.addAttribute("ListaOrden", listaOrden);
                model.addAttribute("ListaEstado",listaEstados);
                return "seguridad/incidencias";
            }else{
                return "redirect:/seguridad";
            }
        }else{
            return "redirect:/seguridad";
        }
    }

    @GetMapping("/incidenciasFiltrado")
    public String listaFiltrada(Model model,@RequestParam("tipo") int idTipo ,@RequestParam("urgencia") int idUrgencia, @RequestParam("orden") int idOrden, @RequestParam("estado") int idEstad, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String codigo = usuario.getId();
        Optional<Usuario> optUser = usuarioRepository.findById(codigo);
        if(optUser.isPresent()){
            if(optUser.get().getValidado() == 1){
                List<Inicidencia> listIncidencias = new ArrayList<>();
                if(idEstad==2) {
                    if (idTipo != 0) {
                        if (idUrgencia != 0) {
                            switch (idOrden) {
                                case 1 ->
                                        listIncidencias.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntig(idTipo, idUrgencia));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipoUrgencia(idTipo, idUrgencia));
                            }
                        } else {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipoAntiguo(idTipo));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipo(idTipo));
                            }
                        }
                    } else {
                        if (idUrgencia != 0) {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.filtradoUrgenciaAntiguo(idUrgencia));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoUrgencia(idUrgencia));
                            }

                        } else {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.findAll());
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.ordenNuevo());
                            }
                        }
                    }
                }else{

                    if (idTipo != 0) {
                        if (idUrgencia != 0) {
                            switch (idOrden) {
                                case 1 ->
                                        listIncidencias.addAll(inicidenciaRepository.filtradoTipoUrgenciaAntigEstado(idTipo, idUrgencia, idEstad));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipoUrgenciaEstado(idTipo, idUrgencia, idEstad));
                            }
                        } else {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipoAntiguoEstado(idTipo, idEstad));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoTipoEstado(idTipo, idEstad));
                            }
                        }
                    } else {
                        if (idUrgencia != 0) {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.filtradoUrgenciaAntiguoEstado(idUrgencia,idEstad));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.filtradoUrgenciaEstado(idUrgencia, idEstad));
                            }

                        } else {
                            switch (idOrden) {
                                case 1 -> listIncidencias.addAll(inicidenciaRepository.ordenAntigEstaodo(idEstad));
                                case 0 -> listIncidencias.addAll(inicidenciaRepository.ordenNuevoEstaodo(idEstad));
                            }
                        }
                    }
                }

                /*Tipo*/
                List<Tipo> listaTipos  = this.obtenerTipos();
                /*urgencias*/
                List<Urgencia> listaUrg = this.obtenerUrgencias();
                /*Orden*/
                List<Orden> listaOrden = this.obtenerOrden();
                /*Estado*/
                List<Orden> listaEstados = this.obtenerEstado();
                model.addAttribute("idtipoI",idTipo);
                model.addAttribute("idUrgI",idUrgencia);
                model.addAttribute("idOrdenI",idOrden);
                model.addAttribute("idEstad",idEstad);
                model.addAttribute("ListaIncidencias",listIncidencias);
                model.addAttribute("ListaTipos", listaTipos);
                model.addAttribute("ListaUrgencia", listaUrg);
                model.addAttribute("ListaOrden", listaOrden);
                model.addAttribute("ListaEstado",listaEstados);
                return "seguridad/incidencias";
            }else{
                return "redirect:/seguridad";
            }
        }else{
            return "redirect:/seguridad";
        }
    }

    @GetMapping("/comentar_incidencia")
    public String comentarIncidencia(@RequestParam("id") Integer id, Model model,
                                     @ModelAttribute("incidencia") Inicidencia incidencia,
                                     @ModelAttribute("comentario") Comentario comentario) {

        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);

        if(optInicidencia.isPresent()){
            Inicidencia inicidencia = optInicidencia.get();
            Comentario comentario1 = comentarioRepository.comentario(id, id);
            if(Objects.isNull(comentario1)){
                Comentario comentario2 = new Comentario();
                comentario2.setTextComentario("Ingrese el comentario.");
                model.addAttribute("comentario", comentario2);
                model.addAttribute("incidencia", inicidencia);
                return "seguridad/seguridad";
            }else{
                if(inicidencia.getMax()<6){
                    model.addAttribute("comentario", comentario1);
                    model.addAttribute("incidencia", inicidencia);
                    return "seguridad/seguridad";
                }else{
                    return "redirect:/seguridad/incidencias";
                }
            }

        }else{
            return "redirect:/seguridad/incidencias";
        }
    }



    @PostMapping("/comentar")
    public String comentar(@RequestParam("id") Integer id, @RequestParam("codigo") String codigo,
                           @RequestParam("comentario") String comentario,
                           RedirectAttributes redirectAttributes, @ModelAttribute("incidencia") @Valid Inicidencia incidencia,
                           BindingResult bindingResult, Model model) {

        System.out.println(id+" "+codigo);

        Optional<Usuario> optusuario = usuarioRepository.findById(codigo);
        Optional<Inicidencia> optinicidencia_flotante = inicidenciaRepository.findById(id);

        if(optusuario.isPresent() && optinicidencia_flotante.isPresent()){

            Usuario usuario = optusuario.get();
            Inicidencia inicidencia_flotante = optinicidencia_flotante.get();
            incidencia.setCodigo(usuario);
            incidencia.setIdtipo(inicidencia_flotante.getIdtipo());
            incidencia.setEstado(inicidencia_flotante.getEstado());
            incidencia.setEmMedica(inicidencia_flotante.getEmMedica());
            incidencia.setNombre(inicidencia_flotante.getNombre());
            incidencia.setMax(inicidencia_flotante.getMax());

            incidencia.setDescripcion(inicidencia_flotante.getDescripcion());
            incidencia.setUbicacion(inicidencia_flotante.getUbicacion());

            if (bindingResult.hasErrors()) {
                model.addAttribute("incidencia", incidencia);
                return "seguridad/seguridad";
            }else{
                int max = incidencia.getMax();
                max+=1;
                inicidenciaRepository.comentarIncidencia(comentario,max,incidencia.getId());
                comentarioRepository.comentarIncidencia(comentario, incidencia.getId());
                String texto = "La incidencia con ID "+incidencia.getId()+" del usuario con código "+ incidencia.getCodigo().getId()+" ha sido respondida.";
                redirectAttributes.addFlashAttribute("msg",texto);
                return "redirect:/seguridad/incidencias";
            }
        }else{
            return "redirect:/seguridad/incidencias";
        }
    }


    @GetMapping("/dashboard")
    public String dashboard(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String codigo = usuario.getId();
        Optional<Usuario> optUser = usuarioRepository.findById(codigo);
        if(optUser.isPresent()){
            if(optUser.get().getValidado() == 1){
                model.addAttribute("incidenciaEstado",inicidenciaRepository.bucarEstadoIncidencia());
                model.addAttribute("incidenciaUrgencia",inicidenciaRepository.buscarUrgenciaIncidencia());
                model.addAttribute("incidenciaTipo",inicidenciaRepository.buscarTipoIncidencia());
                model.addAttribute("incidenciaCantidad",inicidenciaRepository.buscarCantidadIncidencia());
                List<Integer> listaCantidaMes = obtenerIncidenciasMes();
                model.addAttribute("listaCantidadMes",listaCantidaMes);
                return "seguridad/dashboard";
            }else{
                return "redirect:/seguridad";
            }
        }else{
            return "redirect:/seguridad";
        }
    }

    @GetMapping("/mapa_incidencias")
    public String mapa(HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        String codigo = usuario.getId();
        Optional<Usuario> optUser = usuarioRepository.findById(codigo);
        if(optUser.isPresent()){
            if(optUser.get().getValidado() == 1){
                return "seguridad/seguridad_mapa";
            }else{
                return "redirect:/seguridad";
            }
        }else{
            return "redirect:/seguridad";
        }
    }

    @GetMapping("/lista_usuarios")
    public String listaUsuarios(Model model) {
        List<Usuario> ListaUsuarios = usuarioRepository.listarUsuarios();
        HashMap<Usuario,String> user = new HashMap<Usuario,String>();
        for(Usuario usuario: ListaUsuarios){
            user.put(usuario,perfilDao.obtenerImagen(usuario.getId()).getFileBase64());
        }
        model.addAttribute("iperfil",user);
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

    @PostMapping("/StrikeUsuario")
    public String StrikeUsuario(@RequestParam("id") String id, RedirectAttributes redirectAttributes){
        Optional<Usuario> optUsuario = usuarioRepository.findById(id);

        if (optUsuario.isPresent()){
            Usuario usuario = optUsuario.get();
            int strike = usuario.getStrikes();
            strike+=1;
            if(strike==3){
                usuarioRepository.strikeUsuario(strike,id);
                usuarioRepository.banUsuario(1,id);
                redirectAttributes.addFlashAttribute("msg", "El usuario "+usuario.getNombre()+" "+usuario.getApellido()+" ha sido reportado.");
                return "redirect:/seguridad/lista_usuarios";
            }else{
                usuarioRepository.strikeUsuario(strike,id);
                redirectAttributes.addFlashAttribute("msg", "El usuario "+usuario.getNombre()+" "+usuario.getApellido()+" ha sido reportado.");
                return "redirect:/seguridad/lista_usuarios";
            }
        }else{
            return "redirect:/seguridad/reporte?id="+id;
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

    public List<Urgencia> obtenerUrgencias(){
        List<Urgencia> listaUrg = new ArrayList<>();
        Urgencia urgTodos = new Urgencia();
        urgTodos.setId(0);
        urgTodos.setTipoUrgencia("Todas las urgencias");
        listaUrg.add(urgTodos);
        listaUrg.addAll(urgenciaRepository.findAll());
        return listaUrg;
    }
    public List<Tipo> obtenerTipos(){
        List <Tipo>  listaTipos = new ArrayList<>();
        Tipo todos = new Tipo();
        todos.setId(0);
        todos.setTipoIncidencia("Todos");
        listaTipos.add(todos);
        listaTipos.addAll(tipoRepository.findAll());
        return listaTipos;
    }

    public List<Orden> obtenerOrden(){
        List <Orden> listaOrdenes = new ArrayList<>();
        Orden reciente = new Orden();
        reciente.setIdOrdern(0);
        reciente.setTexto("Mas reciente");
        listaOrdenes.add(reciente);
        Orden antiguo = new Orden();
        antiguo.setIdOrdern(1);
        antiguo.setTexto("Mas antiguo");
        listaOrdenes.add(antiguo);
        return listaOrdenes;
    }

    public List<Orden> obtenerEstado(){
        List <Orden> listaEstados = new ArrayList<>();
        Orden todo = new Orden();
        todo.setIdOrdern(2);
        todo.setTexto("Todos");
        listaEstados.add(todo);
        Orden atendido = new Orden();
        atendido.setIdOrdern(1);
        atendido.setTexto("Atendida");
        listaEstados.add(atendido);
        Orden porAtender = new Orden();
        porAtender.setIdOrdern(0);
        porAtender.setTexto("Por atender");
        listaEstados.add(porAtender);
        return listaEstados;
    }

    public List<Integer> obtenerIncidenciasMes(){

        List <IncidenciaPorMes> listaIporMes = inicidenciaRepository.incidenciaMes();
        List<Integer> listaFinal = new ArrayList<>();
        int contador;
        for (int i=1; i <13; i++){
            contador = 0;
            for ( IncidenciaPorMes inci : listaIporMes){
                if (inci.getMes() == i){
                    listaFinal.add(inci.getCantidad());
                    contador = 1;
                    break;
                }else{
                    contador = 0;
                }
            }
            switch (contador){
                case 0->listaFinal.add(0);
            }
        }

        return listaFinal;
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
