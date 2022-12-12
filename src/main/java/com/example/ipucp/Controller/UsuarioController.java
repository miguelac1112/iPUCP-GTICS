package com.example.ipucp.Controller;

import com.example.ipucp.Dao.PerfilDao;
import com.example.ipucp.EmailSenderService;
import com.example.ipucp.Entity.*;
import com.example.ipucp.Repository.*;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.time.Instant;
import java.util.*;

import java.util.Base64;
import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;


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
    private EmailSenderService senderService;
    @Autowired
    ComentarioRepository comentarioRepository;
    @Autowired
    PerfilDao perfilDao;

    @GetMapping("/mapa")
    public String mapa(Model model,HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        Usuario us = usuarioRepository.userPerfil(u.getId());
        List<Inicidencia> listaIncidencia  =inicidenciaRepository.findAll();
        model.addAttribute("listaIncidencia", listaIncidencia);
        model.addAttribute("usi",us);

        return "usuario/menu_mapa";
    }

    @GetMapping("/listar")
    public String listar(Model model,@RequestParam("index") Integer index) {

        HashMap<Inicidencia, String> datos = new HashMap<Inicidencia, String>();
        HashMap<Inicidencia,String> user = new HashMap<Inicidencia,String>();
        List<Inicidencia> lista1  =inicidenciaRepository.orderReciente_Usuario();

        int paso = 10; //Cuantos publicaciones por vista
        int finalIndex;
        int inicialIndex;
        // Condiciones index final:
        if((index+1)*paso <= lista1.size()){
            finalIndex = (index+1)*paso;
        }else{
            finalIndex = lista1.size();
            model.addAttribute("disableSiguiente","disableSiguiente");
        }
        // Condiciones index inicial:
        if(index*paso > 0){
            inicialIndex = index*paso;
        }else{
            inicialIndex = 0;
            model.addAttribute("disableAnterior","disableAnterior");
        }

        if(inicialIndex<finalIndex){
            List<Inicidencia> lista = lista1.subList(inicialIndex, finalIndex);
            model.addAttribute("index",index);
            model.addAttribute("incidenciaList", lista);

            for(Inicidencia incidencia: lista){
                datos.put(incidencia,perfilDao.obtenerImagen("Incidencia_"+ String.valueOf(incidencia.getId())).getFileBase64());
                user.put(incidencia,perfilDao.obtenerImagen(incidencia.getCodigo().getId()).getFileBase64());
            }
            model.addAttribute("hashmap",datos);
            model.addAttribute("iperfil",user);
            return "usuario/menu";
        }else{
            //Esto por si algun usuario chistoso pone en el link un index que supera el numero de incidencias
            return "redirect:/usuario/listar?index=0";
        }
    }

    @PostMapping("/listarFiltrado")
    public String listarFiltrado(Model model, @RequestParam("orden") int form) {
        List<Inicidencia> listIncidencias = new ArrayList<>();
        HashMap<Inicidencia, String> datos = new HashMap<Inicidencia, String>();
        HashMap<Inicidencia,String> user = new HashMap<Inicidencia,String>();

        if (form == 2){
            List<Inicidencia> lista  =inicidenciaRepository.orderMaspopular();
            model.addAttribute("incidenciaList", lista);
            for(Inicidencia incidencia: lista){
                datos.put(incidencia,perfilDao.obtenerImagen("Incidencia_"+ String.valueOf(incidencia.getId())).getFileBase64());
                user.put(incidencia,perfilDao.obtenerImagen(incidencia.getCodigo().getId()).getFileBase64());
            }
        }
        else if(form ==1){
            return "redirect:/usuario/listar?index=0";
        }else{
            return "redirect:/usuario/listar?index=0";
        }
        model.addAttribute("hashmap",datos);
        model.addAttribute("iperfil",user);
        return "usuario/menu";
    }

    @GetMapping("/destacar")
    public String destacarIncidencia(@RequestParam("id") Integer id, RedirectAttributes attr) {
        Optional<Inicidencia> optInicidencia = inicidenciaRepository.findById(id);
        if(optInicidencia.isPresent()){
            Inicidencia incidencia = optInicidencia.get();
            if(incidencia.getDestacado()!=3){
                inicidenciaRepository.destacarIncidencia(id);
                attr.addFlashAttribute("msg","La incidencia con título: '"+incidencia.getNombre()+"' ha recibido un nuevo destacado.");
                return "redirect:/usuario/listar?index=0";
            }else{
                return "redirect:/usuario/listar?index=0";
            }
        }else{
            return "redirect:/usuario/listar?index=0";
        }

    }


    @PostMapping("/save")
    public String guardarNuevaIncidencia(@ModelAttribute("incidencia") @Valid Inicidencia incidencia, BindingResult bindingResult, Model model, RedirectAttributes attr,
                                         HttpSession session,@RequestParam(name = "fot") MultipartFile img, @RequestParam("lat") String latitud,@RequestParam("lon")String longitud) {
        if(bindingResult.hasErrors()){
            System.out.println("############################################### Estoy Aqui");
            List<Tipo> listaTipo  =tipoRepository.findAll();
            List<Urgencia> listaUrgencia  =urgenciaRepository.findAll();
            List<Ubicacion> listaUbicacion = ubicacionRepository.findAll();
            model.addAttribute("listaTipo", listaTipo);
            model.addAttribute("listaUrgencia", listaUrgencia);
            model.addAttribute("listaUbicacion", listaUbicacion);
            model.addAttribute("errorCompany", "Ningún campo puede dejarse vacío, intente crear nuevamente por favor");
            System.out.println(bindingResult.getFieldError());
            if(img.isEmpty()){
                System.out.println("######################################################################imagen vacia");
                model.addAttribute("imagenVacia", "imagenVacia");
                return "usuario/newIncidencia";
            }else{
                model.addAttribute("imagenVacia", "imagenNoVacia");
            }
            if(incidencia.getIdtipo()!=null){
                model.addAttribute("tipoInci",incidencia.getIdtipo().getId());
                System.out.println(incidencia.getIdtipo().getId());
            }
            if(incidencia.getIdurgencia()!=null){
                model.addAttribute("tipoUrg",incidencia.getIdurgencia().getId());
                System.out.println(incidencia.getIdurgencia().getId());
            }
            if(incidencia.getEmMedica()!=null){
                model.addAttribute("emMedicaa",incidencia.getEmMedica());
                System.out.println(incidencia.getEmMedica());
            }
            if(incidencia.getUbicacion()!=null){
                model.addAttribute("ubiId",incidencia.getUbicacion().getId());
                System.out.println(incidencia.getUbicacion().getId());
            }
            return "usuario/newIncidencia";
        }else{

            Usuario user = (Usuario) session.getAttribute("usuario");
            Instant fecha = inicidenciaRepository.fecha();
            incidencia.setFecha(fecha);
            incidencia.setCodigo(user);
            incidencia.setLatitud(latitud);
            incidencia.setLongitud(longitud);

            System.out.println("El usuario es +++++ "+user);
            String descripcion = incidencia.getDescripcion();
            String codigo_pucp = String.valueOf(user.getId());
            String nombre = user.getNombre();
            String apellido = user.getApellido();
            String correo = user.getCorreo();
            String dni = user.getDni();

            if(descripcion.contains(codigo_pucp) || descripcion.contains(nombre) || descripcion.contains(apellido) || descripcion.contains(correo) || descripcion.contains(dni) ) {
                System.out.println("############################################### Estoy Aqui");
                String newDescripcion = descripcion.replace(codigo_pucp, "****");
                String newDescripcion2 = newDescripcion.replace(nombre, "****");
                String newDescripcion3 = newDescripcion2.replace(apellido, "****");
                String newDescripcion4 = newDescripcion3.replace(correo, "****");
                String newDescripcion5 = newDescripcion4.replace(dni, "****");
                incidencia.setDescripcion(newDescripcion5);
                System.out.println("La nueva descripción es " + newDescripcion5);
                incidencia.setDescripcion(newDescripcion5);
            }


            if(img.isEmpty()){
                System.out.println("######################################################################imagen vacia");
                model.addAttribute("imagenVacia", "imagenVacia");
                List<Tipo> listaTipo  =tipoRepository.findAll();
                List<Urgencia> listaUrgencia  =urgenciaRepository.findAll();
                List<Ubicacion> listaUbicacion = ubicacionRepository.findAll();
                model.addAttribute("listaTipo", listaTipo);
                model.addAttribute("listaUrgencia", listaUrgencia);
                model.addAttribute("listaUbicacion", listaUbicacion);
                model.addAttribute("errorCompany", "Ningún campo puede dejarse vacío, intente crear nuevamente por favor");
                if(incidencia.getIdtipo()!=null){
                    model.addAttribute("tipoInci",incidencia.getIdtipo().getId());
                    System.out.println(incidencia.getIdtipo().getId());
                }
                if(incidencia.getIdurgencia()!=null){
                    model.addAttribute("tipoUrg",incidencia.getIdurgencia().getId());
                    System.out.println(incidencia.getIdurgencia().getId());
                }
                if(incidencia.getEmMedica()!=null){
                    model.addAttribute("emMedicaa",incidencia.getEmMedica());
                    System.out.println(incidencia.getEmMedica());
                }
                if(incidencia.getUbicacion()!=null){
                    model.addAttribute("ubiId",incidencia.getUbicacion().getId());
                    System.out.println(incidencia.getUbicacion().getId());
                }
                return "usuario/newIncidencia";
            }else{
                try {
                    inicidenciaRepository.save(incidencia);
                    int i = incidencia.getId();
                    String idInci = String.valueOf(i);
                    byte[] bytes = img.getBytes();
                    Perfil perfil = new Perfil();
                    perfil.setName("Incidencia_"+idInci+".png");
                    String base64utput = faceBlur(Base64.getEncoder().encodeToString(bytes));
                    perfil.setFileBase64(base64utput);
                    System.out.println("llegue hasta aqui");

                    perfilDao.subirImagen(perfil);
                }catch (Exception e){
                    System.out.println("Hay excepcion");
                }
            }

            attr.addFlashAttribute("msg","Incidencia creada exitosamente.");
            return "redirect:/usuario/misIncidencias";
        }

    }

    @GetMapping("/detalle")
    public String detalleIncidencia(Model model,
                                    @RequestParam("id") Integer id, HttpSession session){

        Usuario u = (Usuario) session.getAttribute("usuario");
        String codigo = u.getId();
        System.out.println("El codigo es ++++ "+codigo+" y el id es "+id);
        Optional<Inicidencia> optInicidencia = Optional.ofNullable(inicidenciaRepository.obtenerIncidenciaUsuario(id, codigo));


        if (optInicidencia.isPresent()){
            Inicidencia inicidencia = optInicidencia.get();

            model.addAttribute("incidencia", inicidencia);
            model.addAttribute("imgInc",perfilDao.obtenerImagen("Incidencia_"+String.valueOf(id)).getFileBase64());
            model.addAttribute("imgUs",perfilDao.obtenerImagen(inicidencia.getCodigo().getId()).getFileBase64());
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
        if(incidencia.getNombre().equals("Nombre")){
            incidencia.setNombre("");
        }
        if(incidencia.getDescripcion().equals("Descripcion")){
            incidencia.setDescripcion("");
        }
        model.addAttribute("listaTipo", listaTipo);
        model.addAttribute("listaUrgencia", listaUrgencia);
        model.addAttribute("listaUbicacion", listaUbicacion);
        return "usuario/newIncidencia";
    }

    @GetMapping("/perfil")
    public String perfil(Model model, HttpSession session) {
        Usuario u = (Usuario) session.getAttribute("usuario");
        Usuario perfilUsuario = usuarioRepository.userPerfil(u.getId());
        model.addAttribute("user",perfilUsuario);
        model.addAttribute("imgperfil",perfilDao.obtenerImagen(u.getId()).getFileBase64());
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
                model.addAttribute("incidencia", incidencia);
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
            return "usuario/incidencias";
        }else {
            int max = incidencia.getMax_usuario();
            max+=1;
            Instant fecha = inicidenciaRepository.fecha();
            inicidenciaRepository.comentarIncidenciaUsuario(comentarioUsuario,max,id);
            comentarioRepository.comentarIncidenciaUsuario(comentarioUsuario, fecha,id);
            System.out.println("El correo del usuario es +++++++++++++ " + user.getCorreo());
            senderService.sendSimpleEmail(user.getCorreo(),"Información acerca de la Incidencia con ID "+incidencia.getId(),"Estimado usuario, su incidencia se encuentra en el estado de En Proceso. Pronto verá el nuevo comentario del miembro de seguridad. ");
            if(max==5){
                inicidenciaRepository.cambiarEstadoIncidencia(id);
                redirectAttributes.addFlashAttribute("msg2","Incidencia comentada. Se ha llegado al máximo de comentarios por incidencia. Estado: Resuelto.");
                return "redirect:/usuario/misIncidencias";
            }else{
                redirectAttributes.addFlashAttribute("msg3","Incidencia comentada. Recuerda que hay un máximo de 5 comentarios por incidencia.");
                return "redirect:/usuario/misIncidencias";
            }
        }
    }

    @PostMapping("/incidenciaResuelta")
    public String incidenciaResuelta(@RequestParam("id") Integer id, RedirectAttributes redirectAttributes, HttpSession session) {
        Usuario user = (Usuario) session.getAttribute("usuario");
        Optional<Inicidencia> optinicidencia = inicidenciaRepository.findById(id);
        if(optinicidencia.isPresent()){
            Inicidencia incidencia = optinicidencia.get();
            senderService.sendSimpleEmail(user.getCorreo(),"Información acerca de la Incidencia con ID "+incidencia.getId(),"Estimado usuario, su incidencia ha pasado al estado de Resuelto. ");
            inicidenciaRepository.cambiarEstadoIncidencia(id);
            redirectAttributes.addFlashAttribute("msg4","La incidencia con ID #"+id+" ha sido resuelta.");
            return "redirect:/usuario/misIncidencias";
        }else{
            return "usuario/misIncidencias";
        }
    }
    @PostMapping("/guardarImagenes")
    public String imagenSave(@RequestParam("fruta") String nombre ,@RequestParam(name = "f_subir",required = false) MultipartFile img, RedirectAttributes redirectAttributes, HttpSession session){
        int icono;
        if(nombre.equals("pina")){
            icono = 1;
        }else if(nombre.equals("sandia")){
            icono =2;
        }else{
            icono = 3;
        }
        Usuario user = (Usuario) session.getAttribute("usuario");
        usuarioRepository.saveAvatar(icono,user.getId());

        if(img.isEmpty()){
            System.out.println("imagen vacia");
        }else{
            try {
                byte[] bytes = img.getBytes();
                Perfil perfil = new Perfil();
                perfil.setName(user.getId()+".png");
                perfil.setFileBase64(Base64.getEncoder().encodeToString(bytes));
                System.out.println("llegue hasta aqui");
                perfilDao.subirImagen(perfil);
            }catch (Exception e){
                System.out.println("Hay excepcion");
            }

        }

        redirectAttributes.addFlashAttribute("mensaje","Se han realizado los cambios correctamente.");
        return "redirect:/usuario/perfil";
    }

    /*Face Blur function*/
    public String faceBlur(String base64Input) throws IOException{
        System.out.println(base64Input);
        String originalInput = "victor aponte:$ipucp123GTICS$";
        String credentials = Base64.getEncoder().encodeToString(originalInput.getBytes());

        JSONObject jo = new JSONObject();
        jo.put("base64_Photo_String", base64Input);
        jo.put("photo_url", "NO");
        System.out.println(jo);
        final String POST_PARAMS = jo.toString();

        URL obj = new URL("https://www.de-vis-software.ro/FaceBlurest.aspx");
        HttpURLConnection postConnection = (HttpURLConnection) obj.openConnection();
        postConnection.setRequestMethod("POST");
        postConnection.setRequestProperty("Authorization", "Basic " + credentials);
        postConnection.setRequestProperty("Content-Type", "application/json");
        postConnection.setRequestProperty("Accept", "application/json");
        postConnection.setDoOutput(true);
        OutputStream os = postConnection.getOutputStream();
        os.write(POST_PARAMS.getBytes());
        os.flush();
        os.close();
        int responseCode = postConnection.getResponseCode();

        if (responseCode == 200) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    postConnection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in .readLine()) != null) {
                response.append(inputLine);
            } in .close();
            // print json string result

            System.out.println("WORKEEEEEEEEED");
            String str = response.toString();

            String find = "final_photo";
            int i = str.indexOf(find);
            String outp = str.substring(i+find.length() + 3,str.length() - 2 );
            /*System.out.println("De aqui: " + outp + "HASTA AQUIIII");*/

            return outp;
        }
        else {
            System.out.println("POST NOT WORKED");
            return null;
        }
    }

}
