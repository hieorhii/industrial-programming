package org.vehicles;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.vehicles.utils.ConcreteVehicle;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.util.List;

public class VehicleFactoryGUI extends Application {

    private static VehicleCollection vehicleCollection = new VehicleList();
    private static SecretKey key;

    public static void main(String[] args) {
        // Загрузка ключа перед запуском GUI
        try {
            key = VehicleFactory.loadKeyFromFile();
        } catch (IOException e) {
            try {
                key = EncryptionUtils.generateKey();
                VehicleFactory.saveKeyToFile(key);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }

        // Запуск JavaFX
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Vehicle Factory");

        // Основной макет
        BorderPane root = new BorderPane();

        // Список автомобилей
        ListView<String> vehicleListView = new ListView<>();
        updateVehicleList(vehicleListView);

        // Кнопки управления
        Button addButton = new Button("Добавить");
        Button removeButton = new Button("Удалить");
        Button saveButton = new Button("Сохранить");
        Button loadButton = new Button("Загрузить");

        HBox buttonBox = new HBox(10, addButton, removeButton, saveButton, loadButton);
        buttonBox.setPadding(new Insets(10));
        buttonBox.setStyle("-fx-background-color: #f4f4f4;");

        // Добавление элементов на экран
        root.setCenter(vehicleListView);
        root.setBottom(buttonBox);

        // Обработчики событий
        addButton.setOnAction(e -> showAddVehicleDialog(vehicleListView));
        removeButton.setOnAction(e -> {
            String selectedVehicle = vehicleListView.getSelectionModel().getSelectedItem();
            if (selectedVehicle != null) {
                int id = Integer.parseInt(selectedVehicle.split(":")[0].trim());
                vehicleCollection.removeVehicle(id);
                updateVehicleList(vehicleListView);
            }
        });
        saveButton.setOnAction(e -> {
            FileManager.writeEncryptedToTxt("encrypted.txt", vehicleCollection.getAllVehicles(), key);
            showAlert(Alert.AlertType.INFORMATION, "Сохранение", "Данные успешно зашифрованы и сохранены.");
        });
        loadButton.setOnAction(e -> {
            List<Vehicle> vehicles = FileManager.readDecryptedFromTxt("encrypted.txt", key);
            vehicleCollection.getAllVehicles().clear();
            vehicleCollection.getAllVehicles().addAll(vehicles);
            updateVehicleList(vehicleListView);
            showAlert(Alert.AlertType.INFORMATION, "Загрузка", "Данные успешно расшифрованы и загружены.");
        });

        // Настройка сцены и показ окна
        Scene scene = new Scene(root, 600, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateVehicleList(ListView<String> vehicleListView) {
        vehicleListView.getItems().clear();
        for (Vehicle vehicle : vehicleCollection.getAllVehicles()) {
            vehicleListView.getItems().add(vehicle.getId() + ": " + vehicle.getType() + " - $" + vehicle.getPrice());
        }
    }

    private void showAddVehicleDialog(ListView<String> vehicleListView) {
        // Создание диалога для добавления автомобиля
        Dialog<Vehicle> dialog = new Dialog<>();
        dialog.setTitle("Добавить автомобиль");

        // Поля ввода
        TextField idField = new TextField();
        TextField typeField = new TextField();
        TextField capacityField = new TextField();
        TextField speedField = new TextField();
        TextField priceField = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        grid.add(new Label("ID:"), 0, 0);
        grid.add(idField, 1, 0);
        grid.add(new Label("Модель:"), 0, 1);
        grid.add(typeField, 1, 1);
        grid.add(new Label("Вместимость:"), 0, 2);
        grid.add(capacityField, 1, 2);
        grid.add(new Label("Скорость:"), 0, 3);
        grid.add(speedField, 1, 3);
        grid.add(new Label("Цена:"), 0, 4);
        grid.add(priceField, 1, 4);

        dialog.getDialogPane().setContent(grid);

        ButtonType addButtonType = new ButtonType("Добавить", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addButtonType, ButtonType.CANCEL);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == addButtonType) {
                try {
                    int id = Integer.parseInt(idField.getText());
                    String type = typeField.getText();
                    int capacity = Integer.parseInt(capacityField.getText());
                    int speed = Integer.parseInt(speedField.getText());
                    double price = Double.parseDouble(priceField.getText());
                    return new ConcreteVehicle(id, type, capacity, speed, price);
                } catch (NumberFormatException e) {
                    showAlert(Alert.AlertType.ERROR, "Ошибка", "Пожалуйста, введите корректные данные.");
                }
            }
            return null;
        });

        dialog.showAndWait().ifPresent(vehicle -> {
            vehicleCollection.addVehicle(vehicle);
            updateVehicleList(vehicleListView);
        });
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
