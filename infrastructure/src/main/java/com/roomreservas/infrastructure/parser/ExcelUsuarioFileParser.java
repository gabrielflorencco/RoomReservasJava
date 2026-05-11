package com.roomreservas.infrastructure.parser;

import com.roomreservas.application.dto.request.CriarUsuarioRequest;
import com.roomreservas.application.port.in.UsuarioFileParser;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Component
public class ExcelUsuarioFileParser implements UsuarioFileParser {

    @Override
    public List<CriarUsuarioRequest> parse(InputStream inputStream) {

        try (
            Workbook workbook = new XSSFWorkbook(inputStream)
        ) {

            Sheet sheet = workbook.getSheetAt(0);

            List<CriarUsuarioRequest> usuarios = new ArrayList<>();

            Iterator<Row> rows = sheet.iterator();

            // Ignora cabeçalho
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {

                Row row = rows.next();

                String nome = getCellValue(row.getCell(0));
                String email = getCellValue(row.getCell(1));

                if (nome.isBlank() || email.isBlank()) {
                    continue;
                }

                usuarios.add(
                    new CriarUsuarioRequest(
                        nome,
                        email
                    )
                );
            }

            return usuarios;

        } catch (Exception ex) {
            throw new RuntimeException(
                "Erro ao processar arquivo Excel",
                ex
            );
        }
    }

    private String getCellValue(Cell cell) {

        if (cell == null) {
            return "";
        }

        return switch (cell.getCellType()) {

            case STRING ->
                cell.getStringCellValue().trim();

            case NUMERIC ->
                String.valueOf(cell.getNumericCellValue());

            case BOOLEAN ->
                String.valueOf(cell.getBooleanCellValue());

            case FORMULA ->
                cell.getCellFormula();

            default ->
                "";
        };
    }
}