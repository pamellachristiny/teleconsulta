package br.com.fiap.teleconsulta.infra.dao;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    public Connection getConnection() throws SQLException {
        String urlDeConexao = System.getenv("jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL");
        String login = System.getenv("rm565206");
        String senha = System.getenv("200806");

        if (urlDeConexao == null || login == null || senha == null) {
            throw new RuntimeException("As variáveis de ambiente do Oracle (DB_URL_ORACLE, DB_USER_ORACLE, DB_PASSWORD_ORACLE) não foram configuradas no Render.");
        }

        return DriverManager.getConnection(urlDeConexao, login, senha);
    }
}