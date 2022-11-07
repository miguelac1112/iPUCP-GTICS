package com.example.ipucp.util;

import java.awt.Color;
import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import com.example.ipucp.Entity.Inicidencia;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;


public class ListaIncidenciasPDF {

    private List<Inicidencia> inicidenciaList;

    public ListaIncidenciasPDF(List<Inicidencia> inicidenciaList) {
        this.inicidenciaList = inicidenciaList;
    }

    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);

        cell.setPhrase(new Phrase("Título", font));

        table.addCell(cell);

        cell.setPhrase(new Phrase("Tipo", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Ubicación", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Usuario", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Fecha", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Destacados", font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table) {
        for (Inicidencia incidencia : inicidenciaList) {
            table.addCell(incidencia.getNombre());
            table.addCell(incidencia.getIdtipo().getTipoIncidencia());
            table.addCell(incidencia.getUbicacion().getNombre());
            table.addCell(incidencia.getCodigo().getNombre()+" "+incidencia.getCodigo().getApellido());
            table.addCell(incidencia.getFecha().toString().split(String.valueOf('T'))[0]);
            table.addCell(String.valueOf(incidencia.getDestacado()));
        }
    }

    public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();
        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setSize(18);
        font.setColor(Color.BLUE);

        Paragraph p = new Paragraph("Lista de Incidencias", font);
        p.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(p);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1f, 1f, 1f, 1f, 1f, 1f});
        table.setSpacingBefore(10);

        writeTableHeader(table);
        writeTableData(table);

        document.add(table);

        document.close();

    }

}
