package br.com.fiap.teleconsulta.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // Ajuste conforme seu BD
    private static final String URL = "jdbc:postgresql://localhost:5432/teleconsulta";
    private static final String USER = "seu_usuario";
    private static final String PASS = "sua_senha";

    static {
        try {
            Class.forName("org.postgresql.Driver"); // ou com.mysql.cj.Driver
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao obter conexão com BD", e);
        }
    }
}
