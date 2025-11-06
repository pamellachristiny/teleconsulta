package br.com.fiap.teleconsulta.infra.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        // Correto: Lendo os NOMES das variáveis de ambiente
        String urlDeConexao = System.getenv("DB_URL_ORACLE");
        String login = System.getenv("DB_USER_ORACLE");
        String senha = System.getenv("DB_PASSWORD_ORACLE");

        // 1. Verificação de Variáveis de Ambiente (Configuração)
        if (urlDeConexao == null || login == null || senha == null) {
            throw new RuntimeException("As variáveis de ambiente do Oracle (DB_URL_ORACLE, DB_USER_ORACLE, DB_PASSWORD_ORACLE) não foram configuradas no Render.");
        }

        try {
            // 2. Tenta a Conexão
            return DriverManager.getConnection(urlDeConexao, login, senha);

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados Oracle: " + e.getMessage());
            // 3. Relança a exceção de conexão como uma exceção não checada (RuntimeException)
            throw new RuntimeException("Erro de conexão com o banco de dados Oracle.", e);
        }
    }
}