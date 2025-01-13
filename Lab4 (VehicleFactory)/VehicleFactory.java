package org.vehicles;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.vehicles.utils.ConcreteVehicle;

import static org.vehicles.FileManager.evaluateExpression;

public class VehicleFactory {

    private static final String ALGORITHM = "AES";
    private static final String KEY_FILE = "secretKey.key";
    private static VehicleCollection vehicleCollection = new VehicleList();
    static SecretKey key;

    public static void saveKeyToFile(SecretKey secretKey) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(KEY_FILE)) {
            fos.write(secretKey.getEncoded());
        }
    }

    public static SecretKey loadKeyFromFile() throws IOException {
        byte[] keyBytes = new byte[32];
        FileInputStream fis = new FileInputStream(KEY_FILE);
        fis.read(keyBytes);
        return new SecretKeySpec(keyBytes, ALGORITHM);
    }


    public static void main(String[] args) throws IOException {
        try {
            key = loadKeyFromFile();
            System.out.println("Ключ загружен из файла.");
        } catch (IOException e) {
            System.out.println("Ключ не найден, генерируем новый.");
            key = EncryptionUtils.generateKey();
            System.out.println("Новый ключ сгенерирован и сохранен.");
        }
        System.out.println(key);
        Scanner scanner = new Scanner(System.in);
        String txtFilename = "vehicles.txt";
        String xmlFilename = "vehicles.xml";
        String jsonFilename = "vehicles.json";
        String excelFilename = "vehicles.xlsx";

        while (true) {
            System.out.println("1. Добавить автомобиль");
            System.out.println("2. Показать все автомобили");
            System.out.println("3. Сохранить и зашифровать данные");
            System.out.println("4. Прочитать и расшифровать данные");
            System.out.println("5. Сохранить данные и создать архив (ZIP)");
            System.out.println("6. Удалить автомобиль");
            System.out.println("7. Обновить автомобиль");
            System.out.println("8. Сортировать по цене");
            System.out.println("9. Сортировать по вместимости");
            System.out.println("10. Сохранить автомобили в файл");
            System.out.println("11. Прочитать автомобили из файла");
            System.out.println("12. Выход");

            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    addVehicle(scanner);
                    break;
                case 2:
                    printAllVehicles();
                    break;
                case 3:
                    saveAndEncryptData();
                    break;
                case 4:
                    readAndDecryptData();
                    break;
                case 5:
                    saveDataAndCreateZip();
                    break;
                case 6:
                    removeVehicle(scanner);
                    break;
                case 7:
                    updateVehicle(scanner);
                    break;
                case 8:
                    sortVehiclesByPrice();
                    break;
                case 9:
                    sortVehiclesByCapacity();
                    break;
                case 10:
                    System.out.println("1. Сохранить в txt");
                    System.out.println("2. Сохранить в xml");
                    System.out.println("3. Сохранить в json");
                    System.out.println("4. Сохранить в xlsx");
                    int choice2 = scanner.nextInt();
                    List<Vehicle> vehicles = vehicleCollection.getAllVehicles();
                    switch (choice2) {
                        case 1:
                            FileManager.writeToTxt(txtFilename, vehicles);
                            System.out.println("Данные успешно сохранены в файл.");
                            break;
                        case 2:
                            FileManager.writeToXML(xmlFilename, vehicles);
                            System.out.println("Данные успешно сохранены в файл.");
                            break;
                        case 3:
                            FileManager.writeToJSON(jsonFilename, vehicles);
                            System.out.println("Данные успешно сохранены в файл.");
                            break;
                        case 4:
                            FileManager.writeToExcel(excelFilename, vehicles);
                            System.out.println("Данные успешно сохранены в файл.");
                            break;
                        default:
                            System.out.println("Неверный ввод");
                            break;
                    }
                    break;
                case 11:
                    System.out.println("1. Прочитать из txt");
                    System.out.println("2. Прочитать из xml");
                    System.out.println("3. Прочитать из json");
                    System.out.println("4. Прочитать из xlsx");
                    int choice3 = scanner.nextInt();
                    switch (choice3) {
                        case 1:
                            vehicleCollection.getAllVehicles().clear();
                            vehicleCollection.getAllVehicles().addAll(FileManager.readFromTxt(txtFilename));
                            System.out.println("Данные успешно прочитаны из файла.");
                            printAllVehicles();
                            break;
                        case 2:
                            vehicleCollection.getAllVehicles().clear();
                            vehicleCollection.getAllVehicles().addAll(FileManager.readFromXML(xmlFilename));
                            System.out.println("Данные успешно прочитаны из файла.");
                            printAllVehicles();
                            break;
                        case 3:
                            vehicleCollection.getAllVehicles().clear();
                            vehicleCollection.getAllVehicles().addAll(FileManager.readFromJSON(jsonFilename));
                            System.out.println("Данные успешно прочитаны из файла.");
                            printAllVehicles();
                            break;
                        case 4:
                            vehicleCollection.getAllVehicles().clear();
                            vehicleCollection.getAllVehicles().addAll(FileManager.readFromExcel(excelFilename));
                            System.out.println("Данные успешно прочитаны из файла.");
                            printAllVehicles();
                            break;
                        default:
                            System.out.println("Неверный ввод");
                            break;
                    }
                    break;
                case 12:
                    System.out.println("Выход из программы.");
                    saveKeyToFile(key);
                    return;
                default:
                    System.out.println("Неверный ввод. Пожалуйста, попробуйте снова.");
            }
        }
    }

    private static void addVehicle(Scanner scanner) {
        System.out.println("Введите id автомобиля:");
        String idExpression = scanner.next();
        int id = (int) evaluateExpression(idExpression);

        System.out.println("Введите модель автомобиля:");
        String type = scanner.next();

        System.out.println("Введите вместимость:");
        String capacityExpression = scanner.next();
        int capacity = (int) evaluateExpression(capacityExpression);

        System.out.println("Введите скорость:");
        String speedExpression = scanner.next();
        int speed = (int) evaluateExpression(speedExpression);

        System.out.println("Введите цену:");
        String priceExpression = scanner.next();
        double price = evaluateExpression(priceExpression);

        Vehicle newVehicle = new ConcreteVehicle(id, type, capacity, speed, price);

        System.out.println("Добавить страховку? (y/n)");
        String addInsurance = scanner.next();
        if (addInsurance.equalsIgnoreCase("y")) {
            System.out.println("Введите стоимость страховки:");
            String insuranceCostExpression = scanner.next();
            double insuranceCost = evaluateExpression(insuranceCostExpression);
            newVehicle = new InsuranceDecorator(newVehicle, insuranceCost);
        }

        System.out.println("Добавить расширенную гарантию? (y/n)");
        String addWarranty = scanner.next();
        if (addWarranty.equalsIgnoreCase("y")) {
            System.out.println("Введите стоимость гарантии:");
            String warrantyCostExpression = scanner.next();
            double warrantyCost = evaluateExpression(warrantyCostExpression);
            newVehicle = new WarrantyDecorator(newVehicle, warrantyCost);
        }

        vehicleCollection.addVehicle(newVehicle);
        System.out.println("Автомобиль успешно добавлен: " + newVehicle);
    }



    private static void removeVehicle(Scanner scanner) {
        System.out.println("Введите id автомобиля для удаления:");
        int id = scanner.nextInt();
        vehicleCollection.removeVehicle(id);
    }

    private static void updateVehicle(Scanner scanner) {
        System.out.println("Введите id автомобиля для обновления:");
        int id = scanner.nextInt();
        Vehicle vehicle = vehicleCollection.getVehicleById(id);

        if (vehicle != null) {
            System.out.println("Введите новую модель автомобиля:");
            String type = scanner.next();

            System.out.println("Введите вместимость автомобиля:");
            String capacityExpression = scanner.next();
            int capacity = (int) evaluateExpression(capacityExpression);

            System.out.println("Введите скорость:");
            String speedExpression = scanner.next();
            int speed = (int) evaluateExpression(speedExpression);

            System.out.println("Введите новую цену:");
            String priceExpression = scanner.next();
            double price = evaluateExpression(priceExpression);

            vehicle.setType(type);
            vehicle.setCapacity(capacity);
            vehicle.setSpeed(speed);
            vehicle.setPrice(price);

            System.out.println("Информация об автомобиле успешно обновлена: " + vehicle);
        } else {
            System.out.println("Автомобиль с таким id не найден.");
        }
    }

    private static void printAllVehicles() {
        List<Vehicle> vehicles = vehicleCollection.getAllVehicles();
        if (vehicles.isEmpty()) {
            System.out.println("Автомобили отсутствуют.");
        } else {
            for (Vehicle vehicle : vehicles) {
                System.out.println(vehicle);
            }
        }
    }

    private static void sortVehiclesByPrice() {
        List<Vehicle> vehicles = vehicleCollection.getAllVehicles();
        vehicles.sort(Comparator.comparingDouble(Vehicle::getPrice));
        System.out.println("Автомобили отсортированы по цене:");
        printAllVehicles();
    }

    private static void sortVehiclesByCapacity() {
        List<Vehicle> vehicles = vehicleCollection.getAllVehicles();
        vehicles.sort(Comparator.comparingInt(Vehicle::getCapacity));
        System.out.println("Автомобили отсортированы по вместимости:");
        printAllVehicles();
    }

    private static void saveAndEncryptData() {
        FileManager.writeEncryptedToTxt("encrypted.txt", vehicleCollection.getAllVehicles(), key);
        System.out.println("Данные успешно зашифрованы и сохранены.");
    }

    private static void readAndDecryptData() {
        List<Vehicle> decryptedVehicles = FileManager.readDecryptedFromTxt("encrypted.txt", key);
        vehicleCollection.getAllVehicles().addAll(decryptedVehicles);
        printAllVehicles();
    }

    private static void saveDataAndCreateZip() {
        FileManager.saveDataWithEncryptionAndZip(vehicleCollection.getAllVehicles());
    }
}