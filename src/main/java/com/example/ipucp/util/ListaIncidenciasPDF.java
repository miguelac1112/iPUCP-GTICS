package com.example.ipucp.util;

import com.example.ipucp.Entity.Inicidencia;
import com.example.ipucp.Entity.Orden;
import com.example.ipucp.Entity.Tipo;
import com.example.ipucp.Entity.Urgencia;
import com.example.ipucp.Repository.*;
import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPTable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfWriter;

@Component("seguridad/incidencias")
public class ListaIncidenciasPDF extends AbstractPdfView {

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

    @Override
    protected void buildPdfDocument(Map<String, Object> model, Document document, PdfWriter writer,
                HttpServletRequest request, HttpServletResponse response) throws Exception {

        System.out.println("asas");

        int idtipoI = (int) model.get("idtipoI");
        int idUrgI = (int) model.get("idUrgI");
        int idOrdenI = (int) model.get("idOrdenI");
        int idEstad = (int) model.get("idEstad");
        List<Inicidencia> inicidenciaList = (List<Inicidencia>) model.get("ListaIncidencias");
        List<Tipo> listaTipos = (List<Tipo>) model.get("ListaTipos");
        List<Urgencia> listaUrg = (List<Urgencia>) model.get("ListaUrgencia");
        List<Orden> listaOrden = (List<Orden>) model.get("listaOrden");
        List<Integer> listaEstados = (List<Integer>) model.get("ListaEstado");

        /*Tamaños*/
        Font fuenteTituloColumnas = FontFactory.getFont(FontFactory.HELVETICA_BOLD,12,Color.BLUE);

        document.setPageSize(PageSize.LETTER.rotate());
        document.setMargins(20,20,40,20);
        document.open();

        PdfPTable tablaTitulo = new PdfPTable(1);
        PdfPCell celda = null;
        celda = new PdfPCell(new Phrase("Listado de Incidencias"));
        celda.setBorder(0);
        celda.setBackgroundColor(new Color(40,190,138));
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(15);

        tablaTitulo.addCell(celda);
        tablaTitulo.setSpacingAfter(25);

        PdfPTable tablaIncidencias = new PdfPTable(6);
        tablaIncidencias.setWidths(new float[] {1f,1f,1f,1f,1f,1f});

        celda = new PdfPCell(new Phrase("Titulo",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        celda = new PdfPCell(new Phrase("Tipo",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        celda = new PdfPCell(new Phrase("Ubicación",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        celda = new PdfPCell(new Phrase("Usuario",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        celda = new PdfPCell(new Phrase("Fecha",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        celda = new PdfPCell(new Phrase("Destacados",fuenteTituloColumnas));
        celda.setBackgroundColor(Color.LIGHT_GRAY);
        celda.setHorizontalAlignment(Element.ALIGN_CENTER);
        celda.setVerticalAlignment(Element.ALIGN_CENTER);
        celda.setPadding(10);
        tablaIncidencias.addCell(celda);

        inicidenciaList.forEach(incidencia->{
            tablaIncidencias.addCell(incidencia.getNombre());
            tablaIncidencias.addCell(incidencia.getIdtipo().getTipoIncidencia());
            tablaIncidencias.addCell(incidencia.getUbicacion().getNombre());
            tablaIncidencias.addCell(incidencia.getCodigo().getNombre()+" "+incidencia.getCodigo().getApellido());
            tablaIncidencias.addCell(incidencia.getFecha().toString().split(String.valueOf('T'))[0]);
            tablaIncidencias.addCell(String.valueOf(incidencia.getDestacado()));
        });


        document.add(tablaTitulo);
        document.add(tablaIncidencias);

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
}
