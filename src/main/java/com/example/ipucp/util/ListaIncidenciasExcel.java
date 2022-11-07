package com.example.ipucp.util;


import com.example.ipucp.Entity.Inicidencia;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import static org.apache.poi.ss.util.CellUtil.createCell;

public class ListaIncidenciasExcel  {

    private XSSFWorkbook workbook;
    private XSSFSheet sheet;
    List<Inicidencia> inicidenciaList;

    public ListaIncidenciasExcel(List<Inicidencia> inicidenciaList) {
        this.inicidenciaList = inicidenciaList;
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderRow(){
        sheet = workbook.createSheet("Incidencias");

        Row row = sheet.createRow(0);

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        style.setFont(font);

        createCell(row, 0, "Título", style);
        createCell(row, 1, "Tipo", style);
        createCell(row, 2, "Ubicación", style);
        createCell(row, 3, "Usuario", style);
        createCell(row, 4, "Fecha", style);
        createCell(row, 5, "Destacados", style);
    }

    private void createCell(Row row, int columnCount, Object value, CellStyle style) {
        sheet.autoSizeColumn(columnCount);
        Cell cell = row.createCell(columnCount);
        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        }else {
            cell.setCellValue((String) value);
        }
        cell.setCellStyle(style);
    }

    private void writeDataRow(){
        int rowCount = 1;

        CellStyle style = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        style.setFont(font);

        for (Inicidencia incidencia : inicidenciaList) {
            Row row = sheet.createRow(rowCount++);
            int columnCount = 0;

            createCell(row, columnCount++, incidencia.getNombre(), style);
            createCell(row, columnCount++, incidencia.getIdtipo().getTipoIncidencia(), style);
            createCell(row, columnCount++, incidencia.getUbicacion().getNombre(), style);
            createCell(row, columnCount++, incidencia.getCodigo().getNombre()+" "+incidencia.getCodigo().getApellido(), style);
            createCell(row, columnCount++, incidencia.getFecha().toString().split(String.valueOf('T'))[0], style);
            createCell(row, columnCount++, String.valueOf(incidencia.getDestacado()), style);

        }
    }

    public void export(HttpServletResponse response) throws IOException {
        writeHeaderRow();
        writeDataRow();

        ServletOutputStream outputStream = response.getOutputStream();
        workbook.write(outputStream);
        workbook.close();
        outputStream.close();
    }
}
