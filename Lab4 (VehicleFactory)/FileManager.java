package org.vehicles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.vehicles.utils.ConcreteVehicle;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class FileManager {

    public static List<Vehicle> readFromTxt(String filename) {
        List<Vehicle> vehicles = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                int id = Integer.parseInt(parts[0].trim());
                String type = parts[1].trim();
                int capacity = Integer.parseInt(parts[2].trim());
                int speed = Integer.parseInt(parts[3].trim());
                double price = Double.parseDouble(parts[4].trim());
                vehicles.add(new ConcreteVehicle(id, type, capacity, speed, price));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static void writeToTxt(String filename, List<Vehicle> vehicles) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            for (Vehicle vehicle : vehicles) {
                writer.write(vehicle.getId() + "," + vehicle.getType() + "," + vehicle.getCapacity() + "," +
                        vehicle.getSpeed() + "," + vehicle.getPrice() + "\n");
            }
            System.out.println("Данные успешно записаны в TXT файл.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static List<Vehicle> readFromXML(String filename) {
        List<Vehicle> vehicles = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(filename));

            NodeList vehicleNodes = document.getElementsByTagName("vehicle");

            for (int i = 0; i < vehicleNodes.getLength(); i++) {
                Node node = vehicleNodes.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    int id = Integer.parseInt(element.getElementsByTagName("id").item(0).getTextContent());
                    String type = element.getElementsByTagName("type").item(0).getTextContent();
                    int capacity = Integer.parseInt(element.getElementsByTagName("capacity").item(0).getTextContent());
                    int speed = Integer.parseInt(element.getElementsByTagName("speed").item(0).getTextContent());
                    double price = Double.parseDouble(element.getElementsByTagName("price").item(0).getTextContent());

                    vehicles.add(new ConcreteVehicle(id, type, capacity, speed, price));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return vehicles;
    }

    public static void writeToXML(String filename, List<Vehicle> vehicles) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.newDocument();

            Element root = document.createElement("vehicles");
            document.appendChild(root);

            for (Vehicle vehicle : vehicles) {
                Element vehicleElement = document.createElement("vehicle");

                Element id = document.createElement("id");
                id.appendChild(document.createTextNode(String.valueOf(vehicle.getId())));
                vehicleElement.appendChild(id);

                Element type = document.createElement("type");
                type.appendChild(document.createTextNode(vehicle.getType()));
                vehicleElement.appendChild(type);

                Element capacity = document.createElement("capacity");
                capacity.appendChild(document.createTextNode(String.valueOf(vehicle.getCapacity())));
                vehicleElement.appendChild(capacity);

                Element speed = document.createElement("speed");
                speed.appendChild(document.createTextNode(String.valueOf(vehicle.getSpeed())));
                vehicleElement.appendChild(speed);

                Element price = document.createElement("price");
                price.appendChild(document.createTextNode(String.valueOf(vehicle.getPrice())));
                vehicleElement.appendChild(price);

                root.appendChild(vehicleElement);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(filename));
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.transform(domSource, streamResult);

            System.out.println("Данные успешно записаны в XML файл.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<Vehicle> readFromJSON(String filename) {
        List<Vehicle> vehicles = new ArrayList<>();
        try (FileReader reader = new FileReader(filename)) {
            Gson gson = new Gson();
            Type vehicleListType = new TypeToken<ArrayList<ConcreteVehicle>>() {}.getType();
            vehicles = gson.fromJson(reader, vehicleListType);
        } catch (Exception e) {
        }
        return vehicles;
    }

    // Запись в JSON файл
    public static void writeToJSON(String filename, List<Vehicle> vehicles) {
        try (FileWriter writer = new FileWriter(filename)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(vehicles, writer);
            System.out.println("Данные успешно записаны в JSON файл.");
        } catch (Exception e) {
        }
    }

    public static void writeEncryptedToTxt(String filename, List<Vehicle> vehicles, SecretKey secretKey) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            StringBuilder data = new StringBuilder();

            for (Vehicle vehicle : vehicles) {
                String vehicleData = vehicle.getId() + "," + vehicle.getType() + "," + vehicle.getCapacity() + "," +
                        vehicle.getSpeed() + "," + vehicle.getPrice() + "\n";
                data.append(vehicleData);
            }

            String encryptedData = EncryptionUtils.encrypt(data.toString(), secretKey);

            writer.write(encryptedData);
            System.out.println("Данные успешно зашифрованы и записаны в файл " + filename);
        } catch (IOException e) {
        }
    }

    public static List<Vehicle> readDecryptedFromTxt(String filename, SecretKey secretKey) {
        List<Vehicle> vehicles = new ArrayList<>();

        if (secretKey == null) {
            System.out.println("Ошибка: secretKey не может быть null.");
            return vehicles;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {

            StringBuilder encryptedData = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                encryptedData.append(line);
            }

            if (encryptedData.length() == 0) {
                System.out.println("Файл пуст: " + filename);
                return vehicles; // Возврат пустого списка
            }

            String decryptedData = EncryptionUtils.decrypt(encryptedData.toString(), secretKey);

            String[] vehicleLines = decryptedData.split("\n");
            for (String vehicleLine : vehicleLines) {
                String[] parts = vehicleLine.split(",");
                if (parts.length < 5) {
                    System.out.println("Неполные данные: " + vehicleLine);
                    continue;
                }

                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String type = parts[1].trim();
                    int capacity = Integer.parseInt(parts[2].trim());
                    int speed = Integer.parseInt(parts[3].trim());
                    double price = Double.parseDouble(parts[4].trim());

                    vehicles.add(new ConcreteVehicle(id, type, capacity, speed, price));
                } catch (NumberFormatException e) {
                    System.out.println("Ошибка формата данных в строке: " + vehicleLine);
                }
            }

            System.out.println("Данные успешно расшифрованы и прочитаны из файла " + filename);
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла: " + e.getMessage());
        }

        return vehicles;
    }

    public static void saveDataWithEncryptionAndZip(List<Vehicle> vehicles) {
        String encryptedFileName = "vehicles.txt";

        try (FileOutputStream fos = new FileOutputStream("vehicles.zip");
             ZipOutputStream zipOut = new ZipOutputStream(fos)) {
            File fileToZip = new File(encryptedFileName);
            FileInputStream fis = new FileInputStream(fileToZip);
            ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
            zipOut.putNextEntry(zipEntry);

            byte[] bytes = new byte[1024];
            int length;
            while ((length = fis.read(bytes)) >= 0) {
                zipOut.write(bytes, 0, length);
            }
            zipOut.closeEntry();
            fis.close();

            System.out.println("Данные заархивированы в ZIP файл.");
        } catch (IOException e) {

        }
    }
    public static List<Vehicle> readFromExcel(String filePath) {
        List<Vehicle> vehicles = new ArrayList<>();
        ExcelConnection excelConnection = ExcelConnection.getInstance(filePath);
        Workbook workbook = excelConnection.getWorkbook();

        Sheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            try {
                if (row.getRowNum() == 0) continue;

                int id = (int) row.getCell(0).getNumericCellValue();
                String type = row.getCell(1).getStringCellValue();
                int capacity = (int) row.getCell(2).getNumericCellValue();
                int speed = (int) row.getCell(3).getNumericCellValue();
                double price = row.getCell(4).getNumericCellValue();

                vehicles.add(new ConcreteVehicle(id, type, capacity, speed, price));
            } catch (Exception e) {
                System.err.println("Ошибка чтения строки " + row.getRowNum() + ": " + e.getMessage());
            }
        }
        return vehicles;
    }

    public static void writeToExcel(String filePath, List<Vehicle> vehicles) {
        ExcelConnection excelConnection = ExcelConnection.getInstance(filePath);
        Workbook workbook = excelConnection.getWorkbook();
        Sheet sheet = workbook.createSheet("Vehicles");

        Row header = sheet.createRow(0);
        header.createCell(0).setCellValue("ID");
        header.createCell(1).setCellValue("Type");
        header.createCell(2).setCellValue("Capacity");
        header.createCell(3).setCellValue("Speed");
        header.createCell(4).setCellValue("Price");

        int rowIndex = 1;
        for (Vehicle vehicle : vehicles) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(vehicle.getId());
            row.createCell(1).setCellValue(vehicle.getType());
            row.createCell(2).setCellValue(vehicle.getCapacity());
            row.createCell(3).setCellValue(vehicle.getSpeed());
            row.createCell(4).setCellValue(vehicle.getPrice());
        }

        excelConnection.save(filePath);
        System.out.println("Данные успешно записаны в Excel файл.");
    }
}