package org.vehicles;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExcelConnection {
    private static ExcelConnection instance;
    private Workbook workbook;

    private ExcelConnection(String filePath) {
        try {
            FileInputStream fileInputStream = new FileInputStream(filePath);
            workbook = new XSSFWorkbook(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при подключении к Excel-файлу: " + e.getMessage());
        }
    }

    public static ExcelConnection getInstance(String filePath) {
        if (instance == null) {
            synchronized (ExcelConnection.class) {
                if (instance == null) {
                    instance = new ExcelConnection(filePath);
                }
            }
        }
        return instance;
    }

    public Workbook getWorkbook() {
        return workbook;
    }

    public void save(String filePath) {
        try (FileOutputStream fileOutputStream = new FileOutputStream(filePath)) {
            workbook.write(fileOutputStream);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сохранении Excel-файла: " + e.getMessage());
        }
    }
}
