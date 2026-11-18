package com.uniajc.servicios;

import com.uniajc.modelo.Operacion;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

/**
 * Servicio encargado de generar y actualizar el archivo Registros_ARES.xlsx
 * con diseño ejecutivo a partir de la lista real de operaciones en PostgreSQL.
 */
public class ExportadorExcel {

    public static final String NOMBRE_ARCHIVO = "Registros_ARES.xlsx";

    public static File exportarOperaciones(List<Operacion> operaciones) throws IOException {
        File archivo = new File(NOMBRE_ARCHIVO);

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet hoja = workbook.createSheet("Operaciones ARES");

            // 1. Estilo de Cabecera: Fondo oscuro, texto blanco en negrita y centrado total
            CellStyle estiloCabecera = workbook.createCellStyle();
            estiloCabecera.setFillForegroundColor(IndexedColors.GREY_80_PERCENT.getIndex());
            estiloCabecera.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            estiloCabecera.setAlignment(HorizontalAlignment.CENTER);
            estiloCabecera.setVerticalAlignment(VerticalAlignment.CENTER);
            aplicarBordes(estiloCabecera);

            Font fuenteCabecera = workbook.createFont();
            fuenteCabecera.setBold(true);
            fuenteCabecera.setColor(IndexedColors.WHITE.getIndex());
            fuenteCabecera.setFontHeightInPoints((short) 11);
            estiloCabecera.setFont(fuenteCabecera);

            // 2. Estilo general de celdas de datos: Centrado horizontal y vertical
            CellStyle estiloDatos = workbook.createCellStyle();
            estiloDatos.setAlignment(HorizontalAlignment.CENTER);
            estiloDatos.setVerticalAlignment(VerticalAlignment.CENTER);
            aplicarBordes(estiloDatos);

            // 3. Estilo para columna Costo: Moneda con separador de miles y centrado total
            DataFormat formatoDatos = workbook.createDataFormat();
            CellStyle estiloCosto = workbook.createCellStyle();
            estiloCosto.cloneStyleFrom(estiloDatos);
            estiloCosto.setDataFormat(formatoDatos.getFormat("$ #,##0"));

            String[] cabeceras = {
                "ID", "GUÍA", "TIPO", "ID TRANSPORTADORA", "ID USUARIO",
                "REMITENTE", "DESTINATARIO", "PESO (KG)", "COSTO ($)",
                "ESTADO", "FECHA", "HORA", "OBSERVACIONES"
            };

            // Crear fila de cabeceras con altura ejecutiva
            Row filaCabecera = hoja.createRow(0);
            filaCabecera.setHeightInPoints(26f);
            for (int i = 0; i < cabeceras.length; i++) {
                Cell celda = filaCabecera.createCell(i);
                celda.setCellValue(cabeceras[i]);
                celda.setCellStyle(estiloCabecera);
            }

            // Llenar filas con los datos reales de Operacion.java
            int numFila = 1;
            if (operaciones != null) {
                for (Operacion op : operaciones) {
                    Row fila = hoja.createRow(numFila++);
                    fila.setHeightInPoints(21f);

                    crearCeldaNumero(fila, 0, op.getId(), estiloDatos);
                    crearCeldaTexto(fila, 1, op.getGuia(), estiloDatos);
                    crearCeldaTexto(fila, 2, op.getTipo(), estiloDatos);
                    crearCeldaNumero(fila, 3, op.getIdTransportadora(), estiloDatos);
                    crearCeldaNumero(fila, 4, op.getIdUsuario(), estiloDatos);
                    crearCeldaTexto(fila, 5, op.getRemitente(), estiloDatos);
                    crearCeldaTexto(fila, 6, op.getDestinatario(), estiloDatos);

                    if (op.getPeso() != null) {
                        crearCeldaDecimal(fila, 7, op.getPeso().doubleValue(), estiloDatos);
                    } else {
                        crearCeldaTexto(fila, 7, "N/A", estiloDatos);
                    }

                    // Columna Costo formateada con separador de miles / moneda
                    Cell celdaCosto = fila.createCell(8);
                    celdaCosto.setCellValue(op.getCosto() != null ? op.getCosto().doubleValue() : 0.0);
                    celdaCosto.setCellStyle(estiloCosto);

                    crearCeldaTexto(fila, 9, op.getEstado(), estiloDatos);
                    crearCeldaTexto(fila, 10, op.getFecha() != null ? op.getFecha().toString() : "", estiloDatos);
                    crearCeldaTexto(fila, 11, op.getHora() != null ? op.getHora().toString() : "", estiloDatos);
                    crearCeldaTexto(fila, 12, op.getObservaciones() != null ? op.getObservaciones() : "-", estiloDatos);
                }
            }

            // Autoajuste de ancho de columnas con margen extra generoso para evitar textos pegados
            for (int i = 0; i < cabeceras.length; i++) {
                hoja.autoSizeColumn(i);
                int anchoActual = hoja.getColumnWidth(i);
                int anchoConMargen = Math.max(anchoActual + (10 * 256), 16 * 256);
                hoja.setColumnWidth(i, Math.min(anchoConMargen, 60 * 256));
            }

            // Guardar archivo en la raíz del proyecto
            try (FileOutputStream fos = new FileOutputStream(archivo)) {
                workbook.write(fos);
            }
        }

        return archivo.getAbsoluteFile();
    }

    private static void crearCeldaTexto(Row fila, int columna, String valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor != null && !valor.isBlank() ? valor : "");
        celda.setCellStyle(estilo);
    }

    private static void crearCeldaNumero(Row fila, int columna, int valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }

    private static void crearCeldaDecimal(Row fila, int columna, double valor, CellStyle estilo) {
        Cell celda = fila.createCell(columna);
        celda.setCellValue(valor);
        celda.setCellStyle(estilo);
    }

    private static void aplicarBordes(CellStyle estilo) {
        estilo.setBorderTop(BorderStyle.THIN);
        estilo.setBorderBottom(BorderStyle.THIN);
        estilo.setBorderLeft(BorderStyle.THIN);
        estilo.setBorderRight(BorderStyle.THIN);
        short colorBorde = IndexedColors.GREY_40_PERCENT.getIndex();
        estilo.setTopBorderColor(colorBorde);
        estilo.setBottomBorderColor(colorBorde);
        estilo.setLeftBorderColor(colorBorde);
        estilo.setRightBorderColor(colorBorde);
    }
}
