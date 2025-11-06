package br.com.fiap.teleconsulta.infra.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    public Connection getConnection() {
        // --- CHAVES PARA O RENDER ---
        // O Render precisa que estas variáveis sejam configuradas no painel.
        String urlDeConexao = System.getenv("DB_URL_ORACLE"); // URL completa do Oracle
        String login = System.getenv("DB_USER_ORACLE");
        String senha = System.getenv("DB_PASSWORD_ORACLE");

        if (urlDeConexao == null || login == null || senha == null) {
            throw new RuntimeException("As variáveis de ambiente do Oracle (DB_URL_ORACLE, DB_USER_ORACLE, DB_PASSWORD_ORACLE) não foram configuradas.");
        }

        try {
            // Tenta carregar o driver (dependendo da versão do JDBC)
            // Class.forName("oracle.jdbc.driver.OracleDriver");

            // Tenta a conexão com as credenciais do ambiente
            return DriverManager.getConnection(urlDeConexao, login, senha);

        } catch (SQLException e) {
            System.err.println("Erro ao conectar ao banco de dados Oracle: " + e.getMessage());
            throw new RuntimeException("Erro de conexão com o banco de dados Oracle.", e);
        }
    }
}