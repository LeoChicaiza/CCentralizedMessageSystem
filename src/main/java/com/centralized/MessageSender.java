package com.centralized;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class MessageSender {
    private static final String SHARED_FILE_PATH = "./shared/shared_file.txt";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/message_system";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "sayde.12";

    public static void main(String[] args) {
        String message = "Centralized Node A: Sending message";

        // 1. Escribir el mensaje en el archivo compartido
        try {
            File file = new File(SHARED_FILE_PATH);
            // Crear directorio y archivo si no existen
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }

            // Escribir el mensaje en el archivo
            try (FileWriter writer = new FileWriter(file, true)) {
                writer.write(message + "\n");
                System.out.println("Mensaje escrito en el archivo compartido: " + message);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 2. Insertar el mensaje en la base de datos
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.println("Conexión a la base de datos establecida");

            String sql = "INSERT INTO messages (message) VALUES (?)";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, message);
                int rowsAffected = stmt.executeUpdate();
                System.out.println("Mensaje insertado en la base de datos: " + message);
                System.out.println("Filas afectadas: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
