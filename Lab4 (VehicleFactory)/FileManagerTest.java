package org.vehicles;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import org.vehicles.utils.ConcreteVehicle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class FileManagerTest {

    @Test
    public void testWriteToFile() throws IOException {
        Path tempFile = Files.createTempFile("vehicles2", ".txt");

        List<Vehicle> vehicleList = new ArrayList<>();
        vehicleList.add(new ConcreteVehicle(1, "Man", 120, 150, 500));
        vehicleList.add(new ConcreteVehicle(2, "Mercedes", 80, 200, 1000));

        FileManager.writeToTxt(tempFile.toString(), vehicleList);

        List<String> fileContent = Files.readAllLines(tempFile);

        assertEquals(2, fileContent.size());
        assertTrue(fileContent.get(0).contains("Man"));
        assertTrue(fileContent.get(1).contains("Mercedes"));

        Files.deleteIfExists(tempFile);
    }
}